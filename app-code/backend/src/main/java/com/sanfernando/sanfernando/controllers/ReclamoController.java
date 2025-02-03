package com.sanfernando.sanfernando.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sanfernando.sanfernando.dtos.requests.reclamos.ReclamoFormCreateRequest;
import com.sanfernando.sanfernando.dtos.responses.reclamos.ReclamoEmpresaListaResponse;
import com.sanfernando.sanfernando.dtos.responses.reclamos.ReclamoListaResponse;
import com.sanfernando.sanfernando.dtos.responses.reclamos.ReclamoRepresentanteListaResponse;
import com.sanfernando.sanfernando.dtos.responses.reclamos.ReclamoTicketListaResponse;
import com.sanfernando.sanfernando.dtos.responses.reclamos.ReclamoTicketProductoDetalleResponse;
import com.sanfernando.sanfernando.dtos.responses.reclamos.ReclamoTicketProductoListaResponse;
import com.sanfernando.sanfernando.dtos.responses.reclamos.ReclamoVisorResponse;
import com.sanfernando.sanfernando.services.ReclamoService;

import java.util.List;

@RestController
@CrossOrigin(origins = {"*"})
@RequestMapping("/api/reclamos")
@RequiredArgsConstructor
public class ReclamoController {
    
	@Autowired
	private ReclamoService reclamoService;

	@GetMapping("/lista")
	public List<ReclamoListaResponse> obtenerReclamosLista() {
		return reclamoService.obtenerReclamosLista();
	}

	@PostMapping("/crear")
	public ResponseEntity<String> crearReclamo(@RequestBody ReclamoFormCreateRequest reclamoDTO) {
		try {
			reclamoService.crearReclamo(reclamoDTO);
			return ResponseEntity.ok("Reclamo creado exitosamente");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el reclamo: " + e.getMessage());
		}
	}

	@GetMapping("/representantes")
	public List<ReclamoRepresentanteListaResponse> obtenerRepresentantesLista(@RequestParam int empresaId){
		return reclamoService.obtenerRepresentantesLista(empresaId);
	}

	@GetMapping("/empresas")
	public List<ReclamoEmpresaListaResponse> obtenerEmpresas(){
		return reclamoService.obtenerEmpresas();
	}

	@GetMapping("/tickets")
	public List<ReclamoTicketListaResponse> obtenerTickets(@RequestParam int idRepresentante){
		return reclamoService.obtenerTickets(idRepresentante);
	}

	@GetMapping("/productos")
	public List<ReclamoTicketProductoListaResponse> obtenerProductos(@RequestParam int idTicket){
		return reclamoService.obtenerProductos(idTicket);
	}

	@GetMapping("/productoDetalle")
	public ReclamoTicketProductoDetalleResponse obtenerProductoDetalle(@RequestParam int idTicket, @RequestParam int idProducto){
		return reclamoService.obtenerProductoDetalle(idTicket, idProducto).get(0);
	}

	@GetMapping("/visor")
	public ReclamoVisorResponse obtenerVisor(@RequestParam int idReclamo){
		return reclamoService.obtenerVisor(idReclamo);
	}
}

