import http from "k6/http";
import { check, sleep } from "k6";

export const options = {
  vus: 10,
  duration: "30s",
  thresholds: {
    http_req_failed: ["rate<0.01"],
    http_req_duration: ["p(95)<500"],
  },
};

const baseUrl = __ENV.BASE_URL || "http://localhost:8010";

export default function () {
  const payload = JSON.stringify({
    name: `coffee-${__VU}-${__ITER}`,
    description: "1kg bag",
    price_cents: 1899,
    currency: "USD",
  });

  const params = {
    headers: { "Content-Type": "application/json" },
  };

  const createRes = http.post(`${baseUrl}/products`, payload, params);
  check(createRes, { "create status is 201": (res) => res.status === 201 });

  const listRes = http.get(`${baseUrl}/products`);
  check(listRes, { "list status is 200": (res) => res.status === 200 });

  sleep(1);
}
