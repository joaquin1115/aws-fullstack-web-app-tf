import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { API_URL } from '../../shared/constants/urls.constant';
import { SeguimientoRutaDetalleResponse, SeguimientoRutaListaResponse, SeguimientoTransportistaDetalleResponse, SeguimientoTransportistaListaResponse, SeguimientoTrasladoDetalleResponse, SeguimientoTrasladoListaResponse, SeguimientoTrasladoPedidoListaResponse, SeguimientoVehiculoDetallesResponse, SeguimientoVehiculoListaResponse } from '../models/response/seguimiento-responses';

@Injectable({
  providedIn: 'root',
})
export class SeguimientoService {

  constructor(private http: HttpClient) { }
  private apiurl = API_URL.SEGUIMIENTO;

  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json;charset=utf-8'
    })
  };

  obtenerTrasladosProceso() {
    return this.http.get<SeguimientoTrasladoListaResponse[]>(
      `${this.apiurl}/trasladosProceso`,
      this.httpOptions
    );
  }

  obtenerTrasladoProcesoDetalle(guiaDeRemision: String) {
    console.log(
      `${this.apiurl}/trasladoProcesoDetalle?codGuiaRemision=${guiaDeRemision}`
    );
    return this.http.get<SeguimientoTrasladoDetalleResponse>(
      `${this.apiurl}/trasladoProcesoDetalle?codGuiaRemision=${guiaDeRemision}`,
      this.httpOptions
    );
  }

  obtenerTrasladosProcesoPedidos(guiaDeRemision: String) {
    return this.http.get<SeguimientoTrasladoPedidoListaResponse[]>(
      `${this.apiurl}/trasladoProcesoPedidos?codGuiaRemision=${guiaDeRemision}`,
      this.httpOptions
    );
  }

  actualizarTrasladoProcesoPedido(idPedido: number) {
    return this.http.put<any>(
      `${this.apiurl}/actualizarTrasladoProcesoPedido?idPedido=${idPedido}`,
      this.httpOptions
    );
  }
  obtenerRutas() {
    return this.http.get<SeguimientoRutaListaResponse[]>(
      `${this.apiurl}/obtenerRutas`,
      this.httpOptions
    );
  }
  obtenerRutaDetalle(idRuta: number) {
    return this.http.get<SeguimientoRutaDetalleResponse[]>(
      `${this.apiurl}/obtenerRutaDetalle?idRuta=${idRuta}`,
      this.httpOptions
    );
  }
  obtenerTransportistas() {
    return this.http.get<SeguimientoTransportistaListaResponse[]>(
      `${this.apiurl}/obtenerTransportistas`,
      this.httpOptions
    );
  }
  obtenerTransportistaDetalle(idTransportista: number) {
    return this.http.get<SeguimientoTransportistaDetalleResponse>(
      `${this.apiurl}/obtenerTransportistaDetalle?idTransportista=${idTransportista}`,
      this.httpOptions
    );
  }
  obtenerVehiculos() {
    return this.http.get<SeguimientoVehiculoListaResponse[]>(
      `${this.apiurl}/obtenerVehiculos`,
      this.httpOptions
    );
  }
  obtenerVehiculoDetalle(idVehiculo: number) {
    return this.http.get<SeguimientoVehiculoDetallesResponse>(
      `${this.apiurl}/obtenerVehiculoDetalle?idVehiculo=${idVehiculo}`,
      this.httpOptions
    );
  }
}