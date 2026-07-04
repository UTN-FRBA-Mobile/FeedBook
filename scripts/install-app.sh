#!/usr/bin/env bash

set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
JAVA_HOME="${JAVA_HOME:-/usr/lib/jvm/java-17-openjdk}"
APP_ID="com.example.feedbook"
BACK_PORT="${BACK_PORT:-8080}"

usage() {
  cat <<'EOF'
Uso:
  scripts/install-app.sh [-f|--force] [device_serial]

Compila e instala la app debug en un dispositivo Android conectado por adb.
Si no se pasa device_serial, usa ANDROID_SERIAL o detecta automaticamente
un unico dispositivo en estado "device".

Opciones:
  -f, --force  Desinstala com.example.feedbook de todos los perfiles antes de instalar.
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

parse_args() {
  FORCE_INSTALL=0
  DEVICE_SERIAL_ARG=""

  while [[ $# -gt 0 ]]; do
    case "$1" in
      -f|--force)
        FORCE_INSTALL=1
        ;;
      -h|--help)
        usage
        exit 0
        ;;
      -*)
        echo "Opcion desconocida: $1" >&2
        usage >&2
        exit 1
        ;;
      *)
        if [[ -n "$DEVICE_SERIAL_ARG" ]]; then
          echo "Solo se admite un device_serial." >&2
          usage >&2
          exit 1
        fi
        DEVICE_SERIAL_ARG="$1"
        ;;
    esac
    shift
  done
}

force_uninstall_app() {
  local device_serial="$1"
  local users line user_id
  local user_ids=()
  local uninstall_output
  local package_state
  local removed=0

  echo "Desinstalando $APP_ID de perfiles existentes..."

  users="$(adb -s "$device_serial" shell cmd package list users | tr -d '\r')"
  while IFS= read -r line; do
    if [[ "$line" =~ UserInfo\{([0-9]+): ]]; then
      user_ids+=("${BASH_REMATCH[1]}")
    fi
  done <<<"$users"

  for user_id in "${user_ids[@]}"; do
    echo "Desinstalando $APP_ID del usuario $user_id..."
    uninstall_output="$(adb -s "$device_serial" shell pm uninstall --user "$user_id" "$APP_ID" 2>&1 | tr -d '\r' || true)"
    if grep -q "^Success$" <<<"$uninstall_output"; then
      removed=1
    elif grep -q "not installed for" <<<"$uninstall_output"; then
      echo "$APP_ID no estaba instalado en el usuario $user_id."
    else
      echo "$uninstall_output"
    fi
  done

  if [[ "$removed" -eq 0 ]]; then
    echo "$APP_ID no estaba instalado en ningun perfil."
  fi

  package_state="$(adb -s "$device_serial" shell dumpsys package "$APP_ID" | tr -d '\r')"
  if grep -q "Package \[$APP_ID\]" <<<"$package_state"; then
    echo "No se pudo desinstalar $APP_ID de todos los perfiles. Estado restante:" >&2
    grep -E "User [0-9]+:|installed=|codePath=|signatures=" <<<"$package_state" >&2 || true
    exit 1
  fi
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

main() {
  require_cmd adb
  parse_args "$@"

  if [[ ! -d "$JAVA_HOME" ]]; then
    echo "No existe JAVA_HOME=$JAVA_HOME" >&2
    echo "En Arch instalalo con: sudo pacman -S jdk17-openjdk" >&2
    exit 1
  fi

  local device_serial
  if [[ -n "$DEVICE_SERIAL_ARG" ]]; then
    device_serial="$(resolve_device_serial "$DEVICE_SERIAL_ARG")"
  else
    device_serial="$(resolve_device_serial)"
  fi

  echo "Usando Java: $JAVA_HOME"
  echo "Usando dispositivo: $device_serial"
  ensure_reverse "$device_serial"

  if [[ "$FORCE_INSTALL" -eq 1 ]]; then
    force_uninstall_app "$device_serial"
  fi

  echo "Compilando e instalando app debug..."

  (
    cd "$ROOT_DIR"
    export JAVA_HOME
    ANDROID_SERIAL="$device_serial" bash ./gradlew installDebug
  )

  echo "App instalada en $device_serial."
}

main "$@"
