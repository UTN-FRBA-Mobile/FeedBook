#!/usr/bin/env bash

set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
BACK_DIR="$ROOT_DIR/back"
APP_ID="com.example.feedbook"
MAIN_ACTIVITY="com.example.feedbook/.MainActivity"
BACK_HOST="127.0.0.1"
BACK_PORT="8080"
BACK_PID_FILE="$ROOT_DIR/.feedbook-back.pid"
BACK_LOG_FILE="/tmp/feedbook-back.log"
SCRCPY_LOG_FILE="/tmp/feedbook-scrcpy.log"
CLEAR_DEVICE_HTTP_PROXY="${CLEAR_DEVICE_HTTP_PROXY:-0}"

usage() {
  cat <<'EOF'
Uso:
  scripts/run-on-device.sh [device_serial]

Opciones:
  device_serial      Serial adb del dispositivo. Si no se pasa, usa ANDROID_SERIAL
                     o detecta automaticamente un unico dispositivo en estado "device".

Variables de entorno:
  CLEAR_DEVICE_HTTP_PROXY=1  Limpia el proxy HTTP global del dispositivo si esta seteado.
EOF
}

require_cmd() {
  local cmd="$1"
  if ! command -v "$cmd" >/dev/null 2>&1; then
    echo "Falta dependencia requerida: $cmd" >&2
    exit 1
  fi
}

resolve_device_serial() {
  if [[ $# -ge 1 && "$1" =~ ^(-h|--help)$ ]]; then
    usage
    exit 0
  fi

  if [[ $# -ge 1 ]]; then
    echo "$1"
    return
  fi

  if [[ -n "${ANDROID_SERIAL:-}" ]]; then
    echo "$ANDROID_SERIAL"
    return
  fi

  mapfile -t devices < <(adb devices | awk 'NR > 1 && $2 == "device" { print $1 }')

  if [[ "${#devices[@]}" -eq 0 ]]; then
    echo "No hay dispositivos adb conectados en estado \"device\"." >&2
    exit 1
  fi

  if [[ "${#devices[@]}" -gt 1 ]]; then
    printf 'Hay multiples dispositivos conectados: %s\n' "${devices[*]}" >&2
    echo "Pasame el serial como argumento o exporta ANDROID_SERIAL." >&2
    exit 1
  fi

  echo "${devices[0]}"
}

is_backend_up() {
  local status
  status="$(curl -s -o /dev/null -w '%{http_code}' "http://${BACK_HOST}:${BACK_PORT}/login" || true)"
  [[ "$status" != "000" ]]
}

cleanup_stale_pid() {
  if [[ -f "$BACK_PID_FILE" ]]; then
    local pid
    pid="$(cat "$BACK_PID_FILE")"
    if [[ -z "$pid" ]] || ! kill -0 "$pid" >/dev/null 2>&1; then
      rm -f "$BACK_PID_FILE"
    fi
  fi
}

stop_existing_backend() {
  if [[ -f "$BACK_PID_FILE" ]]; then
    local pid
    pid="$(cat "$BACK_PID_FILE")"
    if [[ -n "$pid" ]] && kill -0 "$pid" >/dev/null 2>&1; then
      echo "Deteniendo backend existente (PID $pid)..."
      kill "$pid" 2>/dev/null || true
      for _ in {1..10}; do
        if ! kill -0 "$pid" >/dev/null 2>&1; then
          break
        fi
        sleep 1
      done
    fi
    rm -f "$BACK_PID_FILE"
  fi

  if is_backend_up; then
    echo "Backend responde en el puerto ${BACK_PORT}, forzando detencion..."
    fuser -k "${BACK_PORT}/tcp" 2>/dev/null || true
    sleep 1
  fi
}

start_backend() {
  stop_existing_backend
  cleanup_stale_pid

  echo "Levantando backend..."
  (
    cd "$BACK_DIR"
    nohup go run . >"$BACK_LOG_FILE" 2>&1 &
    echo $! >"$BACK_PID_FILE"
  )

  for _ in {1..30}; do
    if is_backend_up; then
      echo "Backend listo. Log: $BACK_LOG_FILE"
      return
    fi
    sleep 1
  done

  echo "El backend no levanto a tiempo. Revisar log: $BACK_LOG_FILE" >&2
  exit 1
}

is_scrcpy_running() {
  local device_serial="$1"
  pgrep -f "scrcpy --serial ${device_serial}" >/dev/null 2>&1
}

get_device_http_proxy() {
  local device_serial="$1"
  adb -s "$device_serial" shell settings get global http_proxy 2>/dev/null | tr -d '\r'
}

ensure_device_http_proxy() {
  local device_serial="$1"
  local proxy_value
  proxy_value="$(get_device_http_proxy "$device_serial")"

  if [[ -z "$proxy_value" || "$proxy_value" == "null" || "$proxy_value" == ":0" ]]; then
    echo "Proxy global del dispositivo: limpio."
    return
  fi

  if [[ "$CLEAR_DEVICE_HTTP_PROXY" == "1" ]]; then
    echo "Limpiando proxy global del dispositivo ($proxy_value)..."
    adb -s "$device_serial" shell settings put global http_proxy :0
    echo "Proxy global del dispositivo: limpio."
    return
  fi

  cat >&2 <<EOF
El dispositivo tiene un proxy HTTP global configurado: $proxy_value
Eso puede desviar trafico de localhost y romper el login local.

Volvé a correr con:
  CLEAR_DEVICE_HTTP_PROXY=1 scripts/run-on-device.sh ${device_serial}
EOF
  exit 1
}

ensure_reverse() {
  local device_serial="$1"
  local reverse_output

  reverse_output="$(adb -s "$device_serial" reverse --list | tr -d '\r')"
  if grep -q "tcp:${BACK_PORT} tcp:${BACK_PORT}" <<<"$reverse_output"; then
    echo "adb reverse ya activo para tcp:${BACK_PORT}."
    return
  fi

  echo "Configurando adb reverse tcp:${BACK_PORT} -> tcp:${BACK_PORT}..."
  adb -s "$device_serial" reverse --remove "tcp:${BACK_PORT}" >/dev/null 2>&1 || true
  adb -s "$device_serial" reverse "tcp:${BACK_PORT}" "tcp:${BACK_PORT}"
}

launch_app() {
  local device_serial="$1"

  echo "Relanzando app..."
  adb -s "$device_serial" shell am force-stop "$APP_ID"
  adb -s "$device_serial" shell am start -n "$MAIN_ACTIVITY" >/dev/null
}

main() {
  require_cmd adb
  require_cmd curl
  require_cmd go
  require_cmd nohup
  require_cmd fuser
  require_cmd scrcpy

  local device_serial
  device_serial="$(resolve_device_serial "$@")"

  echo "Usando dispositivo: $device_serial"

  start_backend
  ensure_device_http_proxy "$device_serial"

  echo "Compilando e instalando app debug..."
  (
    cd "$ROOT_DIR"
    ANDROID_SERIAL="$device_serial" ./gradlew installDebug
  )

  ensure_reverse "$device_serial"
  launch_app "$device_serial"

  if is_scrcpy_running "$device_serial"; then
    echo "scrcpy ya esta corriendo para ${device_serial}."
  else
    echo "Abriendo scrcpy..."
    nohup scrcpy --serial "$device_serial" --always-on-top >"$SCRCPY_LOG_FILE" 2>&1 &
  fi

  cat <<EOF
Listo.
- Backend: http://${BACK_HOST}:${BACK_PORT}
- App instalada: ${APP_ID}
- Dispositivo: ${device_serial}
- Proxy global dispositivo: $(get_device_http_proxy "$device_serial")
- Log backend: ${BACK_LOG_FILE}
- Log scrcpy: ${SCRCPY_LOG_FILE}
EOF
}

main "$@"
