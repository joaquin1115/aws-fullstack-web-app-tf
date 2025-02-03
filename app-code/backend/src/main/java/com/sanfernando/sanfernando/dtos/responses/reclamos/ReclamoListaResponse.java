package com.sanfernando.sanfernando.dtos.responses.reclamos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReclamoListaResponse {
    private String codReclamo;
    private String estadoReclamo;
    private String fechaReclamo;
    private String clienteRuc;
    private String tipoCliente;
    private String tipoReclamo;
    private String areaRes;

}
