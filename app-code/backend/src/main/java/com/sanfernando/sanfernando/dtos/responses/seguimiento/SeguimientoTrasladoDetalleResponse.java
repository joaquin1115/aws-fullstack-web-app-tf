package com.sanfernando.sanfernando.dtos.responses.seguimiento;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeguimientoTrasladoDetalleResponse {
  private String nombreCompletoTransportista;
  private String placa;
  private String origen;
  private String horaSalida;
  private String destino;
}
