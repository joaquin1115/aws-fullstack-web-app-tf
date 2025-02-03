export class VehiculoResponse {

  codigoDelVehiculo: string;
  anioDeFabricacion: string;
  fechaDeUltimoMantenimiento: string;
  capacidadDeCarga: string;
  modelo: string;
  placa: string;
  fechaUltimoViaje : string;
  estadoDelVehiculo: string;

  constructor(
    codigoDelVehiculo: string,
    anioDeFabricacion: string,
    fechaDeUltimoMantenimiento: string,
    capacidadDeCarga: string,
    modelo: string,
    placa: string,
    fechaUltimoViaje: string,
    estadoDelVehiculo: string
  ) {
    this.codigoDelVehiculo = codigoDelVehiculo;
    this.anioDeFabricacion = anioDeFabricacion;
    this.fechaDeUltimoMantenimiento = fechaDeUltimoMantenimiento;
    this.capacidadDeCarga = capacidadDeCarga;
    this.modelo = modelo;
    this.placa = placa;
    this.fechaUltimoViaje = fechaUltimoViaje;
    this.estadoDelVehiculo = estadoDelVehiculo;
  }
}
