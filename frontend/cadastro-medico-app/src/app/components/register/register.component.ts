import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { TipoUsuario } from '../../models/usuario.model';
import { EmpresaService } from '../../services/empresa.service';
import { EmpresaDTO } from '../../models/empresa.model';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './register.component.html'
})
export class RegisterComponent implements OnInit {
  registerForm: FormGroup;
  loading = false;
  submitted = false;
  error = '';
  success = false;
  empresas: EmpresaDTO[] = [];

  tiposUsuario = [
    TipoUsuario.PACIENTE,
    TipoUsuario.MEDICO,
    TipoUsuario.FUNCIONARIO,
    TipoUsuario.EMPRESA_CONVENIO,
    TipoUsuario.PARTICULAR
  ];

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private empresaService: EmpresaService,
    private router: Router
  ) {
    this.registerForm = this.formBuilder.group({
      nome: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      senha: ['', [Validators.required, Validators.minLength(6)]],
      tipo: [TipoUsuario.PACIENTE, [Validators.required]],
      empresaId: ['']
    });

    this.registerForm.get('tipo')?.valueChanges.subscribe(tipo => {
      const empresaControl = this.registerForm.get('empresaId');
      if (tipo === TipoUsuario.FUNCIONARIO) {
        empresaControl?.setValidators([Validators.required]);
      } else {
        empresaControl?.clearValidators();
        empresaControl?.setValue('');
      }
      empresaControl?.updateValueAndValidity();
    });
  }

  ngOnInit(): void {
    this.empresaService.listarPublico().subscribe({
      next: (data) => (this.empresas = data),
      error: () => {
        // Se falhar, mantém vazio; apenas impede cadastro de FUNCIONARIO sem empresa.
        this.empresas = [];
      }
    });
  }

  get f() { return this.registerForm.controls; }

  onSubmit(): void {
    this.submitted = true;
    this.error = '';
    this.success = false;

    if (this.registerForm.invalid) {
      return;
    }

    this.loading = true;

    this.authService.register(this.registerForm.value).subscribe({
      next: () => {
        this.success = true;
        this.loading = false;
        setTimeout(() => {
           this.router.navigate(['/login']);
        }, 2000);
      },
      error: (err) => {
        this.error = err.error?.message || 'Erro ao registrar usuário. Tente novamente.';
        this.loading = false;
      }
    });
  }

  getTipoLabel(tipo: string | null | undefined): string {
    switch (tipo) {
      case TipoUsuario.FUNCIONARIO:
        return 'Secretaria';
      case TipoUsuario.MEDICO:
        return 'Especialista';
      case TipoUsuario.PACIENTE:
        return 'Paciente';
      case TipoUsuario.EMPRESA_CONVENIO:
        return 'Empresa/Convênio';
      case TipoUsuario.PARTICULAR:
        return 'Particular';
      default:
        return tipo ?? '';
    }
  }
}
