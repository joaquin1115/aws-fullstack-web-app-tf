package com.sanfernando.sanfernando.dtos.responses.control;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConductoresListaResponse {
    private String codigoDelConductor;
    private String codigoDelEmpleado;
    private String tipoDeLicencia;
    private String fechaDeVencimientoDeLicencia;
    private String fechaUltimoTraslado;
    private String estadoDelConductor;
}
