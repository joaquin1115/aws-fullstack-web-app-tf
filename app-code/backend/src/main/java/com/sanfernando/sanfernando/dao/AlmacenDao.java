package com.sanfernando.sanfernando.dao;

import org.springframework.stereotype.Repository;

import com.sanfernando.sanfernando.dtos.requests.almacen.MercanciasRequestDTO;
import com.sanfernando.sanfernando.dtos.requests.almacen.OperacionDTO;
import com.sanfernando.sanfernando.dtos.requests.almacen.StockDTO;
import com.sanfernando.sanfernando.dtos.requests.almacen.TrasladoDTO;
import com.sanfernando.sanfernando.dtos.responses.almacen.BusquedaAlmacenResponse;

import java.util.List;

@Repository
public interface AlmacenDao {
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
