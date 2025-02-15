package com.sanfernando.sanfernando.dao.daoImpl;

import com.sanfernando.sanfernando.dao.UserDao;
import com.sanfernando.sanfernando.dtos.requests.ClienteRequest;
import com.sanfernando.sanfernando.dtos.requests.LoginRequest;
import com.sanfernando.sanfernando.dtos.requests.PersonaRequest;
import com.sanfernando.sanfernando.dtos.requests.RepresentanteRequest;
import com.sanfernando.sanfernando.dtos.responses.LoginResponse;
import com.sanfernando.sanfernando.dtos.responses.PedidoClienteResponse;
import com.sanfernando.sanfernando.dtos.responses.PersonaResponse;
import com.sanfernando.sanfernando.dtos.responses.RepresentanteResponse;
import com.sanfernando.sanfernando.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Override
  public List<User> getAll() {
    String query = "SELECT * FROM persona";
    return jdbcTemplate.query(query, (rs, rowNum) ->
            User.builder()
                    .cod_persona(rs.getInt("cod_persona"))
                    .cod_estado_civil(rs.getInt("cod_estado_civil"))
                    .cod_nacionalidad(rs.getInt("cod_nacionalidad"))
                    .build()
    );
  }

  @Override
  public LoginResponse login(LoginRequest loginRequest) {
    String query =
            "SELECT u.id_empleado, u.dni, u.cargo, c.nombre AS area, u.representante " +
                    "FROM ( " +
                    "SELECT e.cod_empleado id_empleado, ec.descripcion cargo, p.dni, e.cod_cliente, false AS representante " +
                    "FROM empleado AS e " +
                    "INNER JOIN empleado_cargo AS ec ON ec.cod_empleado_cargo = e.cod_empleado_cargo " +
                    "INNER JOIN persona AS p ON p.cod_persona = e.cod_persona " +
                    "UNION " +
                    "SELECT r.cod_representante id_empleado, r.cargo cargo, p.dni, r.cod_cliente, true AS representante " +
                    "FROM representante AS r " +
                    "INNER JOIN persona AS p ON p.cod_persona = r.cod_persona " +
                    ") AS u " +
                    "INNER JOIN cliente AS c ON c.cod_cliente = u.cod_cliente " +
                    "INNER JOIN cliente_tipo AS ct ON ct.cod_cliente_tipo = c.cod_cliente_tipo " +
                    "WHERE ct.cod_cliente_tipo = 'I' AND u.dni = ?";

    List<LoginResponse> responses = jdbcTemplate.query(query,
            new Object[]{loginRequest.getDni()},
            (rs, rowNum) -> LoginResponse.builder()
                    .dni(rs.getString("dni"))
                    .area(rs.getString("area"))
                    .cargo(rs.getString("cargo"))
                    .representante(rs.getBoolean("representante"))
                    .idEmpleado(rs.getInt("id_empleado"))
                    .build()
    );

    return responses.isEmpty() ? null : responses.get(0);
  }

  @Override
  public PersonaResponse newPersona(PersonaRequest personaRequest) {
    String query =
            "INSERT INTO persona (cod_estado_civil, cod_nacionalidad, cod_genero, dni, " +
                    "primer_apellido, segundo_apellido, prenombre, direccion) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
      ps.setInt(1, 1);
      ps.setInt(2, 1);
      ps.setInt(3, 1);
      ps.setString(4, personaRequest.getDni());
      ps.setString(5, personaRequest.getPrimerApellido());
      ps.setString(6, personaRequest.getSegundoApellido());
      ps.setString(7, personaRequest.getPrenombre());
      ps.setString(8, "Direccion de prueba");
      return ps;
    }, keyHolder);

    return PersonaResponse.builder()
            .idPersona(keyHolder.getKey().intValue())
            .build();
  }

  @Override
  public PedidoClienteResponse newCliente(ClienteRequest clienteRequest) {
    String query =
            "INSERT INTO cliente (cod_cliente_tipo, cod_cliente_estado, nombre, ruc, " +
                    "razon_social, fecha_registro) VALUES (?, ?, ?, ?, ?, ?)";

    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, "I");
      ps.setString(2, "A");
      ps.setString(3, clienteRequest.getNombre());
      ps.setString(4, "12345678910");
      ps.setString(5, "Razon social de prueba");
      ps.setDate(6, java.sql.Date.valueOf("2024-01-01"));
      return ps;
    }, keyHolder);

    return PedidoClienteResponse.builder()
            .idCliente(keyHolder.getKey().intValue())
            .build();
  }

  @Override
  public RepresentanteResponse newRepresentante(RepresentanteRequest representanteRequest) {
    String query =
            "INSERT INTO representante (cod_cliente, cod_persona, num_telefono, " +
                    "correo_empresarial, cargo) VALUES (?, ?, ?, ?, ?)";

    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
      ps.setInt(1, representanteRequest.getIdCliente());
      ps.setInt(2, representanteRequest.getIdPersona());
      ps.setString(3, representanteRequest.getTelefono());
      ps.setString(4, representanteRequest.getCorreoEmpresarial());
      ps.setString(5, "Cargo de prueba");
      return ps;
    }, keyHolder);

    return RepresentanteResponse.builder()
            .idRepresentante(keyHolder.getKey().intValue())
            .build();
  }
}