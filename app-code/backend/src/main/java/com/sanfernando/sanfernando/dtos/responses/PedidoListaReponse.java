package com.sanfernando.sanfernando.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PedidoListaReponse {
  private Integer idPedido;
  private String nombre;
  private String empleado;
  private String fecha;
  private String estado;
}
