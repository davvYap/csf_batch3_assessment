export interface orderResponse {
  orderId: string;
  date: number;
  name: string;
  email: string;
  total: number;
}

export interface order {
  name: string;
  email: string;
  size: number;
  base: string;
  sauce: string;
  toppings: string[];
  comments: string;
}

export interface emailOrders {
  orderId: string;
  total: number;
  date: number;
}
