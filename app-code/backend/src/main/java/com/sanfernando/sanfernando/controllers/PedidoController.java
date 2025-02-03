package com.sanfernando.sanfernando.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sanfernando.sanfernando.dtos.requests.PedidoFormRequest;
import com.sanfernando.sanfernando.dtos.requests.PedidoRequest;
import com.sanfernando.sanfernando.dtos.requests.PedidoTicketRequest;
import com.sanfernando.sanfernando.dtos.responses.PedidoFormResponse;
import com.sanfernando.sanfernando.dtos.responses.PedidoListaReponse;
import com.sanfernando.sanfernando.dtos.responses.PedidoProductoResponse;
import com.sanfernando.sanfernando.dtos.responses.PedidoResponse;
import com.sanfernando.sanfernando.dtos.responses.PedidoTicketResponse;
import com.sanfernando.sanfernando.dtos.responses.pedidos.OrdenResponse;
import com.sanfernando.sanfernando.dtos.responses.pedidos.OrderItem;
import com.sanfernando.sanfernando.services.PedidoService;

import lombok.RequiredArgsConstructor;
@RestController
@CrossOrigin(origins = {"*"})
@RequestMapping("/api/pedido")
@RequiredArgsConstructor
public class PedidoController {

  @Autowired
  private PedidoService pedidoService;

  @GetMapping(value = "all")
  public ResponseEntity<Object> getAll() {
    List<PedidoListaReponse> response = pedidoService.getAll();
    return ResponseEntity.ok(response);
  }

  @PostMapping(value = "datos")
  public ResponseEntity<Object> newForm(@RequestBody PedidoFormRequest request) {
    System.out.println(request);
    PedidoFormResponse response = pedidoService.newForm(request);
    return ResponseEntity.ok(response);
  }

  @GetMapping(value = "productos")
  public ResponseEntity<Object> getProductos() {
    List<PedidoProductoResponse> response = pedidoService.getProductos();
    return ResponseEntity.ok(response);
  }

  @PostMapping(value = "ticket")
  public ResponseEntity<Object> newTicket(@RequestBody PedidoTicketRequest request) {
    PedidoTicketResponse response = pedidoService.newTicket(request);
    return ResponseEntity.ok(response);
  }

  @PostMapping(value = "new")
  public ResponseEntity<Object> newPedido(@RequestBody PedidoRequest request) {
    PedidoResponse response = pedidoService.newPedido(request);
    return ResponseEntity.ok(response);
  }

  @GetMapping(value = "details/{id}")
  public ResponseEntity<Object> getDetails(@PathVariable("id") int idPedido) {
    OrdenResponse response = pedidoService.getOrderDetails(idPedido);
    return ResponseEntity.ok(response);
  }

  @GetMapping(value = "items/{id}")
  public ResponseEntity<Object> getItems(@PathVariable("id") int idPedido) {
    List<OrderItem> response = pedidoService.getOrderItems(idPedido);
    return ResponseEntity.ok(response);
  }
}
