export interface LoginRequest {
  email: string;
  senha: string;
}

export interface LoginResponse {
  token: string;
  tipo: string;
}

export interface AuthToken {
  token: string;
  expiresIn?: number;
}
