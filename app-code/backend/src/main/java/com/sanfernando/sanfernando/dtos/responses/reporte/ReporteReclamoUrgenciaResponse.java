package com.sanfernando.sanfernando.dtos.responses.reporte;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReporteReclamoUrgenciaResponse {
  private String idTipoReclamo;
  private String descripcion;
  private Integer totalTipoReclamo;
  private Integer totalUrgenciaBaja;
  private Integer totalUrgenciaMedia;
  private Integer totalUrgenciaAlta;
}
