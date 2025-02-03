package com.sanfernando.sanfernando.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
  private String dni;
  private String area;
  private String cargo;
  private Boolean representante;
  private Integer idEmpleado;
}
