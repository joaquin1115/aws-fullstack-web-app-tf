package com.sanfernando.sanfernando.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReporteFrecuenciaResponse {
  private Integer idReporteFrecuencia;
  private String descripcion;
}
