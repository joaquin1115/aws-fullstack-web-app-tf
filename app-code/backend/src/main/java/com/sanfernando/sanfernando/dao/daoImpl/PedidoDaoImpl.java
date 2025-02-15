package com.sanfernando.sanfernando.dao.daoImpl;

import com.sanfernando.sanfernando.dao.PedidoDao;
import com.sanfernando.sanfernando.dtos.requests.PedidoFormRequest;
import com.sanfernando.sanfernando.dtos.requests.PedidoRequest;
import com.sanfernando.sanfernando.dtos.requests.PedidoTicketProductoRequest;
import com.sanfernando.sanfernando.dtos.requests.PedidoTicketRequest;
import com.sanfernando.sanfernando.dtos.responses.*;
import com.sanfernando.sanfernando.dtos.responses.pedidos.OrdenResponse;
import com.sanfernando.sanfernando.dtos.responses.pedidos.OrderItem;
import com.sanfernando.sanfernando.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class PedidoDaoImpl implements PedidoDao {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private UserService userService;

  @Override
  public List<PedidoProductoResponse> getProductos() {
    String query =
            "SELECT ec.id_elemento_catalogo, ec.nombre, ec.descripcion, ect.descripcion tipo, " +
                    "ec.id_elemento_catalogo_tipo, ec.peso_unitario, ecu.descripcion unidad " +
                    "FROM elemento_catalogo AS ec " +
                    "LEFT JOIN elemento_catalogo_tipo AS ect ON ect.id_elemento_catalogo_tipo = ec.id_elemento_catalogo_tipo " +
                    "INNER JOIN elemento_catalogo_unidad AS ecu ON ecu.cod_unidad = ec.cod_unidad";

    return jdbcTemplate.query(query, (rs, rowNum) ->
            PedidoProductoResponse.builder()
                    .idElementoCatalogo(rs.getInt("id_elemento_catalogo"))
                    .nombre(rs.getString("nombre"))
                    .descripcion(rs.getString("descripcion"))
                    .tipo(rs.getString("tipo"))
                    .idElementoCatalogoTipo(rs.getInt("id_elemento_catalogo_tipo"))
                    .peso(rs.getDouble("peso_unitario"))
                    .unidad(rs.getString("unidad"))
                    .build()
    );
  }

  @Override
  public List<PedidoListaReponse> getAll() {
    String query =
            "SELECT pd.cod_pedido, c.nombre, pr.prenombre, pr.primer_apellido, pr.segundo_apellido, " +
                    "pd.fecha_registro, pde.estado_pedido FROM pedido As pd " +
                    "INNER JOIN pedido_estado AS pde ON pde.cod_pedido_estado = pd.cod_pedido_estado " +
                    "INNER JOIN representante AS r ON r.cod_representante = pd.cod_representante " +
                    "INNER JOIN empleado AS e ON e.cod_empleado = pd.cod_empleado " +
                    "INNER JOIN persona AS pr ON pr.cod_persona = e.cod_persona " +
                    "INNER JOIN cliente AS c ON c.cod_cliente = r.cod_cliente";

    return jdbcTemplate.query(query, (rs, rowNum) ->
            PedidoListaReponse.builder()
                    .idPedido(rs.getInt("cod_pedido"))
                    .nombre(rs.getString("nombre"))
                    .empleado(rs.getString("prenombre") + " " +
                            rs.getString("primer_apellido") + " " +
                            rs.getString("segundo_apellido"))
                    .fecha(rs.getString("fecha_registro"))
                    .estado(rs.getString("estado_pedido"))
                    .build()
    );
  }

  @Override
  public PedidoFormResponse newForm(PedidoFormRequest pedidoFormRequest) {
    PersonaResponse personaResponse = userService.newPersona(pedidoFormRequest.getPersonaRequest());
    PedidoClienteResponse clienteResponse = userService.newCliente(pedidoFormRequest.getClienteRequest());

    pedidoFormRequest.getRepresentanteRequest().setIdPersona(personaResponse.getIdPersona());
    pedidoFormRequest.getRepresentanteRequest().setIdCliente(clienteResponse.getIdCliente());

    RepresentanteResponse representanteResponse = userService.newRepresentante(pedidoFormRequest.getRepresentanteRequest());

    return PedidoFormResponse.builder()
            .idRepresentante(representanteResponse.getIdRepresentante())
            .build();
  }

  @Override
  public PedidoTicketResponse newTicket(PedidoTicketRequest pedidoTicketRequest) {
    String query = "INSERT INTO ticket (fecha_entrega) VALUES (?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
      ps.setDate(1, java.sql.Date.valueOf(pedidoTicketRequest.getFechaEntrega()));
      return ps;
    }, keyHolder);

    return PedidoTicketResponse.builder()
            .idTicket(keyHolder.getKey().intValue())
            .build();
  }

  public void newTicketProducto(PedidoTicketProductoRequest[] pedidoTicketProductoRequests, Integer idTicket) {
    String query =
            "INSERT INTO detalle_ticket_producto (cod_ticket, id_elemento_catalogo, cantidad) " +
                    "VALUES (?, ?, ?)";

    jdbcTemplate.batchUpdate(query,
            List.of(pedidoTicketProductoRequests),
            pedidoTicketProductoRequests.length,
            (PreparedStatement ps, PedidoTicketProductoRequest request) -> {
              ps.setInt(1, idTicket);
              ps.setInt(2, request.getIdElementoCatalogo());
              ps.setInt(3, request.getCantidad());
            });
  }

  @Override
  public PedidoResponse newPedido(PedidoRequest pedidoRequest) {
    // Create ticket first
    PedidoTicketResponse ticketResponse = newTicket(PedidoTicketRequest.builder()
            .fechaEntrega(pedidoRequest.getFechaEntrega())
            .build());

    pedidoRequest.setIdTicket(ticketResponse.getIdTicket());
    newTicketProducto(pedidoRequest.getPedidoTicketProductoRequest(), ticketResponse.getIdTicket());

    // Create pedido
    String query =
            "INSERT INTO pedido (cod_representante, cod_empleado, cod_pedido_tipo, " +
                    "cod_pedido_estado, cod_ticket, fecha_registro) VALUES (?, ?, ?, ?, ?, ?)";

    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
      ps.setInt(1, pedidoRequest.getIdRepresentante());
      ps.setInt(2, pedidoRequest.getIdEmpleadoRegistro());
      ps.setString(3, pedidoRequest.getIdTipoPedido());
      ps.setString(4, pedidoRequest.getIdEstadoPedido());
      ps.setInt(5, pedidoRequest.getIdTicket());
      ps.setDate(6, java.sql.Date.valueOf(pedidoRequest.getFechaRegistro()));
      return ps;
    }, keyHolder);

    return PedidoResponse.builder()
            .idPedido(keyHolder.getKey().intValue())
            .build();
  }

  @Override
  public OrdenResponse getOrderDetails(int orderId) {
    String query =
            "SELECT p.cod_pedido, p.fecha_registro AS fecha_registro_pedido, " +
                    "t.fecha_entrega, c.nombre AS nombre_cliente, " +
                    "CONCAT(rp.prenombre, ' ', rp.primer_apellido, ' ', rp.segundo_apellido) AS nombre_representante, " +
                    "CONCAT(ep.prenombre, ' ', ep.primer_apellido, ' ', ep.segundo_apellido) AS nombre_empleado_registro, " +
                    "pt.tipo_pedido, p.cod_ticket, pe.estado_pedido " +
                    "FROM pedido p " +
                    "INNER JOIN ticket t ON p.cod_ticket = t.cod_ticket " +
                    "INNER JOIN representante r ON p.cod_representante = r.cod_representante " +
                    "INNER JOIN persona rp ON r.cod_persona = rp.cod_persona " +
                    "INNER JOIN cliente c ON r.cod_cliente = c.cod_cliente " +
                    "INNER JOIN empleado e ON p.cod_empleado = e.cod_empleado " +
                    "INNER JOIN persona ep ON e.cod_persona = ep.cod_persona " +
                    "LEFT JOIN pedido_tipo pt ON p.cod_pedido_tipo = pt.cod_pedido_tipo " +
                    "INNER JOIN pedido_estado pe ON p.cod_pedido_estado = pe.cod_pedido_estado " +
                    "WHERE p.cod_pedido = ?";

    return jdbcTemplate.queryForObject(query, (rs, rowNum) ->
                    OrdenResponse.builder()
                            .id(rs.getInt("cod_pedido"))
                            .requestDate(rs.getString("fecha_registro_pedido"))
                            .deliveryDate(rs.getString("fecha_entrega"))
                            .department(rs.getString("nombre_cliente"))
                            .representative(rs.getString("nombre_representante"))
                            .employee(rs.getString("nombre_empleado_registro"))
                            .orderType(rs.getString("tipo_pedido"))
                            .ticketCode(rs.getString("cod_ticket"))
                            .status(rs.getString("estado_pedido"))
                            .build(),
            orderId
    );
  }

  @Override
  public List<OrderItem> getOrderItems(int orderId) {
    String query =
            "SELECT dtp.id_elemento_catalogo, ec.nombre, dtp.cantidad, ecu.descripcion AS unidad " +
                    "FROM detalle_ticket_producto AS dtp " +
                    "INNER JOIN elemento_catalogo AS ec ON dtp.id_elemento_catalogo = ec.id_elemento_catalogo " +
                    "INNER JOIN elemento_catalogo_unidad AS ecu ON ec.cod_unidad = ecu.cod_unidad " +
                    "INNER JOIN ticket AS t ON dtp.cod_ticket = t.cod_ticket " +
                    "INNER JOIN pedido AS p ON t.cod_ticket = p.cod_ticket " +
                    "WHERE p.cod_pedido = ?";

    return jdbcTemplate.query(query, (rs, rowNum) ->
                    OrderItem.builder()
                            .productCode(rs.getString("id_elemento_catalogo"))
                            .name(rs.getString("nombre"))
                            .quantity(rs.getString("cantidad"))
                            .unit(rs.getString("unidad"))
                            .build(),
            orderId
    );
  }
}