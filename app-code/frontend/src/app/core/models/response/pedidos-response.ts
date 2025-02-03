export class Order {
  id?: number;
  requestDate?: string;
  deliveryDate?: string;
  department?: string;
  representative?: string;
  employee?: string;
  orderType?: string;
  ticketCode?: string;
  status?: string;
  items: OrderItem[] = [];
}

export class OrderItem {
  productCode?: string;
  name?: string;
  quantity?: string;
  unit?: string;
}
