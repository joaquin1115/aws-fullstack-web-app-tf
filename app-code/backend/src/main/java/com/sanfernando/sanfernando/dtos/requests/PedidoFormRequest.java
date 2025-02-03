package com.sanfernando.sanfernando.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PedidoFormRequest {
  private PersonaRequest personaRequest;
  private ClienteRequest clienteRequest;
  private RepresentanteRequest representanteRequest;
}
