import { Injectable } from "@angular/core";
import { VehiculoResponse } from "../models/response/vehiculo-response";
import { Observable, of } from "rxjs";
import { HttpClient } from "@angular/common/http";


@Injectable({
  providedIn: 'root'
})

export class VehiculoService {
  private baseUrl = 'http://localhost:8080/api/control'; // Ajusta la URL base según tu configuración

  constructor(private http: HttpClient) { }

  getVehiculos(): Observable<VehiculoResponse[]> {
    return this.http.get<VehiculoResponse[]>(`${this.baseUrl}/listaVehiculos`);
  }

  actualizarEstado(codigoVehiculo: string, nuevoEstado: string): Observable<any> {
    return this.http.put(`${this.baseUrl}/actualizarEstadoVehiculo/${codigoVehiculo}`, `"${nuevoEstado}"`, { responseType: 'text' });
  }


}
