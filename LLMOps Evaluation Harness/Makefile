PYTHON ?= python3

.PHONY: dev eval lint format test build

dev: eval

eval:
	$(PYTHON) -m eval_harness.cli --task tasks/contextual.json

lint:
	ruff check .

format:
	ruff format --check .

test:
	pytest -q

build:
	$(PYTHON) -m build
