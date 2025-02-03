package com.sanfernando.sanfernando.dtos.responses.seguimiento;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeguimientoRutaListaResponse {
  private int idRuta;
  private String tipoRuta;
  private double distanciaTotal;
  private String origen;
  private String destino;
}
