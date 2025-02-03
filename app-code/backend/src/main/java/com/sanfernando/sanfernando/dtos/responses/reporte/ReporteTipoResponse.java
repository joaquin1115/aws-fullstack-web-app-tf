package com.sanfernando.sanfernando.dtos.responses.reporte;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReporteTipoResponse {
  private Integer idReporteTipo;
  private String descripcion;
}
