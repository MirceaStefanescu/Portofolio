from pathlib import Path

from prompt_quality_safety.models import MockModel
from prompt_quality_safety.runner import run_suite


def test_run_suite(tmp_path: Path) -> None:
    project_root = Path(__file__).resolve().parents[1]
    suite_path = project_root / "suites" / "default.json"
    summary = run_suite(suite_path=suite_path, model=MockModel(), output_dir=tmp_path)

    assert summary["suite"] == "default"
    assert summary["overall"]["cases"] > 0
    assert "keyword_recall" in summary["overall"]["metrics"]
