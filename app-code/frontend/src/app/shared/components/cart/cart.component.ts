import { Component } from '@angular/core';
import { ProductoResponse } from '../../../core/models/response/producto';
import { CartService } from '../../../core/services/cart.service';
import { InputNumberModule } from 'primeng/inputnumber';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [
    CommonModule,
    InputNumberModule,
    FormsModule
  ],
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.scss'
})
export class CartComponent {
  products: ProductoResponse[] = [];
  // public cantidad?: 1;
  public cantidades: number[] = [];
  constructor(private cartService: CartService) { }

  ngOnInit() {
    this.cartService.products.subscribe(products => {
      this.products = products;
      this.cantidades = this.products.map(product => product.cantidad ?? 1);
    });
  }
  onClickDelete(index: number) {
    this.cartService.deleteProduct(index);
  }
  onChangeCantidad(event: any, index: number) {
    this.cartService.changeCantidad(event, index);
  }
}
