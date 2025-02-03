import { Component, OnInit } from '@angular/core';
import { ReclamosService } from '../../../core/services/reclamos.service';
import { ActivatedRoute } from '@angular/router';
import { ReclamoVisorResponse } from '../../../core/models/response/reclamos-responses';
import { PanelModule } from 'primeng/panel';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DropdownModule } from 'primeng/dropdown';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'app-reclamos-visor',
  standalone: true,
  imports: [
    FormsModule,
    DropdownModule,
    ButtonModule,
    PanelModule,
    CommonModule
  ],
  templateUrl: './reclamos-visor.component.html',
  styleUrl: './reclamos-visor.component.scss'
})
export class ReclamosVisorComponent implements OnInit {

  public codReclamo?: Number;
  reclamo!: ReclamoVisorResponse;

  constructor(
    private route: ActivatedRoute,
    private reclamosService: ReclamosService
  ) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => this.codReclamo = params["codReclamo"]);

    this.reclamosService.getVisor(this.codReclamo as any).subscribe(visor => {
      this.reclamo = visor;
    });
  }

}
