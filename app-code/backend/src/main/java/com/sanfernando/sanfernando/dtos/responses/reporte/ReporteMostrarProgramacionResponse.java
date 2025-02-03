package com.sanfernando.sanfernando.dtos.responses.reporte;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReporteMostrarProgramacionResponse {
  private Integer idProgramacionReporte;
  private String descripcionFormato;
  private String descripcionTipo;
  private String descripcionFrecuencia;
  private String fechaInicio;
  private String fechaFin;
}
