import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { LoginRequest, LoginResponse } from '../models/auth.model';
import { UsuarioCreateDTO } from '../models/usuario.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = `${environment.apiUrl}/auth`;
  
  private tokenSubject = new BehaviorSubject<string | null>(this.getStoredToken());
  public token$ = this.tokenSubject.asObservable();
  
  private tipoUsuarioSubject = new BehaviorSubject<string | null>(this.getStoredTipoUsuario());
  public tipoUsuario$ = this.tipoUsuarioSubject.asObservable();

  constructor(private http: HttpClient) {}

  login(credentials: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, credentials)
      .pipe(
        tap(response => {
          if (response.token) {
            localStorage.setItem('authToken', response.token);
            this.tokenSubject.next(response.token);
          }
          if (response.tipo) {
            localStorage.setItem('tipoUsuario', response.tipo);
            this.tipoUsuarioSubject.next(response.tipo);
          }
        })
      );
  }

  register(usuario: UsuarioCreateDTO): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/register`, usuario);
  }

  logout(): void {
    localStorage.removeItem('authToken');
    localStorage.removeItem('tipoUsuario');
    this.tokenSubject.next(null);
    this.tipoUsuarioSubject.next(null);
  }

  getToken(): string | null {
    return this.tokenSubject.value;
  }
  
  getTipoUsuario(): string | null {
    return this.tipoUsuarioSubject.value;
  }

  private getStoredToken(): string | null {
    if (typeof localStorage !== 'undefined') {
      return localStorage.getItem('authToken');
    }
    return null;
  }
  
  private getStoredTipoUsuario(): string | null {
    if (typeof localStorage !== 'undefined') {
      return localStorage.getItem('tipoUsuario');
    }
    return null;
  }

  isAuthenticated(): boolean {
    return this.getToken() !== null;
  }
  
  isAdmin(): boolean {
    const tipo = this.getTipoUsuario();
    return tipo === 'FUNCIONARIO' || tipo === 'ADMINISTRADOR';
  }

  isPaciente(): boolean {
    return this.getTipoUsuario() === 'PACIENTE';
  }

  isMedico(): boolean {
    return this.getTipoUsuario() === 'MEDICO';
  }
}
