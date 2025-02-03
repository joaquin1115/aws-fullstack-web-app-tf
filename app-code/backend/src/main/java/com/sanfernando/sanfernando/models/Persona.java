package com.sanfernando.sanfernando.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Persona {
  private Integer cod_persona;
  private Integer cod_estado_civil;
  private Integer cod_nacionalidad;
  private Integer cod_genero;
  private String dni;
  private String primer_apellido;
  private String segundo_apellido;
  private String prenombre;
  private String direccion;
}