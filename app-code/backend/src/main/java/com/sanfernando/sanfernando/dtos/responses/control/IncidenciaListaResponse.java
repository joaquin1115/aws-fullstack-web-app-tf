package com.sanfernando.sanfernando.dtos.responses.control;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IncidenciaListaResponse {
    private String codigoDeIncidencia;
    private String codigoDeTraslado;
    private String descripcionTipoDeIncidencia;
    private String fechaDeOcurrencia;
    private String horaDeOcurrencia;
    private String estadoDeIncidencia;
}
