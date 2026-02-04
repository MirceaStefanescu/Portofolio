from dataclasses import dataclass, field
from typing import Any, Dict, Optional


@dataclass
class EvalRecord:
    record_id: str
    input: str
    expected: Optional[str] = None
    context: Optional[str] = None
    metadata: Dict[str, Any] = field(default_factory=dict)
