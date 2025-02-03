export interface BusquedaAlmacenResponse {
  proceso: Proceso;
  mercancias: Mercancia[];
  traslado: Traslado;
}

export interface Proceso {
  estado: string;
  operaciones: Operacion[];
}

export interface Operacion {
  idOperacion: number;
  fecha: string;
  horaInicio: string;
  horaFin: string;
  tipoOperacion: string;
  dniEmpEjecutor: string;
  dniEmpSupervisor: string;
}

export interface Mercancia {
  nroPrecinto: string;
  stocks: Stock[];
}

export interface Stock {
  codStock: string;
  nombreStock: string;
  categoria: string;
  tipo: string;
  segmento: string;
  unidad: string;
  cantidadTransportar: number;
}

export interface Traslado {
  estado: string;
  codGuiaRemision: string;
  ruta: Ruta;
  transportista: Transportista;
  vehiculo: Vehiculo;
}

export interface Ruta {
  codRuta: number;
  tipoRuta: string;
  distanciaTotal: number;
  duracion: number;
}

export interface Transportista {
  dniTransportista: string;
  nombreCompleto: string;
  nacionalidad: string;
}

export interface Vehiculo {
  placaVehiculo: string;
  modeloVehiculo: string;
  anioFabricacion: number;
  capacidadCarga: number;
  fechaUltimoMantenimiento: string;
  fechaUltimoViaje: string;
}
