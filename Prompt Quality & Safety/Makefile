PYTHON ?= python3
PYTHONPATH ?= src

.PHONY: dev test lint format clean

dev:
	PYTHONPATH=$(PYTHONPATH) $(PYTHON) -m prompt_quality_safety.cli --suite suites/default.json --output runs/latest

test:
	PYTHONPATH=$(PYTHONPATH) pytest

lint:
	ruff check src tests
	ruff format --check src tests

format:
	ruff format src tests

clean:
	rm -rf runs/latest
