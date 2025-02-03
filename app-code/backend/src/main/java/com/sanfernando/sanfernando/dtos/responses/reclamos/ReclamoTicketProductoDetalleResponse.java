package com.sanfernando.sanfernando.dtos.responses.reclamos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReclamoTicketProductoDetalleResponse {
  private String fecha;
  private int cantidad;
  private String nombre;
  private String nroLote;
}
