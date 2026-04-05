export interface Empresa {
  id?: number;
  nome: string;
  cnpj: string;
  email: string;
  telefone: string;
  status?: string;
}

export interface EmpresaDTO {
  id?: number;
  nome: string;
  cnpj: string;
  email: string;
  telefone: string;
  status?: string;
}
