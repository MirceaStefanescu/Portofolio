from pathlib import Path


def load_prompt(path: Path) -> str:
    return path.read_text(encoding="utf-8").strip()


def build_prompt(system_prompt: str, user_input: str, context: str | None) -> str:
    parts = [system_prompt.strip(), "", f"User: {user_input.strip()}"]
    if context:
        parts.append("")
        parts.append("Context:")
        parts.append(context.strip())
    return "\n".join(parts).strip()
