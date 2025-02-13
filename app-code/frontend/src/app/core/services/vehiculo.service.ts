import { Injectable } from "@angular/core";
import { VehiculoResponse } from "../models/response/vehiculo-response";
import { Observable, of } from "rxjs";
import { HttpClient } from "@angular/common/http";
import { API_URL } from "../../shared/constants/urls.constant";

@Injectable({
  providedIn: 'root'
})

export class VehiculoService {
  private apiUrl = API_URL.CONTROL;

  constructor(private http: HttpClient) { }

  getVehiculos(): Observable<VehiculoResponse[]> {
    return this.http.get<VehiculoResponse[]>(`${this.apiUrl}/listaVehiculos`);
  }

  actualizarEstado(codigoVehiculo: string, nuevoEstado: string): Observable<any> {
    return this.http.put(`${this.apiUrl}/actualizarEstadoVehiculo/${codigoVehiculo}`, `"${nuevoEstado}"`, { responseType: 'text' });
  }


}
