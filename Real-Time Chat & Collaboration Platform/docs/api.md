# API

## REST Endpoints
- All endpoints require OAuth2 login unless stated otherwise.
- `GET /api/me`
  - Returns the authenticated user profile.
- `GET /api/rooms`
  - Lists chat rooms.
- `POST /api/rooms`
  - Creates a new room.
  - Body: `{ "name": "General" }`
- `GET /api/rooms/{roomId}/messages?limit=50`
  - Returns recent messages for a room.

## WebSocket/STOMP
- Connect: `/ws`
- Subscribe: `/topic/rooms/{roomId}`
- Send: `/app/rooms/{roomId}/send`
  - Body: `{ "sender": "Mircea", "content": "Hello" }`
