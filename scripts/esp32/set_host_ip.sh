#!/usr/bin/env bash
set -euo pipefail

IP="192.168.4.2/24"
GW="192.168.4.1"

mapfile -t IFACES < <(
    nmcli -t -f DEVICE,TYPE,STATE device status |
    awk -F: '$2 == "wifi" && $3 == "connected" {print $1}'
)

if (( ${#IFACES[@]} == 0 )); then
    echo "No hay interfaces Wi-Fi conectadas."
    exit 1
fi

echo "Interfaces Wi-Fi conectadas:"
echo

for i in "${!IFACES[@]}"; do
    iface="${IFACES[$i]}"

    current_ip="$(ip -4 -o addr show "$iface" 2>/dev/null \
        | awk '{print $4}' \
        | cut -d/ -f1)"

    if [[ -z "$current_ip" ]]; then
        current_ip="sin IPv4"
    fi

    printf "%2d) %-10s (%s)\n" \
        "$((i + 1))" \
        "$iface" \
        "$current_ip"
done

echo
read -rp "Seleccioná la interfaz conectada al ESP32: " opt

if ! [[ "$opt" =~ ^[0-9]+$ ]] || (( opt < 1 || opt > ${#IFACES[@]} )); then
    echo "Opción inválida."
    exit 1
fi

IFACE="${IFACES[$((opt - 1))]}"

CONN="$(nmcli -t -f NAME,DEVICE connection show --active \
    | awk -F: -v iface="$IFACE" '$2 == iface {print $1; exit}')"

if [[ -z "$CONN" ]]; then
    echo "No encuentro conexión activa en $IFACE"
    exit 1
fi

echo
echo "[+] Interfaz: $IFACE"
echo "[+] Perfil: $CONN"
echo "[+] Configurando IP fija $IP"

sudo nmcli connection modify "$CONN" \
    ipv4.method manual \
    ipv4.addresses "$IP" \
    ipv4.gateway "$GW" \
    ipv4.dns "" \
    ipv4.never-default yes \
    ipv6.method disabled

echo "[+] Reconectando..."

nmcli connection down "$CONN" || true
nmcli connection up "$CONN"

echo
echo "[+] Estado final:"
ip -4 addr show "$IFACE" | grep inet || true

echo
echo "[+] Rutas:"
ip route

echo
echo "[+] Prueba de conectividad:"
ping -c 3 "$GW"
