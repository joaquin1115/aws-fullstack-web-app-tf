package com.sanfernando.sanfernando.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Proceso {
    private String estado;
    private List<Operacion> operaciones;
}
