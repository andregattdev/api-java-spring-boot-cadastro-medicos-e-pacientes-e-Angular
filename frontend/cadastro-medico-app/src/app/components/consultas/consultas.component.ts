import { Component, OnInit, Inject, PLATFORM_ID, ChangeDetectorRef } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ConsultaService } from '../../services/consulta.service';
import { ConsultaDTO } from '../../models/consulta.model';
import { DoutorService } from '../../services/doutor.service';
import { PacienteService } from '../../services/paciente.service';
import { DoutorDTO } from '../../models/doutor.model';
import { PacienteDTO } from '../../models/paciente.model';

@Component({
  selector: 'app-consultas',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './consultas.component.html',
  styleUrl: './consultas.component.css'
})
export class ConsultasComponent implements OnInit {
  consultas: ConsultaDTO[] = [];
  doutores: DoutorDTO[] = [];
  pacientes: PacienteDTO[] = [];
  displayModal = false;
  consultaForm: FormGroup;
  editando = false;
  carregando = true;

  constructor(
    private consultaService: ConsultaService,
    private doutorService: DoutorService,
    private pacienteService: PacienteService,
    private formBuilder: FormBuilder,
    @Inject(PLATFORM_ID) private platformId: Object,
    private cdr: ChangeDetectorRef
  ) {
    this.consultaForm = this.formBuilder.group({
      id: [''],
      data: ['', [Validators.required]],
      hora: ['', [Validators.required]],
      doutorId: ['', [Validators.required]],
      pacienteId: ['', [Validators.required]],
      observacoes: ['']
    });
  }

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.carregarDados();
    } else {
      this.carregando = false;
    }
  }

  carregarDados(): void {
    this.carregando = true;
    
    // Carrega Consultas
    this.consultaService.listarConsultas().subscribe({
      next: (data) => {
        this.consultas = data;
        this.carregando = false;
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('Erro ao carregar consultas:', error);
        this.carregando = false;
        this.cdr.detectChanges();
      }
    });

    // Carrega Doutores para os Dropdowns
    this.doutorService.listarDoutores().subscribe({
      next: (data) => {
        this.doutores = data;
        this.cdr.detectChanges();
      }
    });

    // Carrega Pacientes para os Dropdowns
    this.pacienteService.listarPacientes().subscribe({
      next: (data) => {
        this.pacientes = data;
        this.cdr.detectChanges();
      }
    });
  }

  abrirModal(consulta?: ConsultaDTO): void {
    if (consulta) {
      this.editando = true;
      let d = '';
      let h = '';
      if (consulta.dataHora) {
        d = consulta.dataHora.split('T')[0];
        h = consulta.dataHora.split('T')[1]?.substring(0, 5) || '';
      }
      this.consultaForm.patchValue({
        id: consulta.id,
        data: d,
        hora: h,
        doutorId: consulta.doutorId,
        pacienteId: consulta.pacienteId,
        observacoes: consulta.observacoes
      });
    } else {
      this.editando = false;
      this.consultaForm.reset();
    }
    this.displayModal = true;
  }

  fecharModal(): void {
    this.displayModal = false;
    this.consultaForm.reset();
  }

  salvarConsulta(): void {
    if (this.consultaForm.invalid) {
      return;
    }

    const raw = this.consultaForm.value;
    const consultaParaSalvar: any = {
      id: raw.id,
      doutorId: raw.doutorId,
      pacienteId: raw.pacienteId,
      observacoes: raw.observacoes,
      dataHora: `${raw.data}T${raw.hora}:00`
    };

    if (this.editando && consultaParaSalvar.id) {
      this.consultaService.atualizarConsulta(consultaParaSalvar.id, consultaParaSalvar).subscribe({
        next: () => {
          this.carregarDados();
          this.fecharModal();
        },
        error: (error) => {
          console.error('Erro ao atualizar consulta:', error);
        }
      });
    } else {
      this.consultaService.criarConsulta(consultaParaSalvar).subscribe({
        next: () => {
          this.carregarDados();
          this.fecharModal();
        },
        error: (error) => {
          console.error('Erro ao criar consulta:', error);
        }
      });
    }
  }

  deletarConsulta(id: number | undefined): void {
    if (id && confirm('Deseja realmente deletar esta consulta?')) {
      this.consultaService.deletarConsulta(id).subscribe({
        next: () => {
          this.carregarDados();
        },
        error: (error) => {
          console.error('Erro ao deletar consulta:', error);
        }
      });
    }
  }

  get f() {
    return this.consultaForm.controls;
  }
}
