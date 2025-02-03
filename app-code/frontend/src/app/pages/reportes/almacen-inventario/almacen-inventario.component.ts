import { Component, OnInit } from '@angular/core';
import { ReporteAlmacenStockResponse } from '../../../core/models/response/reporte-responses';
import { Router } from '@angular/router';
import { ReportesService } from '../../../core/services/reportes.service';
import { FormBuilder, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { TableModule } from 'primeng/table';

@Component({
  selector: 'app-almacen-inventario',
  standalone: true,
  imports: [
    TableModule
  ],
  templateUrl: './almacen-inventario.component.html',
  styleUrl: './almacen-inventario.component.scss'
})
export class AlmacenInventarioComponent implements OnInit {
  dataStockAlmacenView: boolean = false;
  products: ReporteAlmacenStockResponse[] = [];
  product!: ReporteAlmacenStockResponse;
  size = { name: 'Small', class: 'p-datatable-sm' };

  constructor(
    private router: Router,
    private reportesService: ReportesService,
    private formBuilder: FormBuilder,
  ) { }

  ngOnInit(): void {
    this.reportesService.getReporteAlmacenStock().subscribe((response) => {
      this.products = response;
      this.dataStockAlmacenView = true;
    })
  }
}
