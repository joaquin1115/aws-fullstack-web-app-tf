export class IncidenciasFormRequest {
  tipoIncidencia: string;
  descripcion: string;
  tipoProcedimiento: string;
  tipoNorma: string;
  fecha: string;
  hora: string;
  codTraslado: number;
  tiempoEstimadoProcedimiento: number;

  constructor(
    tipoIncidencia: string,
    descripcion: string,
    tipoProcedimiento: string,
    tipoNorma: string,
    fecha: string,
    hora: string,
    codTraslado: number,
    tiempoEstimadoProcedimiento: number
  ) {
    this.tipoIncidencia = tipoIncidencia;
    this.descripcion = descripcion;
    this.tipoProcedimiento = tipoProcedimiento;
    this.tipoNorma = tipoNorma;
    this.fecha = fecha;
    this.hora = hora;
    this.codTraslado = codTraslado;
    this.tiempoEstimadoProcedimiento = tiempoEstimadoProcedimiento;
  }

}
