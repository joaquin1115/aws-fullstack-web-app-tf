package com.sanfernando.sanfernando.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PedidoProductoResponse {
  private Integer idElementoCatalogo;
  private String nombre;
  private String descripcion;
  private String tipo;
  private Double precioUnitario;
  private Integer idElementoCatalogoTipo;
  private Double peso;
  private String unidad;
}
