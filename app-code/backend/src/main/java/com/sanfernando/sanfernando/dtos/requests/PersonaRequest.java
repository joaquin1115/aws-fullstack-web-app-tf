package com.sanfernando.sanfernando.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonaRequest {
  private Integer idEstadoCivil;
  private Integer idNacionalidad;
  private Integer idGenero;
  private String dni;
  private String primerApellido;
  private String segundoApellido;
  private String prenombre;
  private String direccion;
}
