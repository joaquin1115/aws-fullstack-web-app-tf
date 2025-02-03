import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { ProductoResponse } from "../models/response/producto";
import { PedidoListaResponse } from "../models/response/pedido-lista";
import { PedidoFormRequest } from "../models/request/pedido-form-request";
import { PedidoFormResponse } from "../models/response/pedido-form-response";
import { API_URL } from "../../shared/constants/urls.constant";
import { PedidoRequest } from "../models/request/pedido-request";
import { PedidoResponse } from "../models/response/pedido-response";
import { Order } from "../models/response/pedidos-response";

@Injectable({
  providedIn: 'root'
})
export class PedidosService {

  constructor(private http: HttpClient) { }
  private apiurl = API_URL.PEDIDOS;

  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json;charset=utf-8'
    })
  };

  getProductos() {
    return this.http.get<ProductoResponse[]>(`${this.apiurl}/productos`, this.httpOptions);
  }

  getPedidos() {
    return this.http.get<PedidoListaResponse[]>(`${this.apiurl}/all`, this.httpOptions);
  }

  postDatosEnvio(pedidoFormRequest: PedidoFormRequest) {
    return this.http.post<PedidoFormResponse>(`${this.apiurl}/datos`, pedidoFormRequest, this.httpOptions);
  }

  postNewPedido(pedidoRequest: PedidoRequest) {
    return this.http.post<PedidoResponse>(`${this.apiurl}/new`, pedidoRequest, this.httpOptions);
  }

  getDetallePedido(idPedido: number) {
    return this.http.get<Order>(`${this.apiurl}/details/${idPedido}`, this.httpOptions);
  }

  getDetallePedidoItems(idPedido: number) {
    return this.http.get<Order>(`${this.apiurl}/items/${idPedido}`, this.httpOptions);
  }
}
