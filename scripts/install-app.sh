#!/usr/bin/env bash

set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
JAVA_HOME="${JAVA_HOME:-/usr/lib/jvm/java-17-openjdk}"
BACK_PORT="${BACK_PORT:-8080}"

usage() {
  cat <<'EOF'
Uso:
  scripts/install-app.sh [device_serial]

Compila e instala la app debug en un dispositivo Android conectado por adb.
Si no se pasa device_serial, usa ANDROID_SERIAL o detecta automaticamente
un unico dispositivo en estado "device".
EOF
}

require_cmd() {
  local cmd="$1"
  if ! command -v "$cmd" >/dev/null 2>&1; then
    echo "Falta dependencia requerida: $cmd" >&2
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

main() {
  require_cmd adb

  if [[ ! -d "$JAVA_HOME" ]]; then
    echo "No existe JAVA_HOME=$JAVA_HOME" >&2
    echo "En Arch instalalo con: sudo pacman -S jdk17-openjdk" >&2
    exit 1
  fi

  local device_serial
  device_serial="$(resolve_device_serial "$@")"

  echo "Usando Java: $JAVA_HOME"
  echo "Usando dispositivo: $device_serial"
  echo "Compilando e instalando app debug..."

  (
    cd "$ROOT_DIR"
    export JAVA_HOME
    ANDROID_SERIAL="$device_serial" ./gradlew installDebug
  )

  ensure_reverse "$device_serial"

  echo "App instalada en $device_serial."
}

main "$@"
