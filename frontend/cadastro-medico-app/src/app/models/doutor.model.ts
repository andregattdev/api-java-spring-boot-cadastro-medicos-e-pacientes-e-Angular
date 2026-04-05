export interface Doutor {
  id?: number;
  nome: string;
  crm: string;
  especialidade: string;
  email: string;
  telefone?: string;
  usuarioId?: number;
}

export interface DoutorDTO {
  id?: number;
  nome: string;
  crm: string;
  especialidade: string;
  email: string;
  telefone?: string;
  usuario?: {
    id: number;
    nome: string;
    email: string;
  };
}
