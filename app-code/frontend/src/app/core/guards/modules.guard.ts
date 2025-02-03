import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { ACCESO_MODULO } from '../../shared/constants/access';

export const modulesGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  if (authService.isLogged()) {
    if (route.url.length > 0) {
      let menu: string = route.url[0].path;
      console.log((ACCESO_MODULO as any)[menu]);

      if (menu != 'pages') {
        if ((ACCESO_MODULO as any)[menu] !== undefined) {
          const acces = (ACCESO_MODULO as any)[menu];
          if (acces.area.includes(authService.getUserArea()?.toLocaleLowerCase)) {
            return true;
          } else {
            router.navigate(['pages/home']);
            return false
          }
        } else {
          router.navigate(['pages/home']);
          console.log('Esa ruta no existe');
          return false;
        }
      } else {
        return true;
      }

      // if (menu == "almacen") {
      //   if (authService.getUserRole() == 'Almacen') {
      //     return true;
      //   } else {
      //     router.navigate(['pages/home']);
      //     console.log('No tienes acceso')
      //     return false;
      //   }
      // } else {

      //   return true;
      // }
    } else {
      return true
    }
  } else {
    router.navigate(['login']);
    return false;
  }

  // if (authService.isLogged() == false) {
  //   router.navigate(['/login']);
  //   return false;
  // }
  // return true;
};