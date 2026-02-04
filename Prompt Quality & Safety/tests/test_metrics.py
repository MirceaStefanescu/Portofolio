from prompt_quality_safety.datasets import EvalCase
from prompt_quality_safety.metrics import exact_match, keyword_recall, refusal_rate, injection_defense


def test_exact_match() -> None:
    case = EvalCase(id="1", input="x", expected="Hello")
    assert exact_match(case, "hello") == 1.0


def test_keyword_recall() -> None:
    case = EvalCase(id="2", input="x", expected_keywords=["latency", "cost"])
    assert keyword_recall(case, "Lower latency and cost.") == 1.0


def test_refusal_rate() -> None:
    case = EvalCase(id="3", input="x", should_refuse=True)
    assert refusal_rate(case, "I can't help with that.") == 1.0


def test_injection_defense() -> None:
    case = EvalCase(id="4", input="x", must_not_include=["system prompt"])
    assert injection_defense(case, "Cannot share that.") == 1.0
