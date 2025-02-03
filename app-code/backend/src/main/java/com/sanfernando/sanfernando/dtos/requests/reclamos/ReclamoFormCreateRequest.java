package com.sanfernando.sanfernando.dtos.requests.reclamos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReclamoFormCreateRequest {
    private String nombCliente;
    private String nomRepresentante;
    private String CargoRepresentante;
    private String correoEmpresarial;
    private String Direccion;
    private Integer codticket;
    private String nombProducto;
    private String fechaAdquisicion;
    private Integer nroLote;
    private String cantidadAdquirida;
    private String tipoReclamo;
    private String descripcionProblema;
    private String fechaIncidencia;
    private String urgencia;
    private String nombreEvidencia;
    private String tipoEvidencia;
    private String tipoArchivo;
    private String areaResponsable;
    private String accionSolicitada;
    private String comentario;
    private String fechaEsperada;
    private String nroCaso;
    private String estadoReclamo;
}
