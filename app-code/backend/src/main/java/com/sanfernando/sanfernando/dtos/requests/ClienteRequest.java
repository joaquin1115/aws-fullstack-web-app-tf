package com.sanfernando.sanfernando.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClienteRequest {
  private Integer idClienteTipo;
  private String nombre;
  private String ruc;
  private String razonSocial;
  private String fechaRegistro;
}
