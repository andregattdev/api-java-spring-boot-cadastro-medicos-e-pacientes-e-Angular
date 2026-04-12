import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { TipoUsuario, StatusUsuario } from '../../models/usuario.model';

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

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.registerForm = this.formBuilder.group({
      nome: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      senha: ['', [Validators.required, Validators.minLength(6)]],
      tipo: ['PACIENTE', [Validators.required]] // Default to PACIENTE
    });
  }

  ngOnInit(): void {}

  get f() { return this.registerForm.controls; }

  onSubmit(): void {
    this.submitted = true;
    this.error = '';
    this.success = false;

    if (this.registerForm.invalid) {
      return;
    }

    this.loading = true;

    // Hardcode Status as ATIVO for auto-registration
    const usuarioPayload = {
      ...this.registerForm.value,
      status: StatusUsuario.ATIVO
    };

    this.authService.register(usuarioPayload).subscribe({
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
}
