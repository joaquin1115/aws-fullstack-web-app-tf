import { Component, OnInit, inject } from '@angular/core';
import { AuthService } from '../../../core/services/auth.service';

interface userInfo {
  dni: string;
  area: string;
  cargo: string;
  idEmpleado: string;
}

@Component({
  selector: 'app-topnav',
  standalone: true,
  imports: [],
  templateUrl: './topnav.component.html',
  styleUrl: './topnav.component.scss'
})
export class TopnavComponent implements OnInit {

  authService = inject(AuthService);
  userInfo: userInfo = { dni: '', area: '', cargo: '', idEmpleado: '' };
  userInfoView: boolean = false;

  ngOnInit(): void {
    this.authService.currentUser$.subscribe(user => {
      if (user) {
        this.userInfo.idEmpleado = user.employeeId || '';
        this.userInfo.area = user.area || '';
        this.userInfo.cargo = user.position || '';
        this.userInfoView = true;
      }
    });
  }


}
