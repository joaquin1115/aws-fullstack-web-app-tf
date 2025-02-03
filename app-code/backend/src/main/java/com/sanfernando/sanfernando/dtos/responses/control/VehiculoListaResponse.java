package com.sanfernando.sanfernando.dtos.responses.control;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VehiculoListaResponse {
    private String codigoDelVehiculo;
    private String anioDeFabricacion;
    private String fechaDeUltimoMantenimiento;
    private String capacidadDeCarga;
    private String modelo;
    private String placa;
    private String fechaUltimoViaje;
    private String estadoDelVehiculo;
}
