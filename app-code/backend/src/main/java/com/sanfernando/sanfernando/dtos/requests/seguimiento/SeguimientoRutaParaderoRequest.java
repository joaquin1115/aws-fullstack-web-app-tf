package com.sanfernando.sanfernando.dtos.requests.seguimiento;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeguimientoRutaParaderoRequest {
  private int idLocal;
  private int idParaderoTipo;
  private int orden;
}