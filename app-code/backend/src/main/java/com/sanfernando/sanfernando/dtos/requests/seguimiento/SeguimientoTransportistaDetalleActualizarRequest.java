package com.sanfernando.sanfernando.dtos.requests.seguimiento;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeguimientoTransportistaDetalleActualizarRequest {
  private int idTransportista;
  private String numLicencia;
  private String codTipoLicencia;
  private Date fechaVencimientoLicencia;
  private String codEstadoTransportista;
}
