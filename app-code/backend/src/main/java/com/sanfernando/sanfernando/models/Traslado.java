package com.sanfernando.sanfernando.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Traslado {
    private String estado;
    private String codGuiaRemision;
    private Ruta ruta;
    private Transportista transportista;
    private Vehiculo vehiculo;
}
