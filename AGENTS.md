# FeedBook Agent Notes

## Firebase Push Notifications

- Android FCM config lives in `app/google-services.json`. This file is intentionally ignored by Git.
- Backend Firebase Admin credentials live in `back/*firebase-adminsdk*.json` or `back/firebase-service-account.json`. These files are intentionally ignored by Git.
- Firebase project ID default is hardcoded in `back/main.go` as `feedbook-9132b`.
- Backend auto-detects `*firebase-adminsdk*.json` in `back/`. Env overrides still work:
  - `FIREBASE_CREDENTIALS_FILE=/path/to/service-account.json`
  - `FIREBASE_PROJECT_ID=<project-id>`

## Local Push Test Flow

1. Start backend:

   ```bash
   cd back
   go run .
   ```

2. For a physical Android device, expose localhost:

   ```bash
   adb reverse tcp:8080 tcp:8080
   ```

3. Open the app once. The app registers its FCM token with:

   ```http
   POST /api/push/register
   ```

4. Check registered tokens:

   ```bash
   curl http://127.0.0.1:8080/api/push/tokens
   ```

5. Send a push through the backend:

   ```bash
   python3 scripts/send_push.py --title "FeedBook" --body "Prueba de notificación"
   ```

## Important Details

- Push tokens are stored in backend memory only. After restarting `go run .`, open the app again before sending.
- If `/api/push/send` returns `cloudmessaging.messages.create denied`, the service account needs the `Firebase Cloud Messaging API Admin` role (`roles/firebasecloudmessaging.admin`) and IAM may need a short propagation delay.
- For Android emulator, `BuildConfig.BACKEND_ORIGIN` must use `http://10.0.2.2:8080/`. For physical device with `adb reverse`, `http://localhost:8080/` is fine.

## Useful Validation Commands

```bash
cd back && GOCACHE=/tmp/feedbook-go-cache GOMODCACHE=/tmp/feedbook-go-mod go test ./...
GRADLE_USER_HOME=/tmp/feedbook-gradle ./gradlew assembleDebug
python3 -m py_compile scripts/send_push.py
```
