package com.sanfernando.sanfernando.dtos.requests.almacen;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MercanciasRequestDTO {
    private int idOperacionPicking;
    private List<MercanciaDTO> mercancias;
}
