package com.sanfernando.sanfernando.dtos.responses.reclamos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReclamoRepresentanteListaResponse {
    private int codRepresentante;
    private String nombreRepresentante;
    private String cargoRepresentante;
    private String direccion;
    private String correoEmpresarial;
}
