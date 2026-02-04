import argparse
import json
from pathlib import Path

from prompt_quality_safety.models import MockModel
from prompt_quality_safety.runner import run_suite


def main() -> None:
    parser = argparse.ArgumentParser(description="Prompt Quality & Safety eval runner")
    parser.add_argument("--suite", default="suites/default.json", help="Path to suite JSON")
    parser.add_argument("--output", default="runs/latest", help="Output directory for artifacts")
    parser.add_argument("--no-output", action="store_true", help="Disable artifact writing")
    args = parser.parse_args()

    suite_path = Path(args.suite)
    output_dir = None if args.no_output else Path(args.output)

    summary = run_suite(suite_path=suite_path, model=MockModel(), output_dir=output_dir)
    print(json.dumps(summary, indent=2))


if __name__ == "__main__":
    main()
