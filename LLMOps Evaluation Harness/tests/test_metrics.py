from eval_harness.metrics import exact_match, hallucination_rate, token_f1


def test_exact_match() -> None:
    assert exact_match("Paris", "paris") == 1.0
    assert exact_match("Paris", "London") == 0.0


def test_token_f1() -> None:
    assert token_f1("blue sky", "blue sky") == 1.0
    assert token_f1("blue sky", "blue") == 0.6666666666666666


def test_hallucination_rate() -> None:
    context = "Paris is the capital of France."
    assert hallucination_rate("Paris is the capital of France.", context) == 0.0
    rate = hallucination_rate("Paris is the capital of France and Berlin.", context)
    assert rate is not None
    assert rate > 0.0
