import { Injectable } from "@angular/core";
import { map, Observable, of } from "rxjs";
import { IncidenciaResponse } from "../models/response/incidencia-response";
import { IncidenciasFormRequest } from "../models/request/incidencias-form-request";
import { HttpClient, HttpHeaders } from "@angular/common/http";


@Injectable({
  providedIn: 'root'
})

export class IncidenciasService {
  private baseUrl = 'http://localhost:8080/api/control';

  constructor(private http: HttpClient) { }

  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json;charset=utf-8'
    })
  };

  getIncidencias(): Observable<IncidenciaResponse[]> {
    return this.http.get<IncidenciaResponse[]>(`${this.baseUrl}/listaIncidencia`);
  }

  actualizarEstadoIncidencia(codigoIncidencia: String, estado: String) {
    console.log(`${this.baseUrl}/actualizarEstadoIncidencia?idIncidencia=${codigoIncidencia}&idEstadoIncidencia=${estado}`)
    return this.http.get<number>(`${this.baseUrl}/actualizarEstadoIncidencia?idIncidencia=${codigoIncidencia}&idEstadoIncidencia=${estado}`, this.httpOptions);
  }

  crearIncidencia(incidencia: IncidenciasFormRequest): Observable<any> {
    const headers = new HttpHeaders().set('Content-Type', 'application/json');
    return this.http.post(`${this.baseUrl}/crearIncidencia`, incidencia, {
      headers: headers,
      responseType: 'text'
    }).pipe(
      map(response => {
        try {
          return JSON.parse(response);
        } catch (e) {
          return { message: response };
        }
      })
    );
  }
}
