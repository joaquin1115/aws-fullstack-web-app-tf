// import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { API_URL } from "../../shared/constants/urls.constant";
import { ReclamoListaResponse, ReclamoVisorResponse } from "../models/response/reclamos-responses";
import { DatosClientesRequest } from "../models/request/reclamos-requests";
import { Observable, of } from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ReclamosService {

  private apiUrl = 'http://localhost:8080/api/reclamos';
  constructor(private http: HttpClient) { }

  getReclamos(): Observable<ReclamoListaResponse[]> {
    // return of([
    //   new ReclamoListaResponse('1', 'Pendiente', '2021-06-01', '123456789', 'Interno', 'Producto', 'Atención al Cliente'),
    //   new ReclamoListaResponse('2', 'En Proceso', '2021-06-02', '987654321', 'Externo', 'Servicio', 'Soporte Técnico'),
    //   new ReclamoListaResponse('3', 'Resuelto', '2021-06-03', '123456789', 'Interno', 'Producto', 'Atención al Cliente'),
    //   new ReclamoListaResponse('4', 'Cerrado', '2021-06-04', '987654321', 'Externo', 'Servicio', 'Soporte Técnico')
    // ]);
    return this.http.get<ReclamoListaResponse[]>(`${this.apiUrl}/lista`);
  }

  getEmpresas(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/empresas`);
  }

  getRepresentantes(empresaId: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/representantes?empresaId=${empresaId}`);
  }

  submitReclamo(request: DatosClientesRequest): Observable<any> {
    return this.http.post(`${this.apiUrl}/crear`, request);
  }

  // You can add more methods here for other API calls, such as:


  getTickets(idRepresentante: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/tickets?idRepresentante=${idRepresentante}`);
  }

  getProductos(idTicket: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/productos?idTicket=${idTicket}`);
  }

  getProductoTicket(idTicket: number, idProducto: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/productoDetalle?idTicket=${idTicket}&idProducto=${idProducto}`);
  }

  getVisor(codReclamo: string): Observable<ReclamoVisorResponse> {
    return this.http.get<ReclamoVisorResponse>(`${this.apiUrl}/visor?idReclamo=${codReclamo}`);
  }
}

