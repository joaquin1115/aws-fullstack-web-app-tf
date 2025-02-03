import { TicketProductoRequest } from "./ticket-producto-request";

export interface PedidoRequest {
  idRepresentante?: number;
  idEmpleadoRegistro?: number;
  idTipoPedido?: string;
  idEstadoPedido?: string;
  fechaRegistro?: string;
  fechaEntrega?: string;
  pedidoTicketProductoRequest?: TicketProductoRequest[];
}
