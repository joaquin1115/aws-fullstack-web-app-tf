package com.sanfernando.sanfernando.dtos.responses.reporte;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportePedidoTopResponse {
  private Integer idElementoCatalogo;
  private String nombre;
  private String descripcion;
  private Integer cantidad;
}
