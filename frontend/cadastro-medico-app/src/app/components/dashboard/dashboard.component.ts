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
    private router: Router
  ) {
    if (this.authService.isPaciente()) {
      this.totalDoutores$ = of(0);
      this.totalPacientes$ = of(0);
      this.totalConsultas$ = of(0);
      this.totalUsuarios$ = of(0);
      this.router.navigate(['/consultas']);
      return;
    }
    
    this.totalDoutores$ = this.doutorService.getCount();
    this.totalPacientes$ = this.pacienteService.getCount();
    this.totalConsultas$ = this.consultaService.getCount();
    this.totalUsuarios$ = this.usuarioService.getCount();
  }

  ngOnInit(): void {
    if (this.authService.isPaciente()) {
      return;
    }

    const today = new Date().toISOString().split('T')[0];

    this.consultaService.getCountByDay(today).subscribe(count => {
      this.consultasHojeCount = count;
    });

    this.consultaService.getConsultasDoDia(today).subscribe(lista => {
      this.consultasHojeList = lista;
    });

    this.consultaService.getCountByWeek('2026-04-01', '2026-04-07').subscribe(count => {
      this.consultasSemana = count;
    });

    this.consultaService.getCountByMonth(2026, 4).subscribe(count => {
      this.consultasMes = count;
    });
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
