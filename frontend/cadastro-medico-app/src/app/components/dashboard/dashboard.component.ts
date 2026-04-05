import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { DoutorService } from '../../services/doutor.service';
import { PacienteService } from '../../services/paciente.service';
import { ConsultaService } from '../../services/consulta.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  sidebarOpen = true;

  // expostos como Observable para usar com async pipe
  totalDoutores$: Observable<number>;
  totalPacientes$: Observable<number>;
  totalConsultas$: Observable<number>;

  // contagem de consultas
  consultasHojeCount = 0;
  consultasSemana = 0;
  consultasMes = 0;

  // lista de consultas do dia (para fila de cards)
  consultasHojeList: any[] = [];

  constructor(
    private authService: AuthService,
    private router: Router,
    private doutorService: DoutorService,
    private pacienteService: PacienteService,
    private consultaService: ConsultaService
  ) {
    this.totalDoutores$ = this.doutorService.getCount();
    this.totalPacientes$ = this.pacienteService.getCount();
    this.totalConsultas$ = this.consultaService.getCount();
  }

  ngOnInit(): void {
    const today = new Date().toISOString().split('T')[0]; // yyyy-MM-dd

    // quantidade de consultas hoje
    this.consultaService.getCountByDay(today).subscribe(count => {
      this.consultasHojeCount = count;
    });

    // lista de consultas hoje (fila de cards)
    this.consultaService.getConsultasDoDia(today).subscribe(lista => {
      this.consultasHojeList = lista;
    });

    // consultas na semana (exemplo fixo, pode ser dinâmico)
    this.consultaService.getCountByWeek('2026-04-01', '2026-04-07').subscribe(count => {
      this.consultasSemana = count;
    });

    // consultas no mês
    this.consultaService.getCountByMonth(2026, 4).subscribe(count => {
      this.consultasMes = count;
    });
  }

  toggleSidebar(): void {
    this.sidebarOpen = !this.sidebarOpen;
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
