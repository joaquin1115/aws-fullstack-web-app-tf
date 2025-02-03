package com.sanfernando.sanfernando.dao;

import java.util.List;

import com.sanfernando.sanfernando.dtos.requests.control.ControlIncidenciaFormRequest;
import com.sanfernando.sanfernando.dtos.responses.control.ConductoresListaResponse;
import com.sanfernando.sanfernando.dtos.responses.control.IncidenciaListaResponse;
import com.sanfernando.sanfernando.dtos.responses.control.VehiculoListaResponse;

public interface ControlDao {
  List<ConductoresListaResponse> obtenerConductoresLista();
  boolean actualizarEstadoConductor(String codigoConductor, String nuevoEstado);
  List<IncidenciaListaResponse> obtenerIncidenciasLista();
  void crearIncidencia(ControlIncidenciaFormRequest incidenciaForm);
  void crearProcedimiento(int codIncidencia, String codTipoProcedimiento, int tiempoEstimado);
  void crearNorma(int codIncidencia, String codNormaTipo);
  List<VehiculoListaResponse> obtenerVehiculosLista();
  boolean actualizarEstado(String codigoVehiculo, String nuevoEstado);
  int actualizarEstadoIncidencia(int idIncidencia, String idEstadoIncidencia);
}
