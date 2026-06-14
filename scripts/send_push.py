#!/usr/bin/env python3
"""Send a FeedBook push notification through the local backend."""

import argparse
import json
import sys
import urllib.error
import urllib.request


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(description="Send a push notification through FeedBook backend")
    parser.add_argument("--backend", default="http://127.0.0.1:8080", help="Backend origin")
    parser.add_argument("--title", required=True, help="Notification title")
    parser.add_argument("--body", required=True, help="Notification body")
    parser.add_argument("--token", help="Optional FCM token; defaults to all registered tokens")
    parser.add_argument(
        "--data",
        action="append",
        default=[],
        metavar="KEY=VALUE",
        help="Optional data payload item; can be passed multiple times",
    )
    return parser.parse_args()


def parse_data(items: list[str]) -> dict[str, str]:
    data: dict[str, str] = {}
    for item in items:
        if "=" not in item:
            raise ValueError(f"invalid --data value {item!r}; expected KEY=VALUE")
        key, value = item.split("=", 1)
        if not key:
            raise ValueError("data keys cannot be empty")
        data[key] = value
    return data


def main() -> int:
    args = parse_args()
    try:
        data = parse_data(args.data)
    except ValueError as error:
        print(error, file=sys.stderr)
        return 2

    payload = {
        "title": args.title,
        "body": args.body,
    }
    if args.token:
        payload["token"] = args.token
    if data:
        payload["data"] = data

    url = args.backend.rstrip("/") + "/api/push/send"
    request = urllib.request.Request(
        url,
        data=json.dumps(payload).encode("utf-8"),
        headers={"Content-Type": "application/json"},
        method="POST",
    )

    try:
        with urllib.request.urlopen(request, timeout=15) as response:
            body = response.read().decode("utf-8").strip()
            print(body)
            try:
                parsed = json.loads(body)
            except json.JSONDecodeError:
                return 0
            if parsed.get("sent") == 0 and parsed.get("message") == "no registered push tokens":
                tokens_url = args.backend.rstrip("/") + "/api/push/tokens"
                print(
                    f"No hay tokens registrados. Abrí la app con el backend levantado y revisá {tokens_url}",
                    file=sys.stderr,
                )
                return 1
            return 0
    except urllib.error.HTTPError as error:
        print(error.read().decode("utf-8").strip(), file=sys.stderr)
        return 1
    except urllib.error.URLError as error:
        print(f"request failed: {error}", file=sys.stderr)
        return 1


if __name__ == "__main__":
    raise SystemExit(main())
