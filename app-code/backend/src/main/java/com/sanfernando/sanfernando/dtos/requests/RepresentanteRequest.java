package com.sanfernando.sanfernando.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RepresentanteRequest {
  private Integer idCliente;
  private Integer idPersona;
  private String telefono;
  private String correoEmpresarial;
  private String cargo;
}
