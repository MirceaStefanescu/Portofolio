from typing import Optional

from pydantic import BaseModel, Field


class ProductCreate(BaseModel):
    name: str = Field(..., min_length=1)
    description: Optional[str] = None
    price_cents: int = Field(..., gt=0)
    currency: str = Field(..., min_length=3, max_length=3)


class ProductUpdate(BaseModel):
    name: Optional[str] = None
    description: Optional[str] = None
    price_cents: Optional[int] = Field(None, gt=0)
    currency: Optional[str] = Field(None, min_length=3, max_length=3)


class Product(BaseModel):
    id: str
    name: str
    description: Optional[str]
    price_cents: int
    currency: str
    created_at: str
    updated_at: str
