package com.sanfernando.sanfernando.dao.daoImpl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.sanfernando.sanfernando.dao.UserDao;
import com.sanfernando.sanfernando.dtos.requests.ClienteRequest;
import com.sanfernando.sanfernando.dtos.requests.LoginRequest;
import com.sanfernando.sanfernando.dtos.requests.PersonaRequest;
import com.sanfernando.sanfernando.dtos.requests.RepresentanteRequest;
import com.sanfernando.sanfernando.dtos.responses.PedidoClienteResponse;
import com.sanfernando.sanfernando.dtos.responses.LoginResponse;
import com.sanfernando.sanfernando.dtos.responses.PersonaResponse;
import com.sanfernando.sanfernando.dtos.responses.RepresentanteResponse;
import com.sanfernando.sanfernando.models.User;
import com.sanfernando.sanfernando.utils.Conexion;

@Repository
public class UserDaoImpl implements UserDao{

  private final Conexion con = new Conexion();

  @Override
  public List<User> getAll() {
    con.startConexion();
    List<User> users = new ArrayList<>();
    try {
      String query = "SELECT * FROM persona;";
      PreparedStatement ps = con.getCon().prepareStatement(query);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        User user = User
          .builder()
          .cod_persona(rs.getInt("cod_persona"))
          .cod_estado_civil(rs.getInt("cod_estado_civil"))
          .cod_nacionalidad(rs.getInt("cod_nacionalidad"))
          .build();
        users.add(user);
      }
      rs.close();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    con.closeConexion();
    return users;
  }

  @Override
  public LoginResponse login(LoginRequest loginRequest) {
    con.startConexion();
    List<LoginResponse> loginResponses = new ArrayList<>();
    try {
      String query = 
        "SELECT u.id_empleado, u.dni, u.cargo, c.nombre AS area, u.representante " +
        "FROM ( " +
          "SELECT e.cod_empleado id_empleado, ec.descripcion cargo,p.dni, e.cod_cliente, false AS representante " +
          "FROM empleado AS e " +
          "INNER JOIN empleado_cargo AS ec ON ec.cod_empleado_cargo = e.cod_empleado_cargo " +
          "INNER JOIN persona AS p ON p.cod_persona = e.cod_persona " +
          "UNION " +
          "SELECT r.cod_representante id_empleado, r.cargo cargo,p.dni, r.cod_cliente, true AS representante " +
          "FROM representante AS r " +
          "INNER JOIN persona AS p ON p.cod_persona = r.cod_persona " +
        ") AS u " +
        "INNER JOIN cliente AS c ON c.cod_cliente = u.cod_cliente " +
        "INNER JOIN cliente_tipo AS ct ON ct.cod_cliente_tipo = c.cod_cliente_tipo " +
        "WHERE ct.cod_cliente_tipo = 'I' AND u.dni = ?; ";
      PreparedStatement ps = con.getCon().prepareStatement(query);
      ps.setString(1,loginRequest.getDni());
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        LoginResponse loginResponse = LoginResponse
          .builder()
          .dni(rs.getString("dni"))
          .area(rs.getString("area"))
          .cargo(rs.getString("cargo"))
          .representante(rs.getBoolean("representante"))
          .idEmpleado(rs.getInt("id_empleado"))
          .build();
        loginResponses.add(loginResponse);
      }
      rs.close();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    con.closeConexion();
    return loginResponses.get(0);
  }

  @Override
  public PersonaResponse newPersona(PersonaRequest personaRequest) {
    con.startConexion();
    List<PersonaResponse> personaResponses = new ArrayList<>();
    try {
      System.out.println("UserDaoImpl.newPersona: " + personaRequest + "\n\n\n");
      String query = 
        "INSERT INTO persona" +
        "( cod_estado_civil, cod_nacionalidad, cod_genero, dni, primer_apellido, segundo_apellido, prenombre, direccion) VALUES " +
        "(?, ?, ?,  ?, ?, ?, ?, ?); ";
      PreparedStatement ps = con.getCon().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
      System.out.println("Preparando query: \n\n\n");
      ps.setInt(1, 1);
      ps.setInt(2, 1);
      ps.setInt(3, 1);
      ps.setString(4, personaRequest.getDni());
      ps.setString(5, personaRequest.getPrimerApellido());
      ps.setString(6, personaRequest.getSegundoApellido());
      ps.setString(7, personaRequest.getPrenombre());
      ps.setString(8, "Direccion de prueba");
      System.out.println("Apunto de ejecutar query: \n\n\n");
      ps.executeUpdate();
      System.out.println("Ejecutnado query: \n\n\n");
      ResultSet rs = ps.getGeneratedKeys();
      while (rs.next()) {
        PersonaResponse personaResponse = PersonaResponse
          .builder()
          .idPersona(rs.getInt(1))
          .build();
          personaResponses.add(personaResponse);
      }
      
      rs.close();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    con.closeConexion();
    return personaResponses.get(0);
  }

  @Override
  public PedidoClienteResponse newCliente(ClienteRequest clienteRequest) {
    con.startConexion();
    List<PedidoClienteResponse> clienteResponses = new ArrayList<>();
    try {
      String query = 
        "INSERT INTO cliente " +
        "(cod_cliente_tipo, cod_cliente_estado, nombre , ruc , razon_social, fecha_registro) " + 
        "VALUES (?, ?, ?,  ?, ?, ?);";
      PreparedStatement ps = con.getCon().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, "I");
      ps.setString(2, "A");
      ps.setString(3, clienteRequest.getNombre());
      ps.setString(4, "12345678910");
      ps.setString(5, "Razon social de prueba");

      java.sql.Date sqlDate = java.sql.Date.valueOf("2024-01-01");
      ps.setDate(6, sqlDate);

      ps.executeUpdate();
      ResultSet rs = ps.getGeneratedKeys();
      while (rs.next()) {
        PedidoClienteResponse clienteResponse = PedidoClienteResponse
          .builder()
          .idCliente(rs.getInt(1))
          .build();
          clienteResponses.add(clienteResponse);
      }

      rs.close();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    con.closeConexion();
    return clienteResponses.get(0);
  }

  @Override
  public RepresentanteResponse newRepresentante(RepresentanteRequest RepresentanteRequest) {
    con.startConexion();
    List<RepresentanteResponse> representanteResponses = new ArrayList<>();
    try {
      String query = 
        "INSERT INTO representante " + 
        "(cod_cliente,cod_persona,num_telefono,correo_empresarial,cargo) " +
        "VALUES (?, ?, ?, ?, ?);";
      PreparedStatement ps = con.getCon().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
      ps.setInt(1, RepresentanteRequest.getIdCliente());
      ps.setInt(2, RepresentanteRequest.getIdPersona());
      ps.setString(3, RepresentanteRequest.getTelefono());
      ps.setString(4, RepresentanteRequest.getCorreoEmpresarial());
      ps.setString(5, "Cargo de prueba");

      ps.executeUpdate();
      ResultSet rs = ps.getGeneratedKeys();
      while (rs.next()) {
        RepresentanteResponse representanteResponse = RepresentanteResponse
          .builder()
          .idRepresentante(rs.getInt(1))
          .build();
          representanteResponses.add(representanteResponse);
      }

      rs.close();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    con.closeConexion();
    return representanteResponses.get(0);
  }
  
}
