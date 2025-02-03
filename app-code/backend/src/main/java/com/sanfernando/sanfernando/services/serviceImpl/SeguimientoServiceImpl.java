package com.sanfernando.sanfernando.services.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sanfernando.sanfernando.dao.SeguimientoDao;
import com.sanfernando.sanfernando.dtos.requests.seguimiento.SeguimientoRutaCrearRequest;
import com.sanfernando.sanfernando.dtos.requests.seguimiento.SeguimientoTransportistaCrearRequest;
import com.sanfernando.sanfernando.dtos.requests.seguimiento.SeguimientoTransportistaDetalleActualizarRequest;
import com.sanfernando.sanfernando.dtos.requests.seguimiento.SeguimientoVehiculoCrearRequest;
import com.sanfernando.sanfernando.dtos.requests.seguimiento.SeguimientoVehiculoDetalleActualizarRequest;
import com.sanfernando.sanfernando.dtos.responses.seguimiento.SeguimientoRutaDetalleResponse;
import com.sanfernando.sanfernando.dtos.responses.seguimiento.SeguimientoRutaListaResponse;
import com.sanfernando.sanfernando.dtos.responses.seguimiento.SeguimientoTransporstistaListaResponse;
import com.sanfernando.sanfernando.dtos.responses.seguimiento.SeguimientoTransportistaDetalleResponse;
import com.sanfernando.sanfernando.dtos.responses.seguimiento.SeguimientoTrasladoDetalleResponse;
import com.sanfernando.sanfernando.dtos.responses.seguimiento.SeguimientoTrasladoListaResponse;
import com.sanfernando.sanfernando.dtos.responses.seguimiento.SeguimientoTrasladoPedidoListaResponse;
import com.sanfernando.sanfernando.dtos.responses.seguimiento.SeguimientoVehiculoDetallesResponse;
import com.sanfernando.sanfernando.dtos.responses.seguimiento.SeguimientoVehiculoListaResponse;
import com.sanfernando.sanfernando.services.SeguimientoService;

@Service
public class SeguimientoServiceImpl implements SeguimientoService{
  
  @Autowired
  private SeguimientoDao seguimientoDao;

  @Override
  public List<SeguimientoTrasladoListaResponse> getTrasladosProceso() {
    return seguimientoDao.getTrasladosProceso();
  }

  @Override
  public SeguimientoTrasladoDetalleResponse getTrasladoProcesoDetalle(String codGuiaRemision) {
    return seguimientoDao.getTrasladoProcesoDetalle(codGuiaRemision);
  }

  @Override
  public List<SeguimientoTrasladoPedidoListaResponse> getTrasladoProcesoPedidos(String codGuiaRemision) {
    return seguimientoDao.getTrasladoProcesoPedidos(codGuiaRemision);
  }

  @Override
  public int actualizarTrasladoProcesoPedido(int idPedido) {
    return seguimientoDao.actualizarTrasladoProcesoPedido(idPedido);
  }

  @Override
  public List<SeguimientoVehiculoListaResponse> obtenerVehiculos() {
    return seguimientoDao.obtenerVehiculos();
  }

  @Override
  public SeguimientoVehiculoDetallesResponse obtenerVehiculoDetalle(int idVehiculo) {
    return seguimientoDao.obtenerVehiculoDetalle(idVehiculo);
  }

  @Override
  public int actualizarVehiculo(SeguimientoVehiculoDetalleActualizarRequest request) {
    return seguimientoDao.actualizarVehiculo(request);
  }

  @Override
  public int crearVehiculo(SeguimientoVehiculoCrearRequest request) {
    return seguimientoDao.crearVehiculo(request);
  }

  @Override
  public List<SeguimientoTransporstistaListaResponse> obtenerTransportistas() {
    return seguimientoDao.obtenerTransportistas();
  }

  @Override
  public SeguimientoTransportistaDetalleResponse obtenerTransportistaDetalle(int idTransportista) {
    return seguimientoDao.obtenerTransportistaDetalle(idTransportista);
  }

  @Override
  public int actualizarTransportista(SeguimientoTransportistaDetalleActualizarRequest request) {
    return seguimientoDao.actualizarTransportista(request);
  }

  @Override
  public int crearTransportista(SeguimientoTransportistaCrearRequest request) {
    return seguimientoDao.crearTransportista(request);
  }

  @Override
  public List<SeguimientoRutaListaResponse> obtenerRutas() {
    return seguimientoDao.obtenerRutas();
  }

  @Override
  public List<SeguimientoRutaDetalleResponse> obtenerRutaDetalle(int idRuta) {
    return seguimientoDao.obtenerRutaDetalle(idRuta);
  }

  @Override
  public int borrarRuta(int idRuta) {
    return seguimientoDao.borrarRuta(idRuta);
  }

  @Override
  public int crearRuta(SeguimientoRutaCrearRequest request) {
    return seguimientoDao.crearRuta(request);
  }
}
