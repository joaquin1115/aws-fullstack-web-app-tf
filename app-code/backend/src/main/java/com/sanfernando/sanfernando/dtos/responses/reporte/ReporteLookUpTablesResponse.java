package com.sanfernando.sanfernando.dtos.responses.reporte;

import java.util.List;

import com.sanfernando.sanfernando.dtos.responses.ReporteFrecuenciaResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReporteLookUpTablesResponse {
  private List<ReporteTipoResponse> reporteTipoResponse;
  private List<ReporteFrecuenciaResponse> reporteFrecuenciaResponse;
  private List<ReporteFormatoResponse> reporteFormatoResponse;
} 
