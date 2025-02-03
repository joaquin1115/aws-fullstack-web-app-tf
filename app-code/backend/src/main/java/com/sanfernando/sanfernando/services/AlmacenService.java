package com.sanfernando.sanfernando.services;


import java.util.List;

import com.sanfernando.sanfernando.dtos.requests.almacen.MercanciasRequestDTO;
import com.sanfernando.sanfernando.dtos.requests.almacen.OperacionDTO;
import com.sanfernando.sanfernando.dtos.requests.almacen.StockDTO;
import com.sanfernando.sanfernando.dtos.requests.almacen.TrasladoDTO;
import com.sanfernando.sanfernando.dtos.responses.almacen.BusquedaAlmacenResponse;

public interface AlmacenService {
    BusquedaAlmacenResponse buscarPorNroPrecinto(String nroPrecinto);
    BusquedaAlmacenResponse buscarPorCodGuiaRemision(String codGuiaRemision);
    int registrarOperacion(OperacionDTO operacionDTO);
    List<String> registrarMercancias(MercanciasRequestDTO mercanciasRequestDTO);
    String registrarTraslado(TrasladoDTO trasladoDTO);
    StockDTO obtenerStock(String codStock);
    boolean validarNroPrecinto(String nroPrecinto);
    boolean validarCodGuiaRemision(String codGuiaRemision);
    boolean validarRuta(int codRuta);
    boolean validarVehiculo(String placa);
    boolean validarTransportista(String dni);
    boolean validarPedido(int codPedido);
    boolean validarEmpleado(String dni);
}
