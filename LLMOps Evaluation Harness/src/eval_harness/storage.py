import json
from pathlib import Path
from typing import Any, Dict


class RunLogger:
    def __init__(self, run_dir: Path) -> None:
        self.run_dir = run_dir
        self.requests_file = open(run_dir / "requests.jsonl", "a", encoding="utf-8")
        self.responses_file = open(run_dir / "responses.jsonl", "a", encoding="utf-8")
        self.scores_file = open(run_dir / "scores.jsonl", "a", encoding="utf-8")

    def log_request(self, payload: Dict[str, Any]) -> None:
        self._write(self.requests_file, payload)

    def log_response(self, payload: Dict[str, Any]) -> None:
        self._write(self.responses_file, payload)

    def log_score(self, payload: Dict[str, Any]) -> None:
        self._write(self.scores_file, payload)

    def write_summary(self, payload: Dict[str, Any]) -> None:
        (self.run_dir / "summary.json").write_text(
            json.dumps(payload, indent=2, sort_keys=True),
            encoding="utf-8",
        )

    def close(self) -> None:
        self.requests_file.close()
        self.responses_file.close()
        self.scores_file.close()

    def __enter__(self) -> "RunLogger":
        return self

    def __exit__(self, exc_type, exc, traceback) -> None:
        self.close()

    @staticmethod
    def _write(handle, payload: Dict[str, Any]) -> None:
        handle.write(json.dumps(payload) + "\n")
        handle.flush()
