package com.sanfernando.sanfernando.dao;

import java.util.List;

import com.sanfernando.sanfernando.dtos.requests.seguimiento.SeguimientoRutaCrearRequest;
import com.sanfernando.sanfernando.dtos.requests.seguimiento.SeguimientoTransportistaCrearRequest;
import com.sanfernando.sanfernando.dtos.requests.seguimiento.SeguimientoTransportistaDetalleActualizarRequest;
import com.sanfernando.sanfernando.dtos.requests.seguimiento.SeguimientoVehiculoCrearRequest;
import com.sanfernando.sanfernando.dtos.requests.seguimiento.SeguimientoVehiculoDetalleActualizarRequest;
import com.sanfernando.sanfernando.dtos.responses.seguimiento.SeguimientoRutaListaResponse;
import com.sanfernando.sanfernando.dtos.responses.seguimiento.SeguimientoRutaDetalleResponse;
import com.sanfernando.sanfernando.dtos.responses.seguimiento.SeguimientoTransporstistaListaResponse;
import com.sanfernando.sanfernando.dtos.responses.seguimiento.SeguimientoTransportistaDetalleResponse;
import com.sanfernando.sanfernando.dtos.responses.seguimiento.SeguimientoTrasladoDetalleResponse;
import com.sanfernando.sanfernando.dtos.responses.seguimiento.SeguimientoTrasladoListaResponse;
import com.sanfernando.sanfernando.dtos.responses.seguimiento.SeguimientoTrasladoPedidoListaResponse;
import com.sanfernando.sanfernando.dtos.responses.seguimiento.SeguimientoVehiculoDetallesResponse;
import com.sanfernando.sanfernando.dtos.responses.seguimiento.SeguimientoVehiculoListaResponse;

public interface SeguimientoDao {
  public List<SeguimientoTrasladoListaResponse> getTrasladosProceso();
  public SeguimientoTrasladoDetalleResponse getTrasladoProcesoDetalle(String codGuiaRemision);
  public List<SeguimientoTrasladoPedidoListaResponse> getTrasladoProcesoPedidos(String codGuiaRemision);
  public int actualizarTrasladoProcesoPedido(int idPedido);
  public List<SeguimientoVehiculoListaResponse> obtenerVehiculos();
  public SeguimientoVehiculoDetallesResponse obtenerVehiculoDetalle(int idVehiculo);
  public int actualizarVehiculo(SeguimientoVehiculoDetalleActualizarRequest request);
  public int crearVehiculo(SeguimientoVehiculoCrearRequest request);
  public List<SeguimientoTransporstistaListaResponse> obtenerTransportistas();
  public SeguimientoTransportistaDetalleResponse obtenerTransportistaDetalle(int idTransportista);
  public int actualizarTransportista(SeguimientoTransportistaDetalleActualizarRequest request);
  public int crearTransportista(SeguimientoTransportistaCrearRequest request);
  public List<SeguimientoRutaListaResponse> obtenerRutas();
  public List<SeguimientoRutaDetalleResponse> obtenerRutaDetalle(int idRuta);
  public int borrarRuta(int idRuta);
  public int crearRuta(SeguimientoRutaCrearRequest request);
}
