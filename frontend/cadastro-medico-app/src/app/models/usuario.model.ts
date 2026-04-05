export enum TipoUsuario {
  DOUTOR = 'DOUTOR',
  PACIENTE = 'PACIENTE',
  FUNCIONARIO = 'FUNCIONARIO',
  PARTICULAR = 'PARTICULAR'
}

export enum StatusUsuario {
  ATIVO = 'ATIVO',
  INATIVO = 'INATIVO'
}

export interface Usuario {
  id?: number;
  nome: string;
  email: string;
  tipo: TipoUsuario;
  status: StatusUsuario;
}

export interface UsuarioCreateDTO {
  nome: string;
  email: string;
  senha: string;
  tipo: TipoUsuario;
  status: StatusUsuario;
}

export interface UsuarioResponseDTO {
  id: number;
  nome: string;
  email: string;
  tipo: TipoUsuario;
  status: StatusUsuario;
}
