package com.sanfernando.sanfernando.dtos.requests.seguimiento;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeguimientoRutaCrearRequest {
  private double distanciaTotal;
  private int idRutaTipo;
  private double duracion;
  private SeguimientoRutaParaderoRequest[] paraderos;
}
