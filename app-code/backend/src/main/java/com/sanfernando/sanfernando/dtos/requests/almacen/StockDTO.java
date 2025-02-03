package com.sanfernando.sanfernando.dtos.requests.almacen;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockDTO {
    private int id;
    private String nombre;
    private String categoria;
    private String tipo;
    private String segmento;
    private String unidad;
}
