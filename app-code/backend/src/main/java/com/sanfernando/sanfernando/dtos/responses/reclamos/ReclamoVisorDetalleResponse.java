package com.sanfernando.sanfernando.dtos.responses.reclamos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReclamoVisorDetalleResponse {
  private int idTicket;
  private String nombre;
  private String fechaEntrega;
  private String nroLote;
  private int cantidad;
}
