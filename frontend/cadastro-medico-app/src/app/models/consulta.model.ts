export interface Consulta {
  id?: number;
  dataHora: string;
  observacoes?: string;
  doutorId: number;
  pacienteId: number;
  empresaId?: number;
}

export interface ConsultaDTO {
  id?: number;
  dataHora: string;
  observacoes?: string;
  doutorId: number;
  doutorNome?: string;
  pacienteId: number;
  pacienteNome?: string;
  empresaId?: number;
}
