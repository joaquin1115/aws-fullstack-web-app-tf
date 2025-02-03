package com.sanfernando.sanfernando.dtos.responses.seguimiento;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeguimientoTrasladoPedidoListaResponse {
  private String idPedido;
  private String tipoPedido;
  private String fechaSalida;
  private String fechaLLegada;
  private String destino;
  private String idEstadoPedido;
}
