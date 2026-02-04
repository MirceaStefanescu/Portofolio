import json
from pathlib import Path

from eval_harness.models.mock import MockModel
from eval_harness.runner import TaskConfig, run_evaluation


def write_dataset(path: Path) -> None:
    rows = [
        {"id": "r1", "input": "2+2?", "expected": "4"},
        {"id": "r2", "input": "Sky color?", "expected": "blue"},
    ]
    with path.open("w", encoding="utf-8") as handle:
        for row in rows:
            handle.write(json.dumps(row) + "\n")


def test_run_evaluation(tmp_path: Path) -> None:
    dataset_path = tmp_path / "data.jsonl"
    write_dataset(dataset_path)

    task = TaskConfig(
        name="unit-test",
        dataset_path=dataset_path,
        metrics=["accuracy"],
        prompt_template="Question: {input}\nAnswer:",
    )
    result = run_evaluation(task, MockModel(), tmp_path, run_name="run-1")
    summary = result["summary"]

    assert summary["total_records"] == 2
    assert summary["metrics"]["accuracy"] == 1.0
    assert Path(result["run_dir"]).exists()
