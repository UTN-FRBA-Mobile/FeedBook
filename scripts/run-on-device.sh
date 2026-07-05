#!/usr/bin/env bash

set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
BACK_DIR="$ROOT_DIR/back"
APP_ID="com.example.feedbook"
TARGET_USER_ID="${TARGET_USER_ID:-0}"
BACK_HOST="127.0.0.1"
BACK_PORT="8080"
BACK_DB_FILE="$BACK_DIR/feedbook.db"
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

  echo "Levantando backend (SQLite)..."
  (
    cd "$BACK_DIR"
    setsid env GOCACHE=/tmp/feedbook-go-cache GOMODCACHE=/tmp/feedbook-go-mod FEEDBOOK_STORE=sqlite go run . >"$BACK_LOG_FILE" 2>&1 < /dev/null &
    echo $! >"$BACK_PID_FILE"
  )

  for _ in {1..120}; do
    if [[ -f "$BACK_PID_FILE" ]]; then
      local pid
      pid="$(cat "$BACK_PID_FILE")"
      if [[ -n "$pid" ]] && ! kill -0 "$pid" >/dev/null 2>&1; then
        echo "El backend se detuvo al arrancar. Revisar log: $BACK_LOG_FILE" >&2
        tail -n 40 "$BACK_LOG_FILE" >&2 || true
        exit 1
      fi
    fi
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
  local start_output

  echo "Relanzando app..."
  adb -s "$device_serial" shell am force-stop "$APP_ID"
  if start_output="$(
    adb -s "$device_serial" shell am start -W \
      --user "$TARGET_USER_ID" \
      -n "$APP_ID/.MainActivity" 2>&1
  )"; then
    printf '%s\n' "$start_output"
    return
  fi

  if start_output="$(
    adb -s "$device_serial" shell am start -W \
      --user "$TARGET_USER_ID" \
      -n "$APP_ID/$APP_ID.MainActivity" 2>&1
  )"; then
    printf '%s\n' "$start_output"
    return
  fi

  if start_output="$(
    adb -s "$device_serial" shell am start -W \
      --user "$TARGET_USER_ID" \
      -a android.intent.action.MAIN \
      -c android.intent.category.LAUNCHER \
      -p "$APP_ID" 2>&1
  )"; then
    printf '%s\n' "$start_output"
    return
  fi

  echo "No se pudo abrir con am start. Ultimo error:" >&2
  printf '%s\n' "$start_output" >&2
  echo "Usando fallback con monkey..." >&2
  adb -s "$device_serial" shell monkey -p "$APP_ID" -c android.intent.category.LAUNCHER 1
}

main() {
  require_cmd adb
  require_cmd curl
  require_cmd go
  require_cmd nohup
  require_cmd setsid
  require_cmd fuser
  require_cmd scrcpy

  local device_serial
  device_serial="$(resolve_device_serial "$@")"

  echo "Usando dispositivo: $device_serial"

  ensure_device_http_proxy "$device_serial"
  ensure_reverse "$device_serial"
  start_backend

  echo "Compilando app debug..."
  (
    cd "$ROOT_DIR"
    export JAVA_HOME=/usr/lib/jvm/java-17-openjdk
    bash ./gradlew --stop 2>/dev/null || true
    bash ./gradlew assembleDebug
  )

  echo "Instalando APK en ${device_serial}..."
  if ! install_output="$(
    adb -s "$device_serial" install -r --user "$TARGET_USER_ID" "$ROOT_DIR/app/build/outputs/apk/debug/app-debug.apk" 2>&1
  )"; then
    echo "Fallo la instalacion del APK:" >&2
    printf '%s\n' "$install_output" >&2
    if grep -q "INSTALL_FAILED_UPDATE_INCOMPATIBLE\|INSTALL_FAILED_VERSION_DOWNGRADE" <<<"$install_output"; then
      echo "Desinstalando la version previa e intentando de nuevo..." >&2
      adb -s "$device_serial" shell pm uninstall --user "$TARGET_USER_ID" "$APP_ID" >/dev/null 2>&1 || true
      if ! install_output="$(
        adb -s "$device_serial" install --user "$TARGET_USER_ID" "$ROOT_DIR/app/build/outputs/apk/debug/app-debug.apk" 2>&1
      )"; then
        echo "La reinstalacion tambien fallo:" >&2
        printf '%s\n' "$install_output" >&2
        exit 1
      fi
    else
      exit 1
    fi
  fi
  printf '%s\n' "$install_output"

  local package_seen=0
  for _ in {1..5}; do
    if adb -s "$device_serial" shell pm path --user "$TARGET_USER_ID" "$APP_ID" >/dev/null 2>&1; then
      package_seen=1
      break
    fi
    sleep 1
  done

  if [[ "$package_seen" -ne 1 ]]; then
    echo "El paquete ${APP_ID} no quedo visible en ${device_serial} segun pm path." >&2
    echo "Paquetes que matchean feedbook en el dispositivo:" >&2
    adb -s "$device_serial" shell pm list packages --user "$TARGET_USER_ID" | grep 'feedbook' >&2 || true
    exit 1
  fi

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
