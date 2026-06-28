#include <WiFi.h>
#include "esp_netif.h"
#include "dhcpserver/dhcpserver.h"

const char *SSID = "FeedBook S.R.L.";
const char *PASSWORD = "super-poc";

void setup() {
  Serial.begin(115200);

  IPAddress ap_ip(192, 168, 4, 1);
  IPAddress gateway(192, 168, 4, 1);
  IPAddress subnet(255, 255, 255, 0);

  WiFi.mode(WIFI_AP);
  WiFi.softAPConfig(ap_ip, gateway, subnet);
  WiFi.softAP(SSID, PASSWORD);

  esp_netif_t *ap_netif = esp_netif_get_handle_from_ifkey("WIFI_AP_DEF");

  esp_netif_dhcps_stop(ap_netif);

  dhcps_lease_t lease;
  lease.enable = true;
  IP4_ADDR(&lease.start_ip, 192, 168, 4, 3);
  IP4_ADDR(&lease.end_ip,   192, 168, 4, 20);

  esp_netif_dhcps_option(
    ap_netif,
    ESP_NETIF_OP_SET,
    ESP_NETIF_REQUESTED_IP_ADDRESS,
    &lease,
    sizeof(lease)
  );

  esp_netif_dhcps_start(ap_netif);

  Serial.print("AP IP: ");
  Serial.println(WiFi.softAPIP());
}

void loop() {}
