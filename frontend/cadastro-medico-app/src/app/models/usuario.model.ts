export enum TipoUsuario {
  PACIENTE = 'PACIENTE',
  MEDICO = 'MEDICO',
  FUNCIONARIO = 'FUNCIONARIO',
  ADMINISTRADOR = 'ADMINISTRADOR',
  EMPRESA_CONVENIO = 'EMPRESA_CONVENIO',
  PARTICULAR = 'PARTICULAR'
}

export enum StatusUsuario {
  ATIVO = 'ATIVO',
  INATIVO = 'INATIVO',
  BLOQUEADO = 'BLOQUEADO'
}

export interface Usuario {
  id?: number;
  nome: string;
  email: string;
  tipo: TipoUsuario;
  status: StatusUsuario;
  empresaId?: number | null;
  empresaNome?: string | null;
}

export interface UsuarioCreateDTO {
  nome: string;
  email: string;
  senha: string;
  tipo: TipoUsuario;
  empresaId?: number | null;
}

export interface UsuarioResponseDTO {
  id: number;
  nome: string;
  email: string;
  tipo: string;
  status: string;
  empresaId?: number | null;
  empresaNome?: string | null;
}
