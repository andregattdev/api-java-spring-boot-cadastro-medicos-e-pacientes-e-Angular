import { Component, OnInit, Inject, PLATFORM_ID, ChangeDetectorRef } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PacienteService } from '../../services/paciente.service';
import { PacienteDTO } from '../../models/paciente.model';
import { EmpresaService } from '../../services/empresa.service';
import { EmpresaDTO } from '../../models/empresa.model';

@Component({
  selector: 'app-pacientes',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './pacientes.component.html',
  styleUrl: './pacientes.component.css'
})
export class PacientesComponent implements OnInit {
  pacientes: PacienteDTO[] = [];
  empresas: EmpresaDTO[] = [];
  displayModal = false;
  pacienteForm: FormGroup;
  editando = false;
  carregando = true;

  constructor(
    private pacienteService: PacienteService,
    private empresaService: EmpresaService,
    private formBuilder: FormBuilder,
    @Inject(PLATFORM_ID) private platformId: Object,
    private cdr: ChangeDetectorRef
  ) {
    this.pacienteForm = this.formBuilder.group({
      id: [''],
      nome: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      cpf: ['', [Validators.required]],
      telefone: ['', [Validators.required]],
      empresaId: ['']
    });
  }

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.carregarPacientes();
    } else {
      this.carregando = false;
    }
  }

  carregarPacientes(): void {
    this.carregando = true;
    this.pacienteService.listarPacientes().subscribe({
      next: (data) => {
        this.pacientes = data;
        this.carregando = false;
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('Erro ao carregar pacientes:', error);
        this.carregando = false;
        this.cdr.detectChanges();
      }
    });

    this.empresaService.listarTodas().subscribe({
      next: (data) => {
        this.empresas = data;
        this.cdr.detectChanges();
      }
    });
  }

  abrirModal(paciente?: PacienteDTO): void {
    if (paciente) {
      this.editando = true;
      this.pacienteForm.patchValue(paciente);
    } else {
      this.editando = false;
      this.pacienteForm.reset();
    }
    this.displayModal = true;
  }

  fecharModal(): void {
    this.displayModal = false;
    this.pacienteForm.reset();
  }

  salvarPaciente(): void {
    if (this.pacienteForm.invalid) {
      return;
    }

    const paciente = { ...this.pacienteForm.value };

    if (paciente.cpf) {
      let cpfStr = paciente.cpf.replace(/\D/g, '');
      if (cpfStr.length === 11) {
        paciente.cpf = cpfStr.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, "$1.$2.$3-$4");
      }
    }

    if (paciente.telefone) {
        let telStr = paciente.telefone.replace(/\D/g, '');
        if (telStr.length === 11) {
            paciente.telefone = telStr.replace(/(\d{2})(\d{5})(\d{4})/, "($1) $2-$3");
        } else if (telStr.length === 10) {
            paciente.telefone = telStr.replace(/(\d{2})(\d{4})(\d{4})/, "($1) $2-$3");
        }
    }

    if (this.editando && paciente.id) {
      this.pacienteService.atualizarPaciente(paciente.id, paciente).subscribe({
        next: () => {
          this.carregarPacientes();
          this.fecharModal();
        },
        error: (error) => {
          console.error('Erro ao atualizar paciente:', error);
        }
      });
    } else {
      this.pacienteService.criarPaciente(paciente).subscribe({
        next: () => {
          this.carregarPacientes();
          this.fecharModal();
        },
        error: (error) => {
          console.error('Erro ao criar paciente:', error);
        }
      });
    }
  }

  deletarPaciente(id: number | undefined): void {
    if (id && confirm('Deseja realmente deletar este paciente?')) {
      this.pacienteService.deletarPaciente(id).subscribe({
        next: () => {
          this.carregarPacientes();
        },
        error: (error) => {
          console.error('Erro ao deletar paciente:', error);
        }
      });
    }
  }

  get f() {
    return this.pacienteForm.controls;
  }
}
