package com.sanfernando.sanfernando.dtos.requests.reportes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReporteRequest {
  private int idRepresentante;
  private int idReporteFormato;
  private int idReporteTipo;
  private String fechaInicio;
  private String fechaFin;
}
