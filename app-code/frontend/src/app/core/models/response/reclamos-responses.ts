export class ReclamoListaResponse {
  codReclamo: string;
  estadoReclamo: string;
  fechaReclamo: string;
  clienteRuc: string;
  tipoCliente: string;
  tipoReclamo: string;
  areaRes: string;

  constructor(
    codReclamo: string,
    estadoReclamo: string,
    fechaReclamo: string,
    clienteRuc: string,
    tipoCliente: string,
    tipoReclamo: string,
    areaRes: string
  ) {
    this.codReclamo = codReclamo;
    this.estadoReclamo = estadoReclamo;
    this.fechaReclamo = fechaReclamo;
    this.clienteRuc = clienteRuc;
    this.tipoCliente = tipoCliente;
    this.tipoReclamo = tipoReclamo;
    this.areaRes = areaRes;
  }
}

export interface ReclamoFormCreate {
  codRepresentante: number;
  codPedido: number;
  codSeguimiento: number;
  codTipoReclamo: string;
  codNivelUrgencia: string;
  codEstadoReclamo: string;
  comentario: string;
  fechaSuceso: string;
  fechaReclamo: string;
  fechaEsperada: string;
  nroCaso: string;
}

export interface ReclamoSeguimientoFormCreate {
  codClienteInterno: number;
  codTipoAccion: string;
  fechaResolucion: string;
  nroCaso: string;
  comentario: string;
}

export interface ReclamoEvidenciaCreate {
  nombreEvidencia: string;
  tipoEvidencia: string;
  tipoArchivo: string;
}

export interface ReclamoVisorClienteResponse {
  nombre: string;
  representante: string;
  cargo: string;
  correoEmpresarial: string;
  direccion: string;
}

export interface ReclamoVisorDetalleResponse {
  idTicket: number;
  nombre: string;
  fechaEntrega: string;
  nroLote: string;
  cantidad: number;
}

export interface ReclamoVisorEvidenciaResponse {
  evidencia: string;
}

export interface ReclamoVisorNaturalezaResponse {
  descripcion: string;
  comentario: string;
  fechaSuceso: string;
  fechaReclamo: string;
  descripcionNivelUrgencia: string;
}

export interface ReclamoVisorResolucionResponse {
  nombre: string;
  descripcion: string;
  comentario: string;
}

export interface ReclamoVisorSeguimientoResponse {
  fechaResolucion: string;
  numeroCaso: string;
  descripcionEstadoReclamo: string;
}

export interface ReclamoVisorResponse {
  cliente: ReclamoVisorClienteResponse;
  detalle: ReclamoVisorDetalleResponse;
  naturaleza: ReclamoVisorNaturalezaResponse;
  evidencias: ReclamoVisorEvidenciaResponse[];
  resolucion: ReclamoVisorResolucionResponse;
  seguimiento: ReclamoVisorSeguimientoResponse;
}