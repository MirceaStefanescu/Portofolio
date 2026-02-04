from eval_harness.models.base import BaseModel
from eval_harness.types import EvalRecord


class MockModel(BaseModel):
    name = "mock"

    def generate(self, record: EvalRecord, prompt: str) -> str:
        if "mock_answer" in record.metadata:
            return str(record.metadata["mock_answer"])
        if record.expected is not None:
            return record.expected
        return "mock-response"
