package com.sanfernando.sanfernando.dtos.responses.reporte;

import java.sql.Time;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReporteOperacionResponse {
  private Integer idOperacionTipo;
  private String nombreOperacion;
  private Time tiempoTotal;
  private Integer cantidad;
  private Double tiempoMedio;
  private String listaOperacion;
}
