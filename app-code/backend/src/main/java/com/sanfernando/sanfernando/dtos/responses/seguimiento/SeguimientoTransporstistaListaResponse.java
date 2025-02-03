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
public class SeguimientoTransporstistaListaResponse {
  private int idTransportista;
  private String nombreCompleto;
  private String licencia;
  private String tipoLicencia;
  private Date fechaVencimientoLicencia;
  private String estado;
}
