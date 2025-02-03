package com.sanfernando.sanfernando.dtos.responses.reclamos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReclamoVisorNaturalezaResponse {
  private String descripcion;
  private String comentario;
  private String fechaSuceso;
  private String fechaReclamo;
  private String descripcionNivelUrgencia;
}
