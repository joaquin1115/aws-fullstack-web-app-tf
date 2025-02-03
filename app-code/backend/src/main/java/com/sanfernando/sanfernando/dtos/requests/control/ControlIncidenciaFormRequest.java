package com.sanfernando.sanfernando.dtos.requests.control;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ControlIncidenciaFormRequest {
  private String tipoIncidencia;
  private String descripcion;
  private String tipoProcedimiento;
  private String tipoNorma;
  private LocalDate fecha;
  private LocalTime Hora;
  private Integer codTraslado;
  private Integer tiempoEstimadoProcedimiento;
}
