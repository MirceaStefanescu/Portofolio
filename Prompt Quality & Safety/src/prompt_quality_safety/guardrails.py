import re
from collections.abc import Iterable
from dataclasses import dataclass

REFUSAL_MESSAGE = (
    "I can't help with that request. If you need safety guidance, consider official resources."
)

INJECTION_PATTERNS = [
    r"ignore (all|previous) instructions",
    r"reveal (the )?system prompt",
    r"developer mode",
    r"disregard (all )?policies",
    r"override (system|developer) message",
]

HARM_PATTERNS = [
    r"\bbomb\b",
    r"\bexplosives?\b",
    r"\bmalware\b",
    r"\bsteal\b",
    r"\bpasswords?\b",
]


@dataclass(frozen=True)
class GuardrailResult:
    action: str
    reason: str
    category: str


def classify_input(text: str) -> GuardrailResult:
    if not text:
        return GuardrailResult(action="allow", reason="empty input", category="none")

    if _matches(INJECTION_PATTERNS, text):
        return GuardrailResult(
            action="refuse", reason="prompt injection detected", category="injection"
        )

    if _matches(HARM_PATTERNS, text):
        return GuardrailResult(action="refuse", reason="unsafe request detected", category="safety")

    return GuardrailResult(action="allow", reason="allowed", category="none")


def is_refusal(text: str) -> bool:
    if not text:
        return False
    text_lc = text.lower()
    return any(
        phrase in text_lc
        for phrase in [
            "can't help",
            "cannot help",
            "unable to assist",
            "i can't",
            "i cannot",
            "refuse",
        ]
    )


def _matches(patterns: Iterable[str], text: str) -> bool:
    text_lc = text.lower()
    return any(re.search(pattern, text_lc) for pattern in patterns)
