package com.sanfernando.sanfernando.dtos.responses.reporte;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReporteReclamoMesResponse {
  private String mes;
  private Integer totalReclamos;
}
