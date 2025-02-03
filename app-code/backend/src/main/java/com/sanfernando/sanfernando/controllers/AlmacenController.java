package com.sanfernando.sanfernando.controllers;

import com.sanfernando.sanfernando.dtos.requests.almacen.MercanciasRequestDTO;
import com.sanfernando.sanfernando.dtos.requests.almacen.OperacionDTO;
import com.sanfernando.sanfernando.dtos.requests.almacen.StockDTO;
import com.sanfernando.sanfernando.dtos.requests.almacen.TrasladoDTO;
import com.sanfernando.sanfernando.dtos.responses.almacen.BusquedaAlmacenResponse;
import com.sanfernando.sanfernando.services.AlmacenService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/almacen")
@RequiredArgsConstructor
public class AlmacenController {

    @Autowired
    private AlmacenService almacenService;

    @GetMapping(value = "/buscarPorNroPrecinto", produces = "application/json;charset=utf-8")
    public BusquedaAlmacenResponse buscarPorNroPrecinto(@RequestParam String nroPrecinto) {
        return almacenService.buscarPorNroPrecinto(nroPrecinto);
    }

    @GetMapping(value = "/buscarPorCodGuiaRemision", produces = "application/json;charset=utf-8")
    public BusquedaAlmacenResponse buscarPorCodGuiaRemision(@RequestParam String codGuiaRemision) {
        return almacenService.buscarPorCodGuiaRemision(codGuiaRemision);
    }

    @PostMapping("/registrarOperacion")
    public ResponseEntity<Integer> registrarOperacion(@RequestBody OperacionDTO operacionDTO) {
        int idOperacion = almacenService.registrarOperacion(operacionDTO);
        return ResponseEntity.ok(idOperacion);
    }

    @PostMapping("/registrarMercancias")
    public List<String> registrarMercancias(@RequestBody MercanciasRequestDTO mercanciasRequestDTO) {
        return almacenService.registrarMercancias(mercanciasRequestDTO);
    }

    @PostMapping(value ="/registrarTraslado")
    public String registrarTraslado(@RequestBody TrasladoDTO trasladoDTO) {
        return almacenService.registrarTraslado(trasladoDTO);
    }

    @GetMapping("/obtenerStock")
    public StockDTO obtenerStock(@RequestParam String codStock) {
        StockDTO stock = almacenService.obtenerStock(codStock);
        return stock;
    }

    @GetMapping("/validarNroPrecinto")
    public ResponseEntity<Boolean> validarNroPrecinto(@RequestParam String nroPrecinto) {
        boolean exists = almacenService.validarNroPrecinto(nroPrecinto);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/validarCodGuiaRemision")
    public ResponseEntity<Boolean> validarCodGuiaRemision(@RequestParam String codGuiaRemision) {
        boolean exists = almacenService.validarCodGuiaRemision(codGuiaRemision);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/validarRuta")
    public ResponseEntity<Boolean> validarRuta(@RequestParam int codRuta) {
        boolean exists = almacenService.validarRuta(codRuta);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/validarVehiculo")
    public ResponseEntity<Boolean> validarVehiculo(@RequestParam String placa) {
        boolean exists = almacenService.validarVehiculo(placa);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/validarTransportista")
    public ResponseEntity<Boolean> validarTransportista(@RequestParam String dni) {
        boolean exists = almacenService.validarTransportista(dni);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/validarPedido")
    public ResponseEntity<Boolean> validarPedido(@RequestParam int codPedido) {
        boolean exists = almacenService.validarPedido(codPedido);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/validarEmpleado")
    public ResponseEntity<Boolean> validarEmpleado(@RequestParam String dni) {
        boolean exists = almacenService.validarEmpleado(dni);
        return ResponseEntity.ok(exists);
    }
}
