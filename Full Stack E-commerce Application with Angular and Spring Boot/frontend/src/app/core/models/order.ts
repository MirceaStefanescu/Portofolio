export interface OrderItemRequest {
  productId: number;
  quantity: number;
}

export interface CreateOrderRequest {
  items: OrderItemRequest[];
  currency: string;
}

export interface OrderItemResponse {
  productId: number;
  name: string;
  quantity: number;
  unitPrice: number;
}

export interface OrderResponse {
  id: number;
  status: string;
  totalAmount: number;
  currency: string;
  paymentIntentId: string;
  clientSecret?: string | null;
  createdAt: string;
  items: OrderItemResponse[];
}
