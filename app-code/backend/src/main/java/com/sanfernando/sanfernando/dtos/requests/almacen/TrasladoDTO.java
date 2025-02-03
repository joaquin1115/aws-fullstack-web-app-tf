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
public class TrasladoDTO {
    private String placaVehiculo;
    private int codRuta;
    private String dniTransportista;
    private int idOperacionInicia;
    private List<Integer> codPedidos;
}
