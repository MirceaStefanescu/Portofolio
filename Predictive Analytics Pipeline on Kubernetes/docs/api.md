# API Usage Examples

Base URL (local): `http://localhost:8082`

## Health
```
curl http://localhost:8082/api/health
```

## Publish an event
```
curl -X POST http://localhost:8082/api/events \
  -H "Content-Type: application/json" \
  -d '{"eventType":"purchase","value":42.5,"timestamp":"2024-05-01T12:00:00Z"}'
```

## List predictions
```
curl "http://localhost:8082/api/predictions?eventType=purchase&limit=10"
```

## Search predictions (Elasticsearch)
```
curl "http://localhost:8082/api/predictions/search?q=purchase&limit=5"
```

## Notes
- Predictions are windowed in 30-second intervals by event time.
- Elasticsearch responses are returned as raw JSON from the `_search` API.
