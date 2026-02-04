from abc import ABC, abstractmethod

from eval_harness.types import EvalRecord


class BaseModel(ABC):
    name: str

    @abstractmethod
    def generate(self, record: EvalRecord, prompt: str) -> str:
        raise NotImplementedError
