import { Component, OnInit, Inject, PLATFORM_ID } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors } from '@angular/forms';

import { EmpresaDTO } from '../../models/empresa.model';
import { Observable } from 'rxjs';
import { EmpresaService } from '../../services/empresa.service';

@Component({
  selector: 'app-empresas',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './empresas.component.html'
})
export class EmpresasComponent implements OnInit {
  empresas$!: Observable<EmpresaDTO[]>; // declarada sem inicialização
  displayModal = false;
  empresaForm: FormGroup;
  editando = false;

  constructor(
    private empresaService: EmpresaService,
    private formBuilder: FormBuilder,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {
    this.empresaForm = this.formBuilder.group({
      id: [''],
      nome: ['', [Validators.required, Validators.minLength(3)]],
      cnpj: ['', [Validators.required, this.cnpjValidator]], // sem regex de pontuação
      email: ['', [Validators.required, Validators.email]],
      telefone: ['', [Validators.required]],
      status: ['ATIVO']
    });
  }

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.empresas$ = this.empresaService.listarTodas(); // inicializa aqui
    }
  }

  abrirModal(empresa?: EmpresaDTO): void {
    if (empresa) {
      this.editando = true;
      this.empresaForm.patchValue(empresa);
    } else {
      this.editando = false;
      this.empresaForm.reset({ status: 'ATIVO' });
    }
    this.displayModal = true;
  }

  fecharModal(): void {
    this.displayModal = false;
    this.empresaForm.reset();
  }

  salvarEmpresa(): void {
    if (this.empresaForm.invalid) return;

    const raw = { ...this.empresaForm.value };
    if (raw.telefone) raw.telefone = raw.telefone.replace(/\D/g, '');

    const request$ = this.editando && raw.id
      ? this.empresaService.atualizar(raw.id, raw)
      : this.empresaService.salvar(raw);

    request$.subscribe({
      next: () => {
        this.empresas$ = this.empresaService.listarTodas(); // recarrega lista
        this.fecharModal();
      },
      error: (error) => console.error('Erro ao salvar empresa:', error)
    });
  }

  get f() {
    return this.empresaForm.controls;
  }

  /**
   * Validator customizado para CNPJ (sem exigir pontuação)
   */
  cnpjValidator(control: AbstractControl): ValidationErrors | null {
    const value = control.value?.replace(/\D/g, '');
    if (!value) return null;

    if (value.length !== 14) {
      return { cnpjInvalido: true };
    }

    if (/^(\d)\1+$/.test(value)) {
      return { cnpjInvalido: true };
    }

    let tamanho = value.length - 2;
    let numeros = value.substring(0, tamanho);
    let digitos = value.substring(tamanho);
    let soma = 0;
    let pos = tamanho - 7;
    for (let i = tamanho; i >= 1; i--) {
      soma += parseInt(numeros.charAt(tamanho - i)) * pos--;
      if (pos < 2) pos = 9;
    }
    let resultado = soma % 11 < 2 ? 0 : 11 - (soma % 11);
    if (resultado !== parseInt(digitos.charAt(0))) {
      return { cnpjInvalido: true };
    }

    tamanho = tamanho + 1;
    numeros = value.substring(0, tamanho);
    soma = 0;
    pos = tamanho - 7;
    for (let i = tamanho; i >= 1; i--) {
      soma += parseInt(numeros.charAt(tamanho - i)) * pos--;
      if (pos < 2) pos = 9;
    }
    resultado = soma % 11 < 2 ? 0 : 11 - (soma % 11);
    if (resultado !== parseInt(digitos.charAt(1))) {
      return { cnpjInvalido: true };
    }

    return null;
  }
}
