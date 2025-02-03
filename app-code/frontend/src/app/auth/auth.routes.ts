import { Routes } from "@angular/router";
import { LoginComponent } from "./login/login.component";
import { authGuard } from "../core/guards/auth.guard";
import { loginGuard } from "../core/guards/login.guard";

export const AUTH_ROUTES: Routes = [
  { path: 'login', component: LoginComponent, canActivate: [loginGuard] }
];