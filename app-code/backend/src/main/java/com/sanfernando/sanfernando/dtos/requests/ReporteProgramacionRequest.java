package com.sanfernando.sanfernando.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReporteProgramacionRequest {
  private Integer idRepresentante;
  private Integer idReporteFormato;
  private Integer idReporteEstado;
  private Integer idReporteTipo;
  private Integer idReporteFrecuencia;
  private String fechaInicio;
  private String fechaFin;
}
