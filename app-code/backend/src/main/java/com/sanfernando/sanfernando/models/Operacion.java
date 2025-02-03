package com.sanfernando.sanfernando.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Operacion {
    private int idOperacion;
    private String fecha;
    private String horaInicio;
    private String horaFin;
    private String tipoOperacion;
    private String dniEmpEjecutor;
    private String dniEmpSupervisor;
}
