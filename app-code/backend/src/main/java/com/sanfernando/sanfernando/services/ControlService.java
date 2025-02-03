package com.sanfernando.sanfernando.services;

import java.util.List;

import com.sanfernando.sanfernando.dtos.requests.control.ControlIncidenciaFormRequest;
import com.sanfernando.sanfernando.dtos.responses.control.ConductoresListaResponse;
import com.sanfernando.sanfernando.dtos.responses.control.IncidenciaListaResponse;
import com.sanfernando.sanfernando.dtos.responses.control.VehiculoListaResponse;

public interface ControlService {
  List<ConductoresListaResponse> obtenerConductoresLista();
  boolean actualizarEstadoConductor(String codigoConductor, String nuevoEstado);
  List<IncidenciaListaResponse> obtenerIncidenciasLista();
  void crearIncidencia(ControlIncidenciaFormRequest incidenciaForm);
  List<VehiculoListaResponse> obtenerVehiculosLista();
  boolean actualizarEstado(String codigoVehiculo, String nuevoEstado);
  int actualizarEstadoIncidencia(int idIncidencia, String idEstadoIncidencia);
}
