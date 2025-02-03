package com.sanfernando.sanfernando.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sanfernando.sanfernando.dtos.requests.ClienteRequest;
import com.sanfernando.sanfernando.dtos.requests.LoginRequest;
import com.sanfernando.sanfernando.dtos.requests.PersonaRequest;
import com.sanfernando.sanfernando.dtos.requests.RepresentanteRequest;
import com.sanfernando.sanfernando.dtos.responses.PedidoClienteResponse;
import com.sanfernando.sanfernando.dtos.responses.LoginResponse;
import com.sanfernando.sanfernando.dtos.responses.PersonaResponse;
import com.sanfernando.sanfernando.dtos.responses.RepresentanteResponse;
import com.sanfernando.sanfernando.models.User;

@Repository
public interface UserDao {
  public List<User> getAll();
  public LoginResponse login(LoginRequest loginRequest);
  public PersonaResponse newPersona(PersonaRequest personaRequest);
  public PedidoClienteResponse newCliente(ClienteRequest clienteRequest);
  public RepresentanteResponse newRepresentante(RepresentanteRequest RepresentanteRequest);
}
