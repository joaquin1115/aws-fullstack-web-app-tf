package com.sanfernando.sanfernando.services.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sanfernando.sanfernando.dao.ControlDao;
import com.sanfernando.sanfernando.dtos.requests.control.ControlIncidenciaFormRequest;
import com.sanfernando.sanfernando.dtos.responses.control.ConductoresListaResponse;
import com.sanfernando.sanfernando.dtos.responses.control.IncidenciaListaResponse;
import com.sanfernando.sanfernando.dtos.responses.control.VehiculoListaResponse;
import com.sanfernando.sanfernando.services.ControlService;

@Service
public class ControlServiceImpl implements ControlService {

  @Autowired
  private ControlDao controlDao;

  @Override
  public List<ConductoresListaResponse> obtenerConductoresLista() {
    return controlDao.obtenerConductoresLista();
  }

  @Override
  public boolean actualizarEstadoConductor(String codigoConductor, String nuevoEstado) {
    return controlDao.actualizarEstadoConductor(codigoConductor, nuevoEstado);
  }

  @Override
  public List<IncidenciaListaResponse> obtenerIncidenciasLista() {
    return controlDao.obtenerIncidenciasLista();

  }

  @Override
  public void crearIncidencia(ControlIncidenciaFormRequest incidenciaForm) {
    controlDao.crearIncidencia(incidenciaForm);
  }

  @Override
  public List<VehiculoListaResponse> obtenerVehiculosLista() {
    return controlDao.obtenerVehiculosLista();
  }

  @Override
  public boolean actualizarEstado(String codigoVehiculo, String nuevoEstado) {
    return controlDao.actualizarEstado(codigoVehiculo, nuevoEstado);
  }

  @Override
  public int actualizarEstadoIncidencia(int idIncidencia, String idEstadoIncidencia) {
    return controlDao.actualizarEstadoIncidencia(idIncidencia, idEstadoIncidencia);
  } 
  
}
