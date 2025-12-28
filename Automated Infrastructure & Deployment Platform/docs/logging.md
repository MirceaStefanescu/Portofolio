# Logging

The portal emits structured JSON logs to stdout with a `requestId` to correlate user actions.

Example:
```
{"timestamp":"2024-05-06T12:00:00.000Z","level":"info","message":"pipeline.generated","requestId":"b2c6c1d2-7d1d-4d6d-9c9a-2a7c2b8f1a1e","actionId":"b2c6c1d2-7d1d-4d6d-9c9a-2a7c2b8f1a1e","provider":"github","appName":"orders-api","environment":"dev"}
```
