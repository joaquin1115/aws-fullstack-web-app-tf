import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SidenavComponent } from '../shared/components/sidenav/sidenav.component';
import { RouterModule, RouterOutlet } from '@angular/router';
import { TopnavComponent } from '../shared/components/topnav/topnav.component';

interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}

@Component({
  selector: 'app-pages',
  standalone: true,
  imports: [
    RouterOutlet,
    CommonModule,
    RouterModule,
    SidenavComponent,
    TopnavComponent
  ],
  templateUrl: './pages.component.html',
  styleUrl: './pages.component.scss'
})
export class PagesComponent {

  // private sidenavToggle: SideNavToggle = { screenWidth: 0, collapsed: false };

  // constructor(private sidenavService: SidenavComponent) { }
  ngOnInit() {
    // this.sidenavService.sidenavToggle.subscribe(sidenavToggle => {
    //   this.sidenavToggle = sidenavToggle;
    // });
    this.getBodyClass();
  }
  collapsed = false;
  screenWidth = 0;

  onToggleSidenav(data: SideNavToggle): void {
    this.screenWidth = data.screenWidth;
    this.collapsed = data.collapsed;
  }

  getBodyClass(): string {
    let styleClass = '';
    if (!this.collapsed && this.screenWidth > 100) {
      styleClass = 'body-trimmed';
    } else if (!this.collapsed && this.screenWidth <= 768 && this.screenWidth > 0) {
      styleClass = 'body-md-screen';
    } else {
      styleClass = ''
    }
    return styleClass;
  }
}
