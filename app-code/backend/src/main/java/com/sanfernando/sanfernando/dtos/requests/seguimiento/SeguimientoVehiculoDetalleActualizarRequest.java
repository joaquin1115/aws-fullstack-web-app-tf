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
public class SeguimientoVehiculoDetalleActualizarRequest {
  private int idVehiculo;
  private int idVehiculoMarca;
  private String idVehiculoModelo;
  private String idVehiculoEstado;
  private Integer anioFabricacion;
  private String placa;
  private String codVehiculoTipo;
  private Double capacidadCarga;
  private Date fechaUltimoMantenimiento;
}
