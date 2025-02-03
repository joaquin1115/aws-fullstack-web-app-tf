package com.sanfernando.sanfernando.dao;

import java.util.List;

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

public interface PedidoDao {
  public PedidoFormResponse newForm(PedidoFormRequest pedidoFormRequest);
  public List<PedidoProductoResponse> getProductos();
  public List<PedidoListaReponse> getAll();
  public PedidoTicketResponse newTicket(PedidoTicketRequest pedidoTicketRequest);
  public PedidoResponse newPedido(PedidoRequest pedidoRequest);
  public OrdenResponse getOrderDetails(int orderId);
  public List<OrderItem> getOrderItems(int orderId);
}
