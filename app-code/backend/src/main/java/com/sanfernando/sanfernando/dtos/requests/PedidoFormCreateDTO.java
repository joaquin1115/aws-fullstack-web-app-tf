package com.sanfernando.sanfernando.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoFormCreateDTO {
  private String nombre;
  private String apellidoPaterno;
  private String apellidoMaterno;
  private String dni;
  private String emailEmpresarial;
  private String emailPersonal;
  private String fechaSolicitud;
  private String fechaEntrega;
}
