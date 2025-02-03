package com.sanfernando.sanfernando.dtos.responses.seguimiento;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeguimientoTrasladoProgramadoListaResponse {
  private String codGuiaRemision;
  private String origen;
  private String destino;
  private String horaInicio;
}
