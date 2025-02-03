export class TransportistaListaResponse{
  codigoDelConductor: string;
  codigoDelEmpleado: string;
  tipoDeLicencia: string;
  fechaDeVencimientoDeLicencia: string;
  fechaUltimoTraslado: string;
  estadoDelConductor: string;

  constructor(
    codigoDelConductor: string,
    codigoDelEmpleado: string,
    tipoDeLicencia: string,
    fechaDeVencimientoDeLicencia: string,
    fechaUltimoTraslado: string,
    estadoDelConductor: string
  ) {
    this.codigoDelConductor = codigoDelConductor;
    this.codigoDelEmpleado = codigoDelEmpleado;
    this.tipoDeLicencia = tipoDeLicencia;
    this.fechaDeVencimientoDeLicencia = fechaDeVencimientoDeLicencia;
    this.fechaUltimoTraslado = fechaUltimoTraslado;
    this.estadoDelConductor = estadoDelConductor;
  }
}
