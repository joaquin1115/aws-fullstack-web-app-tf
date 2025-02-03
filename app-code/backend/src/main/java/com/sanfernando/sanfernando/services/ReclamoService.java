package com.sanfernando.sanfernando.services;

import java.util.List;

import com.sanfernando.sanfernando.dtos.requests.reclamos.ReclamoFormCreateRequest;
import com.sanfernando.sanfernando.dtos.responses.reclamos.ReclamoEmpresaListaResponse;
import com.sanfernando.sanfernando.dtos.responses.reclamos.ReclamoListaResponse;
import com.sanfernando.sanfernando.dtos.responses.reclamos.ReclamoRepresentanteListaResponse;
import com.sanfernando.sanfernando.dtos.responses.reclamos.ReclamoTicketListaResponse;
import com.sanfernando.sanfernando.dtos.responses.reclamos.ReclamoTicketProductoListaResponse;
import com.sanfernando.sanfernando.dtos.responses.reclamos.ReclamoVisorResponse;
import com.sanfernando.sanfernando.dtos.responses.reclamos.ReclamoTicketProductoDetalleResponse;

public interface ReclamoService {
  List<ReclamoListaResponse> obtenerReclamosLista();
  void crearReclamo(ReclamoFormCreateRequest reclamoDTO) throws Exception;
  List<ReclamoRepresentanteListaResponse> obtenerRepresentantesLista(int empresaId);
  List<ReclamoEmpresaListaResponse> obtenerEmpresas();
  List<ReclamoTicketListaResponse> obtenerTickets(int idRepresentante);
  List<ReclamoTicketProductoListaResponse> obtenerProductos(int idTicket);
  List<ReclamoTicketProductoDetalleResponse> obtenerProductoDetalle(int idTicket, int idProducto);
  ReclamoVisorResponse obtenerVisor(int idReclamo);
}
