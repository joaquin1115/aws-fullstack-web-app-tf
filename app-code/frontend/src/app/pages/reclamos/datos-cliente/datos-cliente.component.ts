import { Component, OnInit } from '@angular/core';
import { DropdownModule } from "primeng/dropdown";
import { FormsModule } from "@angular/forms";
import { ButtonModule } from "primeng/button";
import { StepperModule } from "primeng/stepper";
import { AutoCompleteCompleteEvent, AutoCompleteModule } from "primeng/autocomplete";
import { InputTextModule } from "primeng/inputtext";
import { InputTextareaModule } from 'primeng/inputtextarea';
import { CalendarModule } from "primeng/calendar";
import { ReclamosService } from '../../../core/services/reclamos.service';
import { DatosClientesRequest } from '../../../core/models/request/reclamos-requests';
import { response } from 'express';
import { ReclamoEvidenciaCreate, ReclamoFormCreate, ReclamoSeguimientoFormCreate } from '../../../core/models/response/reclamos-responses';
import { formatDate } from '../../../shared/utils/dateUtil';

@Component({
  selector: 'app-datos-cliente',
  standalone: true,
  imports: [
    DropdownModule,
    FormsModule,
    ButtonModule,
    StepperModule,
    AutoCompleteModule,
    InputTextModule,
    CalendarModule,
    InputTextareaModule
  ],
  templateUrl: './datos-cliente.component.html',
  styleUrl: './datos-cliente.component.scss'
})
export class DatosClienteComponent implements OnInit {

  // Paso 1
  selectedEmpresa!: any;
  filteredEmpresas!: any[];

  selectedRepresentante: any = null;
  filteredRepresentantes: any[] = [];
  mostrarRepresentantes: boolean = true;

  filterEmpresas(event: AutoCompleteCompleteEvent) {
    let filtered: any[] = [];
    let query = event.query;
    for (let i = 0; i < (this.filteredEmpresas as any[]).length; i++) {
      let empresa = (this.filteredEmpresas as any[])[i];
      if (empresa.nombre.toLowerCase().indexOf(query.toLowerCase()) == 0) {
        filtered.push(empresa);
      }
    }
    this.filteredEmpresas = filtered;
  }

  onEmpresaChange(event: any) {
    this.reclamoService.getRepresentantes(this.selectedEmpresa.idCliente).subscribe(
      response => {
        if (response.length > 0 || response != undefined) {
          this.mostrarRepresentantes = false;
        }
        this.filteredRepresentantes = response;
      }
    );
  }

  onRepresentanteChange(event: any) {
    this.reclamoService.getTickets(this.selectedRepresentante.codRepresentante).subscribe(
      response => {
        if (response.length > 0 || response != undefined) {
          this.mostrarTickets = false;
        }
        console.log(response);
        this.filteredTickets = response;
      }
    );
  }

  filterRepresentantes(event: AutoCompleteCompleteEvent) {
    let filtered: any[] = [];
    let query = event.query;
    for (let i = 0; i < (this.filteredRepresentantes as any[]).length; i++) {
      let representante = (this.filteredRepresentantes as any[])[i];
      if (representante.nombreRepresentante.toLowerCase().indexOf(query.toLowerCase()) == 0) {
        filtered.push(representante);
      }
    }
    this.filteredRepresentantes = filtered;
  }

  // Paso 2
  selectedTicket!: any;
  filteredTickets!: any[];
  mostrarTickets: boolean = true;

  selectedProducto: any;
  filteredProductos!: any[];
  mostrarProductos: boolean = true;

  filteredProductoDetalle!: any;
  mostrarProductoDetalle: boolean = true;

  filterTickets(event: AutoCompleteCompleteEvent) {
    let filtered: any[] = [];
    let query = event.query;
    for (let i = 0; i < (this.filteredTickets as any[]).length; i++) {
      let ticket = (this.filteredTickets as any[])[i];
      if (ticket.idTicket.toString().toLowerCase().indexOf(query.toLowerCase()) == 0) {
        filtered.push(ticket);
      }
    }
    this.filteredTickets = filtered;
  }

  onTicketChange(event: any) {
    this.reclamoService.getProductos(this.selectedTicket.idTicket).subscribe(
      response => {
        if (response.length > 0 || response == undefined) {
          this.mostrarProductos = false;
        }
        this.filteredProductos = response;
      }
    );
  }

  onProductoChange(event: any) {
    console.log(this.selectedProducto);
    console.log(this.selectedTicket);
    this.reclamoService.getProductoTicket(this.selectedTicket.idTicket, this.selectedProducto.idProducto).subscribe(
      response => {
        if (response.length > 0 || response == undefined) {
          this.mostrarProductoDetalle = false;
        }
        console.log(response);
        this.filteredProductoDetalle = response;
      }
    );
  }

  filterProductos(event: AutoCompleteCompleteEvent) {
    let filtered: any[] = [];
    let query = event.query;
    for (let i = 0; i < (this.filteredProductos as any[]).length; i++) {
      let producto = (this.filteredProductos as any[])[i];
      if (producto.nombre.toString().toLowerCase().indexOf(query.toLowerCase()) == 0) {
        filtered.push(producto);
      }
    }
    this.filteredProductos = filtered;
  }

  // Paso 3


  ngOnInit() {
    // Initialize dropdown options
    this.tipoReclamoOptions = [
      { "label": "Productos en mal estado o de calidad inferior", "value": "A" },
      { "label": "Cantidad incorrecta de productos recibidos", "value": "B" },
      { "label": "Productos incorrectos o diferentes a los solicitados", "value": "C" },
      { "label": "Embalaje inadecuado que afecta la frescura o la calidad de los productos", "value": "D" },
      { "label": "Retraso en la entrega del pedido", "value": "E" },
      { "label": "Problemas con la facturación o el proceso de pago", "value": "F" },
      { "label": "Problemas relacionados con la limpieza, higiene o seguridad de los productos", "value": "G" }
    ]
    this.urgenciaOptions = [
      { label: 'Alta', value: 'A' },
      { label: 'Media', value: 'M' },
      { label: 'Baja', value: 'B' }
    ];
    this.filteredAreas = [
      { "label": "Mantenimiento", "value": 1 },
      { "label": "Producción", "value": 2 },
      { "label": "Lavanderia", "value": 3 },
      { "label": "Camara", "value": 4 },
      { "label": "Recursos Humanos", "value": 5 },
      { "label": "Almacen", "value": 6 }
    ];
    this.accionesSolicitadas = [
      { "label": "Investigación", "value": "I" },
      { "label": "Evaluación", "value": "E" },
      { "label": "Resarcimiento", "value": "R" },
      { "label": "Corrección", "value": "C" }
    ]
    this.estadosReclamo = [
      { "label": "Pendiente", "value": "A" },
      { "label": "En Proceso", "value": "B" },
      { "label": "Resuelto", "value": "C" },
      { "label": "Rechazado", "value": "D" }
    ];
    this.tipoEvidenciaOptions = [
      { "label": "Captura Pantalla", "value": "C" },
      { "label": "Documento", "value": "D" },
      { "label": "Escaneo", "value": "E" },
      { "label": "Fotografía", "value": "F" },
      { "label": "Grabación de audio", "value": "G" },
      { "label": "Testimonio escrito", "value": "T" },
      { "label": "Video", "value": "V" }
    ]
    this.tipoArchivoOptions = [
      { "label": "AAC", "value": 1 },
      { "label": "AVI", "value": 2 },
      { "label": "BMP", "value": 3 },
      { "label": "CSV", "value": 4 },
      { "label": "DOCX", "value": 5 },
      { "label": "FLAC", "value": 6 },
      { "label": "GIF", "value": 7 },
      { "label": "JPG", "value": 8 },
      { "label": "MKV", "value": 9 },
      { "label": "MOV", "value": 10 },
      { "label": "MP3", "value": 11 },
      { "label": "MP4", "value": 12 },
      { "label": "OGG", "value": 13 },
      { "label": "PDF", "value": 14 },
      { "label": "PNG", "value": 15 },
      { "label": "PPTX", "value": 16 },
      { "label": "RTF", "value": 17 },
      { "label": "TIFF", "value": 18 },
      { "label": "TXT", "value": 19 },
      { "label": "WAV", "value": 20 },
      { "label": "WMV", "value": 21 },
      { "label": "XLSX", "value": 22 }
    ]

    this.reclamoService.getEmpresas().subscribe(response => {
      console.log(response.map(item => item.nombre));
      this.filteredEmpresas = response
    })

  }

  fechaAdquisicion!: Date;
  nroLote: number = 0;
  cantidadAdquirida: string = '';
  naturalezaReclamo: any = {
    tipoReclamo: '',
    descripcionProblema: '',
    fechaIncidencia: Date,
    urgencia: ''
  };
  evidenciaReclamo: any = {
    nombreEvidencia: '',
    tipoEvidencia: '',
    tipoArchivo: ''
  };
  resolucionReclamo: any = {
    areaResponsable: '',
    accionSolicitada: '',
    comentario: ''
  };
  confirmacionSeguimiento: any = {
    fechaEsperada: Date,
    nroCaso: '',
    estadoReclamo: ''
  };

  tipoEvidenciaOptions: any[] = [];
  tipoArchivoOptions: any[] = [];

  tipoReclamoOptions: any[] = [];
  urgenciaOptions: any[] = [];
  filteredAreas: any[] = [];
  accionesSolicitadas: any[] = [];
  estadosReclamo: any[] = [];

  representantesStringList: string[] = [];
  constructor(private reclamoService: ReclamosService) { }






  onEmpresaClear() {
    // Clear empresa selection
  }

  onEmpresaInput(event: any) {
    // Handle empresa input
  }



  transformToStringList(data: any[]): string[] {
    return data.map(item => `${item.codRepresentante} - ${item.nombreRepresentante}`);
  }


  onRepresentanteClear() {
    // Clear representante selection
  }


  filterAreaResponsable(event: any) {
    // Implement area responsable filtering logic
  }

  submitReclamo() {
    const requestReclamo: ReclamoFormCreate = {
      codRepresentante: this.selectedRepresentante.codRepresentante,
      codPedido: this.selectedTicket.idTicket,
      codSeguimiento: 1, // AGREGAR
      codTipoReclamo: this.naturalezaReclamo.tipoReclamo,
      codNivelUrgencia: this.naturalezaReclamo.urgencia,
      codEstadoReclamo: this.confirmacionSeguimiento.estadoReclamo,
      comentario: this.naturalezaReclamo.comentario,
      fechaSuceso: formatDate(this.naturalezaReclamo.fechaIncidencia),
      fechaReclamo: formatDate(new Date()),
      fechaEsperada: formatDate(this.confirmacionSeguimiento.fechaEsperada),
      nroCaso: this.confirmacionSeguimiento.nroCaso
    }
    const requestSeguimiento: ReclamoSeguimientoFormCreate = {
      codClienteInterno: this.selectedEmpresa.idCliente,
      codTipoAccion: this.resolucionReclamo.accionSolicitada,
      fechaResolucion: formatDate(new Date()),
      nroCaso: this.confirmacionSeguimiento.nroCaso,
      comentario: this.resolucionReclamo.comentario
    }
    const requestEvidencia: ReclamoEvidenciaCreate = {
      nombreEvidencia: this.evidenciaReclamo.nombreEvidencia,
      tipoEvidencia: this.evidenciaReclamo.tipoEvidencia,
      tipoArchivo: this.evidenciaReclamo.tipoArchivo
    }

    console.log(requestReclamo);
    console.log(requestSeguimiento);
    // this.reclamoService.submitReclamo(request).subscribe(
    //   response => {
    //     console.log('Reclamo creado exitosamente', response);

    //   },
    //   error => {
    //     console.error('Error al crear el reclamo', error);
    //   }
    // );
  }
}
