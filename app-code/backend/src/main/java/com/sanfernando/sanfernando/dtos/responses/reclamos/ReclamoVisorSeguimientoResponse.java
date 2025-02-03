package com.sanfernando.sanfernando.dtos.responses.reclamos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReclamoVisorSeguimientoResponse {
  private String fechaResolucion;
  private String numeroCaso;
  private String descripcionEstadoReclamo;
}
