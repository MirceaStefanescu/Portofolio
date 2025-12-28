import http from "k6/http";
import { check, sleep } from "k6";

export const options = {
  vus: 5,
  duration: "30s",
  thresholds: {
    http_req_failed: ["rate<0.01"],
    http_req_duration: ["p(95)<750"],
  },
};

const baseUrl = __ENV.BASE_URL || "http://localhost:8082";
const eventTypes = ["temperature", "purchase", "latency"];

export default function () {
  const eventType = eventTypes[__ITER % eventTypes.length];
  const payload = JSON.stringify({
    eventType,
    value: Math.random() * 100,
    timestamp: new Date().toISOString(),
  });

  const params = {
    headers: { "Content-Type": "application/json" },
  };

  const postRes = http.post(`${baseUrl}/api/events`, payload, params);
  check(postRes, { "event accepted": (res) => res.status === 202 });

  const listRes = http.get(
    `${baseUrl}/api/predictions?eventType=${eventType}&limit=5`
  );
  check(listRes, { "predictions ok": (res) => res.status === 200 });

  sleep(1);
}
