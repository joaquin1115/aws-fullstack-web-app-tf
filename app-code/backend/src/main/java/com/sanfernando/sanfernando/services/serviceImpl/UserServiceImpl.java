package com.sanfernando.sanfernando.services.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sanfernando.sanfernando.dao.UserDao;
import com.sanfernando.sanfernando.dtos.requests.ClienteRequest;
import com.sanfernando.sanfernando.dtos.requests.PersonaRequest;
import com.sanfernando.sanfernando.dtos.requests.RepresentanteRequest;
import com.sanfernando.sanfernando.dtos.responses.PedidoClienteResponse;
import com.sanfernando.sanfernando.dtos.responses.PersonaResponse;
import com.sanfernando.sanfernando.dtos.responses.RepresentanteResponse;
import com.sanfernando.sanfernando.models.User;
import com.sanfernando.sanfernando.services.UserService;

@Service
public class UserServiceImpl implements UserService{

  @Autowired
  private UserDao userDao;

  @Override
  public List<User> getAll() {
    return userDao.getAll();
  }

  @Override
  public PersonaResponse newPersona(PersonaRequest personaRequest) {
    return userDao.newPersona(personaRequest);
  }

  @Override
  public PedidoClienteResponse newCliente(ClienteRequest clienteRequest) {
    return userDao.newCliente(clienteRequest);
  }

  @Override
  public RepresentanteResponse newRepresentante(RepresentanteRequest RepresentanteRequest) {
    return userDao.newRepresentante(RepresentanteRequest);
  }

}
