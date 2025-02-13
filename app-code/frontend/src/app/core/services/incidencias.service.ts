import { Injectable } from "@angular/core";
import { map, Observable, of } from "rxjs";
import { IncidenciaResponse } from "../models/response/incidencia-response";
import { IncidenciasFormRequest } from "../models/request/incidencias-form-request";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { API_URL } from "../../shared/constants/urls.constant";

@Injectable({
  providedIn: 'root'
})

export class IncidenciasService {
  private apiUrl = API_URL.INCIDENCIAS;

  constructor(private http: HttpClient) { }

  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json;charset=utf-8'
    })
  };

  getIncidencias(): Observable<IncidenciaResponse[]> {
    return this.http.get<IncidenciaResponse[]>(`${this.apiUrl}/listaIncidencia`);
  }

  actualizarEstadoIncidencia(codigoIncidencia: String, estado: String) {
    console.log(`${this.apiUrl}/actualizarEstadoIncidencia?idIncidencia=${codigoIncidencia}&idEstadoIncidencia=${estado}`)
    return this.http.get<number>(`${this.apiUrl}/actualizarEstadoIncidencia?idIncidencia=${codigoIncidencia}&idEstadoIncidencia=${estado}`, this.httpOptions);
  }

  crearIncidencia(incidencia: IncidenciasFormRequest): Observable<any> {
    const headers = new HttpHeaders().set('Content-Type', 'application/json');
    return this.http.post(`${this.apiUrl}/crearIncidencia`, incidencia, {
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
