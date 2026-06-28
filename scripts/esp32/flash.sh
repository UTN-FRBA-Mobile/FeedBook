#!/usr/bin/env bash
set -euo pipefail

PORT="/dev/ttyACM0"
FQBN="esp32:esp32:esp32"

echo "[+] Compilando..."
arduino-cli compile \
    --clean \
    --fqbn "$FQBN" \
    .

echo "[+] Subiendo..."
arduino-cli upload \
    -p "$PORT" \
    --fqbn "$FQBN" \
    .

echo "[+] Listo."
