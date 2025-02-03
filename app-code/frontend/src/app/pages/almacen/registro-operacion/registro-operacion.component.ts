import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { jsPDF } from 'jspdf';
import autoTable from 'jspdf-autotable';
import { TableModule } from "primeng/table";
import { DialogModule } from "primeng/dialog";
import { CardModule } from "primeng/card";
import { MessageService } from "primeng/api";
import { AlmacenService } from "../../../core/services/almacen.service";
import { forkJoin, map, Observable, of, switchMap, tap } from "rxjs";
import { CommonModule } from "@angular/common";
import { ButtonModule } from "primeng/button";
import { InputTextModule } from "primeng/inputtext";
import { ToastModule } from "primeng/toast";

@Component({
  selector: 'app-registro-operacion',
  standalone: true,
  imports: [
    CommonModule,
    ButtonModule,
    InputTextModule,
    FormsModule,
    RouterModule,
    ReactiveFormsModule,
    TableModule,
    DialogModule,
    CardModule,
    ToastModule
  ],
  templateUrl: './registro-operacion.component.html',
  styleUrl: './registro-operacion.component.scss',
  providers: [MessageService, AlmacenService]
})
export class RegistroOperacionComponent {
  registroForm: FormGroup;
  titulo: string = '';
  codTipoOperacion!: number;
  mercancias: any[] = [];
  pedidos: number[] = [];
  mostrarModal: boolean = false;
  codStock: string = '';
  stockEncontrado: any;
  cantidad!: number;
  mensajeError: string = '';
  mostrarModalPedido = false;
  codPedidoStr = '';
  mostrarResultado: boolean = false;
  mensajeResultado: string = '';
  idPicking!: number;
  mercanciaIndex: number = -1;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient,
    private messageService: MessageService,
    private almacenService: AlmacenService
  ) {
    this.registroForm = this.fb.group({
      fecha: [new Date().toISOString().substring(0, 10), Validators.required],
      horaInicio: ['', Validators.required],
      horaFin: ['', Validators.required],
      dniEjecutor: ['', Validators.required],
      dniSupervisor: ['', Validators.required],
      codRuta: [''],
      placaVehiculo: [''],
      dniTransportista: ['']
    });
  }

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.codTipoOperacion = +params['codTipoOperacion'];
      this.titulo = this.getTitulo(this.codTipoOperacion);
      this.idPicking = +params['idPicking'];
      this.mercancias = [];
      this.pedidos = [];
      this.mensajeResultado = '';

      this.registroForm.reset({
        fecha: new Date().toISOString().substring(0, 10),
        horaInicio: '',
        horaFin: '',
        dniEjecutor: '',
        dniSupervisor: '',
        codRuta: '',
        placaVehiculo: '',
        dniTransportista: ''
      });

      this.mostrarResultado = false;
    });
  }

  getTitulo(codTipoOperacion: number): string {
    switch (codTipoOperacion) {
      case 1: return 'Registro de picking';
      case 2: return 'Registro de precintado';
      case 3: return 'Registro de paletizado';
      case 4: return 'Registro de carga';
      case 5: return 'Registro de salida';
      case 6: return 'Registro de recepción y verificación';
      case 7: return 'Registro de descarga';
      default: return '';
    }
  }

  abrirModal() {
    this.mostrarModal = true;
  }

  abrirModalParaMercancia(index: number) {
    this.mostrarModal = true;
    this.mercanciaIndex = index;
  }

  cerrarModal() {
    this.mostrarModal = false;
    this.codStock = '';
    this.stockEncontrado = null;
    this.cantidad = 0;
    this.mensajeError = '';
    this.mercanciaIndex = -1;
  }

  onSubmit() {
    const formValue = this.registroForm.value;
    this.validarFormulario(formValue).subscribe(
      isValid => {
        if (isValid) {
          const operacionData = {
            idPicking: this.idPicking,
            dniEjecutor: formValue.dniEjecutor,
            dniSupervisor: formValue.dniSupervisor,
            codTipoOperacion: this.codTipoOperacion,
            fecha: formValue.fecha,
            horaInicio: formValue.horaInicio,
            horaFin: formValue.horaFin
          };
          this.registrarOperacion(operacionData, formValue);
          this.messageService.add({ severity: 'success', summary: 'Éxito', detail: 'Operación registrada exitosamente.' });
          this.mostrarResultado = true;
        }
      },
      error => {
        console.error('Error:', error);
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error al validar el formulario' });
      }
    );
  }

  validarFormulario(formValue: any): Observable<boolean> {
    return this.validarEmpleados(formValue.dniEjecutor, formValue.dniSupervisor).pipe(
      switchMap(empleadosValidos => {
        if (this.codTipoOperacion === 5) {
          return this.validarTraslado(formValue, empleadosValidos);
        } else if (this.codTipoOperacion === 1) {
          return this.validarMercancias().pipe(
            map(mercanciasValidas => empleadosValidos && mercanciasValidas)
          );
        } else {
          return of(empleadosValidos);
        }
      })
    );
  }

  validarMercancias(): Observable<boolean> {
    if (this.mercancias.length === 0) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Debe ingresar al menos una mercancía' });
      return of(false);
    }
    return of(true);
  }

  validarEmpleados(dniEjecutor: string, dniSupervisor: string): Observable<boolean> {
    return forkJoin([
      this.almacenService.validarEmpleado(dniEjecutor),
      this.almacenService.validarEmpleado(dniSupervisor)
    ]).pipe(
      map(([validEjecutor, validSupervisor]) => {
        if (!validEjecutor) {
          this.messageService.add({ severity: 'error', summary: 'Error', detail: 'DNI del ejecutor no es válido' });
        }
        if (!validSupervisor) {
          this.messageService.add({ severity: 'error', summary: 'Error', detail: 'DNI del supervisor no es válido' });
        }
        return validEjecutor && validSupervisor;
      })
    );
  }

  validarTraslado(formValue: any, empleadosValidos: boolean): Observable<boolean> {
    const validacionPedidos = this.validarPedidos();
    const validacionRuta = this.almacenService.validarRuta(formValue.codRuta).pipe(
      tap(validRuta => {
        if (!validRuta) {
          this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Código de ruta no es válido' });
        }
      }),
      map(validRuta => empleadosValidos && validRuta)
    );

    const codRuta = formValue.codRuta;
    if (!/^\d+$/.test(codRuta) || parseInt(codRuta, 10) <= 0) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Código de ruta no válido. Debe ser un entero positivo.' });
      return of(false);
    }

    const validacionVehiculo = this.almacenService.validarVehiculo(formValue.placaVehiculo).pipe(
      tap(validVehiculo => {
        if (!validVehiculo) {
          this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Placa de vehículo no es válida' });
        }
      }),
      map(validVehiculo => empleadosValidos && validVehiculo)
    );
    const validacionTransportista = this.almacenService.validarTransportista(formValue.dniTransportista).pipe(
      tap(validTransportista => {
        if (!validTransportista) {
          this.messageService.add({ severity: 'error', summary: 'Error', detail: 'DNI del transportista no es válido' });
        }
      }),
      map(validTransportista => empleadosValidos && validTransportista)
    );

    return forkJoin([validacionPedidos, validacionRuta, validacionVehiculo, validacionTransportista]).pipe(
      map(([pedidosValidos, rutaValida, vehiculoValido, transportistaValido]) => {
        return pedidosValidos && rutaValida && vehiculoValido && transportistaValido;
      })
    );
  }

  validarPedidos(): Observable<boolean> {
    if (this.pedidos.length === 0) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Debe ingresar al menos un código de pedido' });
      return of(false);
    }
    const validaciones = this.pedidos.map(codPedido => this.almacenService.validarPedido(codPedido));
    return forkJoin(validaciones).pipe(
      map(results => {
        const pedidosInvalidos = results.some(valid => !valid);
        if (pedidosInvalidos) {
          this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Uno o más códigos de pedido no son válidos' });
        }
        return !pedidosInvalidos;
      })
    );
  }

  registrarOperacion(operacionData: any, formValue: any) {
    this.almacenService.registrarOperacion(operacionData).subscribe(
      idOperacion => {
        this.mensajeResultado += 'Operación registrada con éxito. ';
        if (this.codTipoOperacion === 1) {
          this.idPicking = idOperacion;
          this.registrarMercancias();
        } else if (this.codTipoOperacion === 5) {
          this.registrarTraslado(idOperacion, formValue);
        }
      },
      error => {
        console.error('Error:', error);
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error al registrar la operación' });
      }
    );
  }

  registrarMercancias() {
    const mercanciaData = {
      idOperacionPicking: this.idPicking,
      mercancias: this.mercancias.map(mercancia => ({
        detalles: mercancia.detalles.map((detalle: any) => ({
          idStock: detalle.idStock,
          cantidad: detalle.cantidad
        }))
      }))
    };

    this.almacenService.registrarMercancias(mercanciaData).subscribe(
      (nroPrecintos: string[]) => {
        nroPrecintos.forEach((nroPrecinto, index) => {
          if (this.mercancias[index]) {
            this.mercancias[index].nroPrecinto = nroPrecinto;
          }
        });
        this.exportarPDF();
        this.mensajeResultado += 'Se generaron los números de precinto de las mercancías. ';
      },
      error => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Ocurrió un error al registrar las mercancías.' });
      }
    );
  }

  registrarTraslado(idOperacion: number, formValue: any) {
    const trasladoData = {
      placaVehiculo: formValue.placaVehiculo,
      codRuta: parseInt(formValue.codRuta, 10),
      dniTransportista: formValue.dniTransportista,
      idOperacionInicia: idOperacion,
      codPedidos: this.pedidos
    };
    this.almacenService.registrarTraslado(trasladoData).subscribe(
      codGuiaRemision => {
        this.mensajeResultado += `Traslado registrado con Guía de Remisión ${codGuiaRemision} `;
      },
      error => {
        console.error('Error al registrar traslado:', error);
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error al registrar información de traslado' });
      }
    );
  }

  buscarStock() {
    if (!/^\d{5}-\d{5}-\d{5}$/.test(this.codStock)) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Formato inválido' });
      return;
    }

    this.almacenService.obtenerStock(this.codStock).subscribe(
      data => {
        this.stockEncontrado = data;
        this.messageService.clear();
      },
      error => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Código no encontrado' });
      }
    );
  }

  agregarCodStock() {
    if (!this.cantidad || this.cantidad <= 0) {
      this.mensajeError = 'Cantidad inválida';
      return;
    }

    const detalle = {
      idStock: this.stockEncontrado.id,
      codStock: this.codStock,
      nombre: this.stockEncontrado.nombre,
      categoria: this.stockEncontrado.categoria,
      tipo: this.stockEncontrado.tipo,
      segmento: this.stockEncontrado.segmento,
      unidad: this.stockEncontrado.unidad,
      cantidad: this.cantidad
    };

    if (this.mercanciaIndex !== -1) {
      const mercancia = this.mercancias[this.mercanciaIndex];
      const detalleIndex = mercancia.detalles.findIndex((d: any) => d.idStock === detalle.idStock);
      if (detalleIndex !== -1) {
        mercancia.detalles[detalleIndex].cantidad += this.cantidad;
      } else {
        mercancia.detalles.push(detalle);
      }
    } else {
      this.mercancias.push({ detalles: [detalle] });
    }

    this.cerrarModal();
  }


  quitarCodStock(index: number, codStock: string) {
    this.mercancias[index].detalles = this.mercancias[index].detalles.filter((detalle: any) => detalle.codStock !== codStock);
    if (this.mercancias[index].detalles.length === 0) {
      this.mercancias.splice(index, 1);
    }
  }

  agregarPedido() {
    const codPedido = parseInt(this.codPedidoStr, 10);
    if (!isNaN(codPedido)) {
      this.almacenService.validarPedido(codPedido).subscribe(
        (exists: boolean) => {
          if (exists) {
            this.pedidos.push(codPedido);
            this.mostrarModalPedido = false;
            this.codPedidoStr = '';
            this.mensajeError = '';
          } else {
            this.mensajeError = 'Código de pedido no encontrado';
          }
        }
      );
    } else {
      this.mensajeError = 'Por favor, ingrese un código de pedido válido.';
    }
  }

  quitarPedido(index: number) {
    this.pedidos.splice(index, 1);
  }

  exportarPDF() {
    const doc = new jsPDF();
    let currentY = 10;

    this.mercancias.forEach((mercancia, mercanciaIndex) => {
      if (mercanciaIndex > 0) {
        doc.addPage();
        currentY = 10;
      }

      doc.text(`Nro Precinto: ${mercancia.nroPrecinto}`, 10, currentY);
      currentY += 10;

      autoTable(doc, {
        startY: currentY,
        head: [['Cod Stock', 'Nombre', 'Categoría', 'Tipo', 'Segmento', 'Unidad', 'Cantidad']],
        body: mercancia.detalles.map((detalle: any) => [
          detalle.codStock,
          detalle.nombre,
          detalle.categoria,
          detalle.tipo,
          detalle.segmento,
          detalle.unidad,
          detalle.cantidad.toString()
        ])
      });

      currentY = (doc as any).lastAutoTable.finalY + 10;
    });

    doc.save('mercancias.pdf');
    this.messageService.add({ severity: 'success', summary: 'Éxito', detail: 'Números de precinto exportados en PDF.' });
  }
  volverVistaProcesos() {
    this.router.navigate(['pages/almacen/vista-procesos']);
  }

  continuarRegistroAdicional() {
    this.router.navigate(['pages/almacen/registro-operacion'], { queryParams: { idPicking: this.idPicking, codTipoOperacion: this.codTipoOperacion + 1 } });
  }
}
