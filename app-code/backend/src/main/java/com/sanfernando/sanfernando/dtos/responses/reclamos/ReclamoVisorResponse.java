package com.sanfernando.sanfernando.dtos.responses.reclamos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReclamoVisorResponse {
  private ReclamoVisorClienteResponse cliente;
  private ReclamoVisorDetalleResponse detalle;
  private ReclamoVisorNaturalezaResponse naturaleza;
  private List<ReclamoVisorEvidenciaResponse> evidencias;
  private ReclamoVisorResolucionResponse resolucion;
  private ReclamoVisorSeguimientoResponse seguimiento;
}
