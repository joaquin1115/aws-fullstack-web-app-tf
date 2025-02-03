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
public class SeguimientoTransportistaCrearRequest {
  private int idEmpleado;
  private String idEstadoTransportista;
  private String idTipoLicencia;
  private String numLicencia;
  private Date fechaVencimientoLicencia;
}
