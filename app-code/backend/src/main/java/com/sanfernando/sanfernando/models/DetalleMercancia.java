package com.sanfernando.sanfernando.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetalleMercancia {
    private String codStock;
    private String nombreStock;
    private String categoria;
    private String tipo;
    private String segmento;
    private String unidad;
    private int cantidadTransportar;
}
