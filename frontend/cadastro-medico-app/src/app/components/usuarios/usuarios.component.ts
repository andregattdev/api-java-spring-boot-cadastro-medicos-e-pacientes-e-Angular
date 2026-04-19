import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UsuarioService } from '../../services/usuario.service';
import { UsuarioResponseDTO, TipoUsuario } from '../../models/usuario.model';
import { EmpresaService } from '../../services/empresa.service';
import { EmpresaDTO } from '../../models/empresa.model';

@Component({
  selector: 'app-usuarios',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './usuarios.component.html',
  styleUrl: './usuarios.component.css'
})
export class UsuariosComponent implements OnInit {
  usuarios: UsuarioResponseDTO[] = [];
  empresas: EmpresaDTO[] = [];
  displayModal = false;
  usuarioForm: FormGroup;
  editando = false;
  carregando = false;
  tiposUsuario = Object.values(TipoUsuario);

  constructor(
    private usuarioService: UsuarioService,
    private empresaService: EmpresaService,
    private formBuilder: FormBuilder
  ) {
    this.usuarioForm = this.formBuilder.group({
      id: [''],
      nome: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      senha: ['', [Validators.required, Validators.minLength(6)]],
      tipo: ['', [Validators.required]],
      status: ['', [Validators.required]],
      empresaId: ['']
    });

    this.usuarioForm.get('tipo')?.valueChanges.subscribe(tipo => {
      const empresaControl = this.usuarioForm.get('empresaId');
      if (tipo === 'FUNCIONARIO') {
        empresaControl?.setValidators([Validators.required]);
      } else {
        empresaControl?.clearValidators();
      }
      empresaControl?.updateValueAndValidity();
    });
  }

  ngOnInit(): void {
    this.carregarUsuarios();
    this.carregarEmpresas();
  }

  carregarEmpresas(): void {
    this.empresaService.listarTodas().subscribe({
      next: (data) => this.empresas = data,
      error: (err) => console.error('Erro ao carregar empresas:', err)
    });
  }

  carregarUsuarios(): void {
    this.carregando = true;
    this.usuarioService.listarUsuarios().subscribe({
      next: (data) => {
        this.usuarios = data;
        this.carregando = false;
      },
      error: (error) => {
        console.error('Erro ao carregar usuários:', error);
        this.carregando = false;
      }
    });
  }

  abrirModal(usuario?: UsuarioResponseDTO): void {
    if (usuario) {
      this.editando = true;
      this.usuarioForm.patchValue(usuario);
      this.usuarioForm.get('senha')?.clearValidators();
      this.usuarioForm.get('senha')?.setValidators([Validators.minLength(6)]);
    } else {
      this.editando = false;
      this.usuarioForm.reset();
      this.usuarioForm.get('senha')?.setValidators([Validators.required, Validators.minLength(6)]);
    }
    this.usuarioForm.get('senha')?.updateValueAndValidity();
    this.displayModal = true;
  }

  fecharModal(): void {
    this.displayModal = false;
    this.usuarioForm.reset();
  }

  salvarUsuario(): void {
    if (this.usuarioForm.invalid) {
      return;
    }

    const usuario = this.usuarioForm.value;

    if (this.editando && usuario.id) {
      this.usuarioService.atualizarUsuario(usuario.id, usuario).subscribe({
        next: () => {
          this.carregarUsuarios();
          this.fecharModal();
        },
        error: (error) => {
          console.error('Erro ao atualizar usuário:', error);
        }
      });
    } else {
      this.usuarioService.criarUsuario(usuario).subscribe({
        next: () => {
          this.carregarUsuarios();
          this.fecharModal();
        },
        error: (error) => {
          console.error('Erro ao criar usuário:', error);
        }
      });
    }
  }

  deletarUsuario(id: number | undefined): void {
    if (id && confirm('Deseja realmente deletar este usuário?')) {
      this.usuarioService.deletarUsuario(id).subscribe({
        next: () => {
          this.carregarUsuarios();
        },
        error: (error) => {
          console.error('Erro ao deletar usuário:', error);
        }
      });
    }
  }

  get f() {
    return this.usuarioForm.controls;
  }
}
