package com.sanfernando.sanfernando.dtos.responses.almacen;

import com.sanfernando.sanfernando.models.Mercancia;
import com.sanfernando.sanfernando.models.Proceso;
import com.sanfernando.sanfernando.models.Traslado;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BusquedaAlmacenResponse {
    private Proceso proceso;
    private List<Mercancia> mercancias;
    private Traslado traslado;
}
