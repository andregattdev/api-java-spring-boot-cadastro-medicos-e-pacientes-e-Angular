export interface Paciente {
  id?: number;
  nome: string;
  email: string;
  cpf: string;
  telefone?: string;
  usuarioId?: number;
}

export interface PacienteDTO {
  id?: number;
  nome: string;
  email: string;
  cpf: string;
  telefone?: string;
  usuario?: {
    id: number;
    nome: string;
    email: string;
  };
  empresaId?: number | null;
  empresaNome?: string | null;
}
