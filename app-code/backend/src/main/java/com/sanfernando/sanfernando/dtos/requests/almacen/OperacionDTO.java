package com.sanfernando.sanfernando.dtos.requests.almacen;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperacionDTO {
    private int idPicking;
    private String dniEjecutor;
    private String dniSupervisor;
    private int codTipoOperacion;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
}
