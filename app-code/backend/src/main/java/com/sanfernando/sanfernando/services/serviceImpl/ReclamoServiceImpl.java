package com.sanfernando.sanfernando.services.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sanfernando.sanfernando.dao.ReclamoDao;
import com.sanfernando.sanfernando.dtos.requests.reclamos.ReclamoFormCreateRequest;
import com.sanfernando.sanfernando.dtos.responses.reclamos.ReclamoEmpresaListaResponse;
import com.sanfernando.sanfernando.dtos.responses.reclamos.ReclamoListaResponse;
import com.sanfernando.sanfernando.dtos.responses.reclamos.ReclamoRepresentanteListaResponse;
import com.sanfernando.sanfernando.dtos.responses.reclamos.ReclamoTicketListaResponse;
import com.sanfernando.sanfernando.dtos.responses.reclamos.ReclamoTicketProductoDetalleResponse;
import com.sanfernando.sanfernando.dtos.responses.reclamos.ReclamoTicketProductoListaResponse;
import com.sanfernando.sanfernando.dtos.responses.reclamos.ReclamoVisorResponse;
import com.sanfernando.sanfernando.services.ReclamoService;

@Service
public class ReclamoServiceImpl implements ReclamoService {
  
  @Autowired
  private ReclamoDao reclamoDao;

  @Override
  public List<ReclamoListaResponse> obtenerReclamosLista() {
    return reclamoDao.obtenerReclamosLista();
  }
  @Override
  public void crearReclamo(ReclamoFormCreateRequest reclamoDTO) throws Exception {
    reclamoDao.insertarReclamo(reclamoDTO);
  }

  @Override
  public List<ReclamoRepresentanteListaResponse> obtenerRepresentantesLista(int empresaId) {
    return reclamoDao.obtenerRepresentantesLista(empresaId);
  }

  @Override
  public List<ReclamoEmpresaListaResponse> obtenerEmpresas() {
    return reclamoDao.obtenerEmpresas();
  }

  @Override
  public List<ReclamoTicketListaResponse> obtenerTickets(int idRepresentante) {
    return reclamoDao.obtenerTickets(idRepresentante);
  }

  @Override
  public List<ReclamoTicketProductoListaResponse> obtenerProductos(int idTicket) {
    return reclamoDao.obtenerProductos(idTicket);
  }

  @Override
  public List<ReclamoTicketProductoDetalleResponse> obtenerProductoDetalle(int idTicket, int idProducto) {
    return reclamoDao.obtenerProductoDetalle(idTicket, idProducto);
  }

  @Override
  public ReclamoVisorResponse obtenerVisor(int idReclamo) {
    return reclamoDao.obtenerVisor(idReclamo);
  }
}
