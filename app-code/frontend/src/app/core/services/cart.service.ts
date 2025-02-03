import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { ProductoResponse } from '../models/response/producto';
import { TicketProductoRequest } from '../models/request/ticket-producto-request';

@Injectable({
  providedIn: 'root',
})
export class CartService {
  private cartProducts: ProductoResponse[] = [];
  private _products: BehaviorSubject<ProductoResponse[]> = new BehaviorSubject<
    ProductoResponse[]
  >([]);
  constructor() {
    this._products = new BehaviorSubject<ProductoResponse[]>([]);
  }

  get products() {
    return this._products.asObservable();
  }

  addNewProduct(product: ProductoResponse) {
    const existingProductIndex = this.cartProducts.findIndex(p => p.idElementoCatalogo === product.idElementoCatalogo);
    if (existingProductIndex !== -1) {
      console.log('El producto ya está en el carrito');
    } else {
      this.cartProducts.push(product);
      this._products.next(this.cartProducts);
    }
  }

  deleteProduct(index: number) {
    this.cartProducts.splice(index, 1);
    this._products.next(this.cartProducts);
  }

  changeCantidad(cantidad: number, index: number) {
    if (index >= 0 && index < this.cartProducts.length) {
      this.cartProducts[index].cantidad = cantidad;
    } else {
      console.error('Índice fuera de rango:', index);
    }
    console.log(this.cartProducts)
  }

  getTicketProducto(): TicketProductoRequest[] {
    const ticketProductoRequests: TicketProductoRequest[] = this.cartProducts.map(
      product => (
        {
          idElementoCatalogo: product.idElementoCatalogo,
          cantidad: product.cantidad || 1
        } as TicketProductoRequest
      ));
    return ticketProductoRequests;
  }
}
