package com.sanfernando.sanfernando.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Vehiculo {
    private String placaVehiculo;
    private String modeloVehiculo;
    private int anioFabricacion;
    private float capacidadCarga;
    private Date fechaUltimoMantenimiento;
    private Date fechaUltimoViaje;
}
