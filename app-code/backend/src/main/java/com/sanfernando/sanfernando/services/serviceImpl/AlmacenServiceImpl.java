package com.sanfernando.sanfernando.services.serviceImpl;

import com.sanfernando.sanfernando.dtos.requests.almacen.MercanciasRequestDTO;
import com.sanfernando.sanfernando.dtos.requests.almacen.OperacionDTO;
import com.sanfernando.sanfernando.dtos.requests.almacen.StockDTO;
import com.sanfernando.sanfernando.dtos.requests.almacen.TrasladoDTO;
import com.sanfernando.sanfernando.dtos.responses.almacen.BusquedaAlmacenResponse;
import com.sanfernando.sanfernando.dao.AlmacenDao;
import com.sanfernando.sanfernando.services.AlmacenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlmacenServiceImpl implements AlmacenService {

    @Autowired
    private AlmacenDao almacenDao;

    @Override
    public BusquedaAlmacenResponse buscarPorNroPrecinto(String nroPrecinto) {
        return almacenDao.buscarPorNroPrecinto(nroPrecinto);
    }

    @Override
    public BusquedaAlmacenResponse buscarPorCodGuiaRemision(String codGuiaRemision) {
        return almacenDao.buscarPorCodGuiaRemision(codGuiaRemision);
    }

    @Override
    public int registrarOperacion(OperacionDTO operacionDTO) {
        return almacenDao.registrarOperacion(operacionDTO);
    }

    @Override
    public List<String> registrarMercancias(MercanciasRequestDTO mercanciasRequestDTO) {
        return almacenDao.registrarMercancias(mercanciasRequestDTO);
    }

    @Override
    public String registrarTraslado(TrasladoDTO trasladoDTO) {
        return almacenDao.registrarTraslado(trasladoDTO);
    }

    @Override
    public StockDTO obtenerStock(String codStock){
        return almacenDao.obtenerStock(codStock);
    }

    @Override
    public boolean validarNroPrecinto(String nroPrecinto) {
        return almacenDao.validarNroPrecinto(nroPrecinto);
    }

    @Override
    public boolean validarCodGuiaRemision(String codGuiaRemision) {
        return almacenDao.validarCodGuiaRemision(codGuiaRemision);
    }

    @Override
    public boolean validarRuta(int codRuta) {
        return almacenDao.validarRuta(codRuta);
    }

    @Override
    public boolean validarVehiculo(String placa) {
        return almacenDao.validarVehiculo(placa);
    }

    @Override
    public boolean validarTransportista(String dni) {
        return almacenDao.validarTransportista(dni);
    }

    @Override
    public boolean validarPedido(int codPedido) {
        return almacenDao.validarPedido(codPedido);
    }

    @Override
    public boolean validarEmpleado(String dni) {
        return almacenDao.validarEmpleado(dni);
    }
}
