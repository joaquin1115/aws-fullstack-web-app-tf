import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';

import { AlmacenService } from "../../core/services/almacen.service";
import { CommonModule } from "@angular/common";
import { FormsModule } from "@angular/forms";
import { ButtonModule } from "primeng/button";
import { InputTextModule } from "primeng/inputtext";
import { MessagesModule } from "primeng/messages";
import { MessageModule } from "primeng/message";
import { TableModule } from "primeng/table";
import { RadioButtonModule } from "primeng/radiobutton";

@Component({
  selector: 'app-almacen',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule
  ],
  templateUrl: './almacen.component.html',
  styleUrl: './almacen.component.scss'
})
export class AlmacenComponent implements OnInit {

  constructor() { }
  ngOnInit(): void {
  }

}
