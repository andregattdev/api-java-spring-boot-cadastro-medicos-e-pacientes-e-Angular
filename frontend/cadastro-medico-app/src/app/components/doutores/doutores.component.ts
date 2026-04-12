import { Component, OnInit, Inject, PLATFORM_ID, ChangeDetectorRef } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DoutorService } from '../../services/doutor.service';
import { DoutorDTO } from '../../models/doutor.model';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-doutores',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './doutores.component.html',
  styleUrl: './doutores.component.css'
})
export class DoutoresComponent implements OnInit {
  doutores: DoutorDTO[] = [];
  displayModal = false;
  doutorForm: FormGroup;
  editando = false;
  carregando = true;
  totalDoutores: number = 0;
  isPaciente = false;

  especialidades = [
    'ALERGIA_IMUNOLOGIA', 'ANESTESIOLOGIA', 'ANGIOLOGIA', 'CANCEROLOGIA', 'CARDIOLOGIA',
    'CIRURGIA_CARDIOVASCULAR', 'CIRURGIA_MAO', 'CIRURGIA_CABECA_PESCOCO',
    'CIRURGIA_APARELHO_DIGESTIVO', 'CIRURGIA_GERAL', 'CIRURGIA_PEDIATRICA',
    'CIRURGIA_PLASTICA', 'CIRURGIA_TORACICA', 'CIRURGIA_VASCULAR', 'CLINICA_MEDICA',
    'COLOPROCTOLOGIA', 'DERMATOLOGIA', 'ENDOCRINOLOGIA_METABOLOGIA', 'GASTROENTEROLOGIA',
    'GENETICA_MEDICA', 'GERIATRIA', 'GINECOLOGIA_OBSTETRICIA', 'HEMATOLOGIA_HEMOTERAPIA',
    'HOMEOPATIA', 'INFECTOLOGIA', 'MEDICINA_FAMILIA_COMUNIDADE', 'MEDICINA_TRABALHO',
    'MEDICINA_TRAFEGO', 'MEDICINA_ESPORTIVA', 'MEDICINA_FISICA_REABILITACAO',
    'MEDICINA_INTENSIVA', 'MEDICINA_LEGAL_PERICIA', 'MEDICINA_NUCLEAR',
    'MEDICINA_PREVENTIVA_SOCIAL', 'NEFROLOGIA', 'NEUROCIRURGIA', 'NEUROLOGIA',
    'NUTROLOGIA', 'OFTALMOLOGIA', 'ORTOPEDIA_TRAUMATOLOGIA', 'OTORRINOLARINGOLOGIA',
    'PATOLOGIA', 'PATOLOGIA_CLINICA', 'PEDIATRIA', 'PNEUMOLOGIA', 'PSIQUIATRIA',
    'RADIOLOGIA_DIAGNOSTICO_IMAGEM', 'RADIOTERAPIA', 'REUMATOLOGIA', 'UROLOGIA'
  ];

  constructor(
    private doutorService: DoutorService,
    private formBuilder: FormBuilder,
    @Inject(PLATFORM_ID) private platformId: Object,
    private cdr: ChangeDetectorRef,
    private authService: AuthService
  ) {
    this.isPaciente = this.authService.isPaciente();
    this.doutorForm = this.formBuilder.group({
      id: [''],
      nome: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      crm: ['', [Validators.required]],
      especialidade: ['', [Validators.required]],
      telefone: ['', [Validators.required]]
    });
  }

  ngOnInit(): void {
    this.doutorService.getCount().subscribe(count => {
      this.totalDoutores = count;
    });
    
    if (isPlatformBrowser(this.platformId)) {
      this.carregarDoutores();
    } else {
      this.carregando = false;
    }
  }

  carregarDoutores(): void {
    this.carregando = true;
    this.doutorService.listarDoutores().subscribe({
      next: (data) => {
        this.doutores = data;
        this.carregando = false;
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('Erro ao carregar doutores:', error);
        this.carregando = false;
        this.cdr.detectChanges();
      }
    });
  }

  abrirModal(doutor?: DoutorDTO): void {
    if (doutor) {
      this.editando = true;
      this.doutorForm.patchValue(doutor);
    } else {
      this.editando = false;
      this.doutorForm.reset();
    }
    this.displayModal = true;
  }

  fecharModal(): void {
    this.displayModal = false;
    this.doutorForm.reset();
  }

  salvarDoutor(): void {
    if (this.doutorForm.invalid) {
      return;
    }

    const doutor = this.doutorForm.value;

    if (this.editando && doutor.id) {
      this.doutorService.atualizarDoutor(doutor.id, doutor).subscribe({
        next: () => {
          this.carregarDoutores();
          this.fecharModal();
        },
        error: (error) => {
          console.error('Erro ao atualizar doutor:', error);
        }
      });
    } else {
      this.doutorService.criarDoutor(doutor).subscribe({
        next: () => {
          this.carregarDoutores();
          this.fecharModal();
        },
        error: (error) => {
          console.error('Erro ao criar doutor:', error);
        }
      });
    }
  }

  deletarDoutor(id: number | undefined): void {
    if (id && confirm('Deseja realmente deletar este doutor?')) {
      this.doutorService.deletarDoutor(id).subscribe({
        next: () => {
          this.carregarDoutores();
        },
        error: (error) => {
          console.error('Erro ao deletar doutor:', error);
        }
      });
    }
  }

  get f() {
    return this.doutorForm.controls;
  }
}
