package com.sanfernando.sanfernando.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sanfernando.sanfernando.dtos.requests.control.ControlIncidenciaFormRequest;
import com.sanfernando.sanfernando.dtos.responses.control.ConductoresListaResponse;
import com.sanfernando.sanfernando.dtos.responses.control.IncidenciaListaResponse;
import com.sanfernando.sanfernando.dtos.responses.control.VehiculoListaResponse;
import com.sanfernando.sanfernando.dtos.responses.reporte.ReporteLookUpTablesResponse;
import com.sanfernando.sanfernando.services.ControlService;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin(origins = {"*"})
@RequestMapping("/api/control")
@RequiredArgsConstructor
public class ControlController {
  
  @Autowired
  private ControlService controlService;

  @GetMapping("/listaVehiculos")
  public List<VehiculoListaResponse> obtenerVehiculosLista() {
    return controlService.obtenerVehiculosLista();
  }

  @PutMapping("/actualizarEstadoVehiculo/{codigoVehiculo}")
  public ResponseEntity<?> actualizarEstadoVehiculo(@PathVariable String codigoVehiculo, @RequestBody String nuevoEstado) {
    try {
      // Validar que codigoConductor sea un número válido
      Integer.parseInt(codigoVehiculo);

      // Eliminar comillas si están presentes y convertir a minúsculas
      nuevoEstado = nuevoEstado.replaceAll("\"", "").toLowerCase().trim();

      // Validar que nuevoEstado sea una de las opciones válidas
      if (!nuevoEstado.equals("disponible") &&
              !nuevoEstado.equals("no disponible") &&
              !nuevoEstado.equals("cuarentena")) {
        return ResponseEntity.badRequest().body("Estado inválido. Debe ser 'Disponible', 'No disponible' o 'Cuarentena'.");
      }

      boolean actualizado = controlService.actualizarEstado(codigoVehiculo, nuevoEstado);
      if (actualizado) {
        return ResponseEntity.ok("Estado del conductor actualizado correctamente.");
      } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conductor no encontrado o no se pudo actualizar.");
      }
    } 
    catch (NumberFormatException e) {
      return ResponseEntity.badRequest().body("Código de conductor inválido. Debe ser un número entero.");
    }
  }

  @GetMapping("/listaIncidencia")
  public List<IncidenciaListaResponse> obtenerIncidenciasLista() {
      return controlService.obtenerIncidenciasLista();
  }

  @PostMapping("/crearIncidencia")
  public ResponseEntity<?> crearIncidencia(@RequestBody ControlIncidenciaFormRequest incidenciaForm) {
    try {
      controlService.crearIncidencia(incidenciaForm);
      return ResponseEntity.ok("Incidencia creada con éxito");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la incidencia: " + e.getMessage());
    }
  }

  @GetMapping("/actualizarEstadoIncidencia")
  public ResponseEntity<Object> actualizarEstadoIncidencia(@RequestParam int idIncidencia, @RequestParam String idEstadoIncidencia) {
    int response = controlService.actualizarEstadoIncidencia(idIncidencia,idEstadoIncidencia);
    if (response > 0) {
      return ResponseEntity.ok(true);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping(value = "/listaConductores")
  public List<ConductoresListaResponse> obtenerConductoresLista() {
    return controlService.obtenerConductoresLista();
  }

  @PutMapping(value = "/actualizar-estado/{codigoConductor}")
  public ResponseEntity<String> actualizarEstadoConductor( @PathVariable String codigoConductor, @RequestBody String nuevoEstado) {
    try {
      // Validar que codigoConductor sea un número válido
      Integer.parseInt(codigoConductor);

      // Eliminar comillas si están presentes y convertir a minúsculas
      nuevoEstado = nuevoEstado.replaceAll("\"", "").toLowerCase().trim();

      // Validar que nuevoEstado sea una de las opciones válidas
      if (!nuevoEstado.equals("disponible") &&
              !nuevoEstado.equals("no disponible") &&
              !nuevoEstado.equals("cuarentena")) {
          return ResponseEntity.badRequest().body("Estado inválido. Debe ser 'Disponible', 'No disponible' o 'Cuarentena'.");
      }

      boolean actualizado = controlService.actualizarEstadoConductor(codigoConductor, nuevoEstado);
      if (actualizado) {
          return ResponseEntity.ok("Estado del conductor actualizado correctamente.");
      } else {
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conductor no encontrado o no se pudo actualizar.");
      }
    } 
    catch (NumberFormatException e) {
      return ResponseEntity.badRequest().body("Código de conductor inválido. Debe ser un número entero.");
    }
  }
}
