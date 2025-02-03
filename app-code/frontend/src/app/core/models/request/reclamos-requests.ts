export class DatosClientesRequest {
  nombCliente: string;
  nomRepresentante: string;
  CargoRepresentante: string;
  correoEmpresarial: string;
  Direccion: string;
  codticket: number;
  nombProducto: string;
  fechaAdquisicion: string;
  nroLote: number;
  cantidadAdquirida: string;
  tipoReclamo: string;
  descripcionProblema: string;
  fechaIncidencia: string;
  urgencia: string;
  nombreEvidencia: string;
  tipoEvidencia: string;
  tipoArchivo: string;
  areaResponsable: string;
  accionSolicitada: string;
  comentario: string;
  fechaEsperada: string;
  nroCaso: string;
  estadoReclamo: string;
  constructor(
    nombCliente: string,
    nomRepresentante: string,
    CargoRepresentante: string,
    correoEmpresarial: string,
    Direccion: string,
    codticket: number,
    nombProducto: string,
    fechaAdquisicion: string,
    nroLote: number,
    cantidadAdquirida: string,
    tipoReclamo: string,
    descripcionProblema: string,
    fechaIncidencia: string,
    urgencia: string,
    nombreEvidencia: string,
    tipoEvidencia: string,
    tipoArchivo: string,
    areaResponsable: string,
    accionSolicitada: string,
    comentario: string,
    fechaEsperada: string,
    nroCaso: string,
    estadoReclamo: string
  ) {
    this.nombCliente = nombCliente;
    this.nomRepresentante = nomRepresentante;
    this.CargoRepresentante = CargoRepresentante;
    this.correoEmpresarial = correoEmpresarial;
    this.Direccion = Direccion;
    this.codticket = codticket;
    this.nombProducto = nombProducto;
    this.fechaAdquisicion = fechaAdquisicion;
    this.nroLote = nroLote;
    this.cantidadAdquirida = cantidadAdquirida;
    this.tipoReclamo = tipoReclamo;
    this.descripcionProblema = descripcionProblema;
    this.fechaIncidencia = fechaIncidencia;
    this.urgencia = urgencia;
    this.nombreEvidencia = nombreEvidencia;
    this.tipoEvidencia = tipoEvidencia;
    this.tipoArchivo = tipoArchivo;
    this.areaResponsable = areaResponsable;
    this.accionSolicitada = accionSolicitada;
    this.comentario = comentario;
    this.fechaEsperada = fechaEsperada;
    this.nroCaso = nroCaso;
    this.estadoReclamo = estadoReclamo;
  }
}
