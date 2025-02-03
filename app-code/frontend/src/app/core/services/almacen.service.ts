import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { BusquedaAlmacenResponse } from '../models/response/almacen-response';

@Injectable({
  providedIn: 'root'
})
export class AlmacenService {
  private apiUrl = 'http://localhost:8080/api/almacen';

  constructor(private http: HttpClient) { }

  validarNroPrecinto(nroPrecinto: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/validarNroPrecinto`, { params: { nroPrecinto } });
  }

  validarCodGuiaRemision(codGuiaRemision: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/validarCodGuiaRemision`, { params: { codGuiaRemision } });
  }

  buscarPorNroPrecinto(nroPrecinto: string): Observable<BusquedaAlmacenResponse> {
    return this.http.get<BusquedaAlmacenResponse>(`${this.apiUrl}/buscarPorNroPrecinto`, { params: { nroPrecinto } });
  }

  buscarPorCodGuiaRemision(codGuiaRemision: string): Observable<BusquedaAlmacenResponse> {
    return this.http.get<BusquedaAlmacenResponse>(`${this.apiUrl}/buscarPorCodGuiaRemision`, { params: { codGuiaRemision } });
  }

  validarEmpleado(dni: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/validarEmpleado`, { params: { dni } });
  }

  validarRuta(codRuta: number): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/validarRuta`, { params: { codRuta } });
  }

  validarVehiculo(placa: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/validarVehiculo`, { params: { placa } });
  }

  validarTransportista(dni: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/validarTransportista`, { params: { dni } });
  }

  validarPedido(codPedido: number): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/validarPedido`, { params: { codPedido } });
  }

  registrarOperacion(operacionData: any): Observable<number> {
    return this.http.post<number>(`${this.apiUrl}/registrarOperacion`, operacionData);
  }

  registrarMercancias(mercanciaData: any): Observable<string[]> {
    return this.http.post<string[]>(`${this.apiUrl}/registrarMercancias`, mercanciaData);
  }

  registrarTraslado(trasladoData: any): Observable<string> {
    return this.http.post(`${this.apiUrl}/registrarTraslado`, trasladoData, { responseType: 'text' });
  }

  obtenerStock(codStock: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/obtenerStock`, { params: { codStock } });
  }
}
