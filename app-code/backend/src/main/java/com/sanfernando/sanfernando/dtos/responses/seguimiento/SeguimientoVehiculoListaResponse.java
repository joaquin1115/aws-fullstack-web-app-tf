package com.sanfernando.sanfernando.dtos.responses.seguimiento;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeguimientoVehiculoListaResponse {
  private int idVehiculo;
  private String placa;
  private String modelo;  
  private int anioFabricacion;
  private int capacidadCarga;
  private String fechaUltimoViaje;
  private String fechaUltimoMantenimiento;
  private String estado;
}
