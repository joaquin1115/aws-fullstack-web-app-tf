package com.sanfernando.sanfernando.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sanfernando.sanfernando.dtos.requests.ClienteRequest;
import com.sanfernando.sanfernando.dtos.requests.PersonaRequest;
import com.sanfernando.sanfernando.dtos.requests.RepresentanteRequest;
import com.sanfernando.sanfernando.dtos.responses.PedidoClienteResponse;
import com.sanfernando.sanfernando.dtos.responses.PersonaResponse;
import com.sanfernando.sanfernando.dtos.responses.RepresentanteResponse;
import com.sanfernando.sanfernando.models.User;
import com.sanfernando.sanfernando.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin(origins = {"*"})
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
  
  @Autowired
  private UserService userService;

  @GetMapping(value = "all")
  public ResponseEntity<Object> getAllUsers() {
    List<User> users = userService.getAll();
    return ResponseEntity.ok(users);
  }

  @PostMapping(value = "persona")
  public ResponseEntity<Object> newPersona(@RequestBody PersonaRequest request) {
    System.out.println(request);
    PersonaResponse response = userService.newPersona(request);
    return ResponseEntity.ok(response);
  }

  @PostMapping(value = "representante")
  public ResponseEntity<Object> newRepresentante(@RequestBody RepresentanteRequest request) {
    RepresentanteResponse response = userService.newRepresentante(request);
    return ResponseEntity.ok(response);
  }

  @PostMapping(value = "cliente")
  public ResponseEntity<Object> newCliente(@RequestBody ClienteRequest request) {
    PedidoClienteResponse response = userService.newCliente(request);
    return ResponseEntity.ok(response);
  }
}
