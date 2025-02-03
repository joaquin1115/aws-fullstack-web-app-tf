package com.sanfernando.sanfernando.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Ruta {
    private int codRuta;
    private String tipoRuta;
    private float distanciaTotal;
    private float duracion;
}
