export interface SeguimientoTrasladoDetalleResponse {
  nombreCompletoTransportista: string;
  placa: string;
  origen: string;
  horaSalida: string;
  destino: string;
}

export interface SeguimientoTrasladoListaResponse {
  codGuiaRemision: string;
  origen: string;
  destino: string;
}

export interface SeguimientoTrasladoPedidoListaResponse {
  idPedido: string;
  tipoPedido: string;
  fechaSalida: string;
  fechaLLegada: string;
  destino: string;
  idEstadoPedido: string;
}

export interface SeguimientoRutaDetalleResponse {
  orden: number;
  local: string;
  tipoParadero: string;
}

export interface SeguimientoRutaListaResponse {
  idRuta: number;
  tipoRuta: string;
  distanciaTotal: number;
  origen: string;
  destino: string;
}

export interface SeguimientoTransportistaListaResponse {
  idTransportista: number;
  nombreCompleto: string;
  licencia: string;
  tipoLicencia: string;
  fechaVencimientoLicencia: Date;
  estado: string;
}

export interface SeguimientoTransportistaDetalleResponse {
  nombreCompleto: string;
  dni: string;
  licencia: string;
  tipoLicencia: string;
  fechaVencimientoLicencia: Date;
  estado: string;
}


export interface SeguimientoVehiculoDetallesResponse {
  marca: string;
  modelo: string;
  estado: string;
  anioFabricacion: number;
  placa: string;
  tipo: string;
  capacidadCarga: number;
  fechaUltimoMantenimiento: Date;
}

export interface SeguimientoVehiculoListaResponse {
  idVehiculo: number;
  placa: string;
  modelo: string;
  anioFabricacion: number;
  capacidadCarga: number;
  fechaUltimoViaje: string;
  fechaUltimoMantenimiento: string;
  estado: string;
}
