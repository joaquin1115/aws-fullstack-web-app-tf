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
public class SeguimientoVehiculoDetallesResponse {
  private String marca;
  private String modelo;
  private String estado;
  private int anioFabricacion;
  private String placa;
  private String tipo;
  private Double capacidadCarga;
  private Date fechaUltimoMantenimiento;
}
