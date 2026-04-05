export interface LoginRequest {
  email: string;
  senha: string;
}

export interface LoginResponse {
  token: string;
  usuario: {
    id: number;
    nome: string;
    email: string;
    tipo: string;
  };
}

export interface AuthToken {
  token: string;
  expiresIn?: number;
}
