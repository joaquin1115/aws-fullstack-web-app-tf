package com.sanfernando.sanfernando.services.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sanfernando.sanfernando.dao.PedidoDao;
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

@Service
public class PedidoServiceImpl implements PedidoService{
  
  @Autowired
  private PedidoDao pedidoDao;

  @Override
  public List<PedidoProductoResponse> getProductos() {
    return pedidoDao.getProductos();
  }

  @Override
  public List<PedidoListaReponse> getAll() {
    return pedidoDao.getAll();
  }

  @Override
  public PedidoFormResponse newForm(PedidoFormRequest pedidoFormRequest) {
    return pedidoDao.newForm(pedidoFormRequest);
  }

  @Override
  public PedidoTicketResponse newTicket(PedidoTicketRequest pedidoTicketRequest) {
    return pedidoDao.newTicket(pedidoTicketRequest);
  }

  @Override
  public PedidoResponse newPedido(PedidoRequest pedidoRequest) {
    return pedidoDao.newPedido(pedidoRequest);
  }

  @Override
  public OrdenResponse getOrderDetails(int orderId) {
    return pedidoDao.getOrderDetails(orderId);
  }

  @Override
  public List<OrderItem> getOrderItems(int orderId) {
    return pedidoDao.getOrderItems(orderId);
  }
}
