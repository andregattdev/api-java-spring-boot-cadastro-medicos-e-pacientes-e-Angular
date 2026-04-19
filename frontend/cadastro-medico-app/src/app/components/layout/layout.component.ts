import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.css']
})
export class LayoutComponent {
  sidebarOpen = true;
  isAdmin = false;
  isPaciente = false;
  isMedico = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {
    this.isAdmin = this.authService.isAdmin();
    this.isPaciente = this.authService.isPaciente();
    this.isMedico = this.authService.isMedico();
  }

  toggleSidebar(): void {
    this.sidebarOpen = !this.sidebarOpen;
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
