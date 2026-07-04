#!/usr/bin/env python3
"""Pick a persisted FeedBook device token and send a test notification."""

import argparse
import json
import sqlite3
import sys
import urllib.error
import urllib.request
from dataclasses import dataclass
from pathlib import Path


DEFAULT_TITLE = "FeedBook"
DEFAULT_BODY = "Juanma134 comenzó a seguirte"


@dataclass(frozen=True)
class PushToken:
    token: str
    platform: str
    username: str | None


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(description="Send a hardcoded test push to a persisted FeedBook device token")
    parser.add_argument("--backend", default="http://127.0.0.1:8080", help="Backend origin")
    parser.add_argument(
        "--db",
        default=str(Path(__file__).resolve().parents[1] / "back" / "feedbook.db"),
        help="Path to backend SQLite DB",
    )
    return parser.parse_args()


def read_tokens(db_path: Path) -> list[PushToken]:
    if not db_path.exists():
        raise FileNotFoundError(f"No existe la DB: {db_path}")

    with sqlite3.connect(f"file:{db_path}?mode=ro", uri=True) as connection:
        connection.row_factory = sqlite3.Row
        rows = connection.execute(
            """
            SELECT token, platform, username
            FROM push_token_models
            ORDER BY COALESCE(username, ''), token
            """
        ).fetchall()

    return [
        PushToken(
            token=row["token"],
            platform=row["platform"] or "unknown",
            username=row["username"],
        )
        for row in rows
    ]


def token_label(token: PushToken) -> str:
    suffix = token.token[-10:] if len(token.token) > 10 else token.token
    username = token.username or "sin usuario"
    return f"{suffix} ({username}) [{token.platform}]"


def choose_token(tokens: list[PushToken]) -> PushToken:
    print("Tokens encontrados:")
    for index, token in enumerate(tokens, start=1):
        print(f"{index}. {token_label(token)}")

    while True:
        raw = input("Elegí un token para enviar la notificación: ").strip()
        try:
            selected = int(raw)
        except ValueError:
            print("Ingresá un número de la lista.")
            continue
        if 1 <= selected <= len(tokens):
            return tokens[selected - 1]
        print("Número fuera de rango.")


def send_push(backend: str, token: str) -> str:
    payload = {
        "title": DEFAULT_TITLE,
        "body": DEFAULT_BODY,
        "token": token,
    }

    url = backend.rstrip("/") + "/api/push/send"
    request = urllib.request.Request(
        url,
        data=json.dumps(payload).encode("utf-8"),
        headers={"Content-Type": "application/json"},
        method="POST",
    )

    with urllib.request.urlopen(request, timeout=15) as response:
        return response.read().decode("utf-8").strip()


def main() -> int:
    args = parse_args()

    try:
        tokens = read_tokens(Path(args.db))
    except (FileNotFoundError, sqlite3.Error) as error:
        print(f"No pude leer tokens desde SQLite: {error}", file=sys.stderr)
        return 1
    if not tokens:
        print("No hay tokens persistidos en la DB. Abrí la app con el backend levantado primero.", file=sys.stderr)
        return 1

    selected = choose_token(tokens)
    print(f"Enviando push hardcodeada a {token_label(selected)}...")

    try:
        print(send_push(args.backend, selected.token))
        return 0
    except urllib.error.HTTPError as error:
        print(error.read().decode("utf-8").strip(), file=sys.stderr)
        return 1
    except urllib.error.URLError as error:
        print(f"request failed: {error}", file=sys.stderr)
        return 1


if __name__ == "__main__":
    raise SystemExit(main())
