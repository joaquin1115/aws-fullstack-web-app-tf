package com.sanfernando.sanfernando.dtos.responses.seguimiento;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeguimientoTransportistaDetalleResponse {
  private String nombreCompleto;
  private String dni;
  private String licencia;
  private String tipoLicencia;
  private Date fechaVencimientoLicencia;
  private String estado;
}
