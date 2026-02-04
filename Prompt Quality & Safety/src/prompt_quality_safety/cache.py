from dataclasses import dataclass, field
import hashlib
from typing import Dict


def cache_key(text: str) -> str:
    return hashlib.sha256(text.encode("utf-8")).hexdigest()


@dataclass
class InMemoryCache:
    store: Dict[str, str] = field(default_factory=dict)

    def get(self, key: str) -> str | None:
        return self.store.get(key)

    def set(self, key: str, value: str) -> None:
        self.store[key] = value
