import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { DoutorService } from '../../services/doutor.service';
import { PacienteService } from '../../services/paciente.service';
import { ConsultaService } from '../../services/consulta.service';
import { UsuarioService } from '../../services/usuario.service';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { Observable, of } from 'rxjs';
import { StatusConsulta } from '../../models/consulta.model';
import { isPlatformBrowser } from '@angular/common';
import { Inject, PLATFORM_ID } from '@angular/core';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  totalDoutores$: Observable<number>;
  totalPacientes$: Observable<number>;
  totalConsultas$: Observable<number>;
  totalUsuarios$: Observable<number>;
  isStaff = false;

  consultasHojeCount = 0;
  consultasSemana = 0;
  consultasMes = 0;

  consultasHojeList: any[] = [];

  constructor(
    private doutorService: DoutorService,
    private pacienteService: PacienteService,
    private consultaService: ConsultaService,
    private usuarioService: UsuarioService,
    private authService: AuthService,
    private router: Router,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {
    this.isStaff = this.authService.isStaff();
    
    if (this.authService.isPaciente()) {
      this.totalDoutores$ = of(0);
      this.totalPacientes$ = of(0);
      this.totalConsultas$ = of(0);
      this.totalUsuarios$ = of(0);
      this.router.navigate(['/consultas']);
      return;
    }
    
    if (isPlatformBrowser(this.platformId) && this.isStaff) {
      this.totalDoutores$ = this.doutorService.getCount();
      this.totalPacientes$ = this.pacienteService.getCount();
      this.totalConsultas$ = this.consultaService.getCount();
      this.totalUsuarios$ = this.usuarioService.getCount();
    } else {
      this.totalDoutores$ = of(0);
      this.totalPacientes$ = of(0);
      this.totalConsultas$ = of(0);
      this.totalUsuarios$ = of(0);
    }
  }

  ngOnInit(): void {
    if (this.authService.isPaciente()) {
      return;
    }

    if (isPlatformBrowser(this.platformId)) {
      const today = new Date().toISOString().split('T')[0];

      if (this.isStaff) {
        this.consultaService.getCountByDay(today).subscribe({
          next: count => this.consultasHojeCount = count,
          error: err => console.error('Erro getCountByDay', err)
        });

        this.consultaService.getCountByWeek('2026-04-01', '2026-04-07').subscribe({
          next: count => this.consultasSemana = count,
          error: err => console.error('Erro getCountByWeek', err)
        });

        this.consultaService.getCountByMonth(2026, 4).subscribe({
          next: count => this.consultasMes = count,
          error: err => console.error('Erro getCountByMonth', err)
        });
      }

      this.consultaService.getConsultasDoDia(today).subscribe({
        next: lista => {
          this.consultasHojeList = lista;
        },
        error: err => console.error('Erro ao buscar consultas do dia', err)
      });
    }
  }

  marcarConsulta(consulta: any, status: string): void {
    this.consultaService.atualizarStatus(consulta.id, status).subscribe({
      next: (updatedConsulta) => {
        const index = this.consultasHojeList.findIndex(c => c.id === updatedConsulta.id);
        if (index !== -1) {
          this.consultasHojeList[index].status = updatedConsulta.status;
        }
      },
      error: (err) => console.error('Erro ao atualizar status', err)
    });
  }
}
