package com.sanfernando.sanfernando.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Representante {
  private Integer idRepresentante;
  private Integer idCliente;
  private Integer idPersona;
  private String telefono;
  private String correoEmpresarial;
  private String cargo;
}
