export class IncidenciaResponse {
   codigoDeIncidencia: string;
   codigoDeTraslado: string;
   descripcionTipoDeIncidencia: string;
   fechaDeOcurrencia: string;
   horaDeOcurrencia: string;
   estadoDeIncidencia: string;

  constructor(
    codigoDeIncidencia: string,
    codigoDeTraslado: string,
    descripcionTipoDeIncidencia: string,
    fechaDeOcurrencia: string,
    horaDeOcurrencia: string,
    estadoDeIncidencia: string
  ) {
    this.codigoDeIncidencia = codigoDeIncidencia;
    this.codigoDeTraslado = codigoDeTraslado;
    this.descripcionTipoDeIncidencia = descripcionTipoDeIncidencia;
    this.fechaDeOcurrencia = fechaDeOcurrencia;
    this.horaDeOcurrencia = horaDeOcurrencia;
    this.estadoDeIncidencia = estadoDeIncidencia;
  }
}
