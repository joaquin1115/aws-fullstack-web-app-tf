package com.sanfernando.sanfernando.services;

import java.util.List;

import com.sanfernando.sanfernando.dtos.requests.ClienteRequest;
import com.sanfernando.sanfernando.dtos.requests.PersonaRequest;
import com.sanfernando.sanfernando.dtos.requests.RepresentanteRequest;
import com.sanfernando.sanfernando.dtos.responses.PedidoClienteResponse;
import com.sanfernando.sanfernando.dtos.responses.PersonaResponse;
import com.sanfernando.sanfernando.dtos.responses.RepresentanteResponse;
import com.sanfernando.sanfernando.models.User;

public interface UserService {
  public List<User> getAll();
  public PersonaResponse newPersona(PersonaRequest personaRequest);
  public PedidoClienteResponse newCliente(ClienteRequest clienteRequest);
  public RepresentanteResponse newRepresentante(RepresentanteRequest RepresentanteRequest);
}
