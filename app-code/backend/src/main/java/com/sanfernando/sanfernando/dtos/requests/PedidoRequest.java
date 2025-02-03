package com.sanfernando.sanfernando.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PedidoRequest {
  private Integer idRepresentante;
  private Integer idEmpleadoRegistro;
  private String idTipoPedido;
  private String idEstadoPedido;
  private Integer idTicket;
  private String fechaRegistro;
  private String fechaEntrega;
  private PedidoTicketProductoRequest[] pedidoTicketProductoRequest;
}
