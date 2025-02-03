package com.sanfernando.sanfernando.dao;

import java.util.List;

import com.sanfernando.sanfernando.dtos.requests.reclamos.ReclamoFormCreateRequest;
import com.sanfernando.sanfernando.dtos.responses.reclamos.ReclamoEmpresaListaResponse;
import com.sanfernando.sanfernando.dtos.responses.reclamos.ReclamoListaResponse;
import com.sanfernando.sanfernando.dtos.responses.reclamos.ReclamoRepresentanteListaResponse;
import com.sanfernando.sanfernando.dtos.responses.reclamos.ReclamoTicketListaResponse;
import com.sanfernando.sanfernando.dtos.responses.reclamos.ReclamoTicketProductoDetalleResponse;
import com.sanfernando.sanfernando.dtos.responses.reclamos.ReclamoTicketProductoListaResponse;
import com.sanfernando.sanfernando.dtos.responses.reclamos.ReclamoVisorResponse;

public interface ReclamoDao {
  List<ReclamoListaResponse> obtenerReclamosLista();
  void insertarReclamo(ReclamoFormCreateRequest reclamoDTO) throws Exception;
  List<ReclamoRepresentanteListaResponse> obtenerRepresentantesLista(int empresaId);
  List<ReclamoEmpresaListaResponse> obtenerEmpresas();
  List<ReclamoTicketListaResponse> obtenerTickets(int idRepresentante);
  List<ReclamoTicketProductoListaResponse> obtenerProductos(int idTicket);
  List<ReclamoTicketProductoDetalleResponse> obtenerProductoDetalle(int idTicket, int idProducto);
  ReclamoVisorResponse obtenerVisor(int idReclamo);
}
