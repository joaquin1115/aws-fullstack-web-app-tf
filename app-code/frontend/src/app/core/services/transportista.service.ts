import { Injectable } from "@angular/core";
import { TransportistaListaResponse } from "../models/response/transportista-lista";
import { Observable, of } from "rxjs";
import { HttpClient } from "@angular/common/http";


@Injectable({
  providedIn: 'root'
})
export class TransportistaService {
  private apiUrl = 'http://localhost:8080/api/control';

  constructor(private http: HttpClient) { }


  getConductores(): Observable<TransportistaListaResponse[]> {
    return this.http.get<TransportistaListaResponse[]>(`${this.apiUrl}/listaConductores`);
  }

  actualizarEstadoConductor(cod_conductor: string, estado: string): Observable<any> {
    return this.http.put(`${this.apiUrl}/actualizar-estado/${cod_conductor}`, estado, { responseType: 'text' });
  }

}
