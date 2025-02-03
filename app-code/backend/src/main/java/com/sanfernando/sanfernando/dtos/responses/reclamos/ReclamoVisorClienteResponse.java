package com.sanfernando.sanfernando.dtos.responses.reclamos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReclamoVisorClienteResponse {
  private String nombre;
  private String representante;
  private String cargo;
  private String correoEmpresarial;
  private String direccion;
}
