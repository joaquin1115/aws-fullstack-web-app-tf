package com.sanfernando.sanfernando.dtos.responses.reporte;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReporteReclamoTiempoResponse {
  private String idNivelUrgencia;
  private String descripcion;
  private Double tiempoMedio;
  private Integer totalNivelUrgencia;
}
