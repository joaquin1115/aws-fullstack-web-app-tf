import { HttpClientModule } from '@angular/common/http';
import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { AlmacenComponent } from "./pages/almacen/almacen.component";
import { FormsModule } from "@angular/forms";
import { MessageModule } from "primeng/message";
import { InputTextModule } from "primeng/inputtext";
import { ButtonModule } from "primeng/button";
import { MessagesModule } from "primeng/messages";
import { ToastModule } from "primeng/toast";
import { MessageService } from "primeng/api";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    HttpClientModule,
    AlmacenComponent,
    FormsModule,
    ButtonModule,
    InputTextModule,
    MessagesModule,
    MessageModule,
    ToastModule
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
  providers: [MessageService],
})
export class AppComponent {
  title = 'san-fernando-app';
}
