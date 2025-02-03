package com.sanfernando.sanfernando.dtos.responses.reporte;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReporteAlmacenStockResponse {
  private Integer idElementoCatalogo;
  private String nombre;
  private Double pesoUnitario;
  private String unidad;
  private String tipoElemento;
  private String tipoProduccion;
  private Double cantidad;
}
