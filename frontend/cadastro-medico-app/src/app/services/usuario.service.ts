import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UsuarioCreateDTO, UsuarioResponseDTO } from '../models/usuario.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {
  private apiUrl = `${environment.apiUrl}/usuarios`;

  constructor(private http: HttpClient) {}

  criarUsuario(usuario: UsuarioCreateDTO): Observable<UsuarioResponseDTO> {
    return this.http.post<UsuarioResponseDTO>(this.apiUrl, usuario);
  }

  obterUsuario(id: number): Observable<UsuarioResponseDTO> {
    return this.http.get<UsuarioResponseDTO>(`${this.apiUrl}/${id}`);
  }

  listarUsuarios(): Observable<UsuarioResponseDTO[]> {
    return this.http.get<UsuarioResponseDTO[]>(this.apiUrl);
  }

  getCount(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/count`);
  }

  atualizarUsuario(id: number, usuario: UsuarioCreateDTO): Observable<UsuarioResponseDTO> {
    return this.http.put<UsuarioResponseDTO>(`${this.apiUrl}/${id}`, usuario);
  }

  deletarUsuario(id: number): Observable<UsuarioResponseDTO> {
    return this.http.put<UsuarioResponseDTO>(`${this.apiUrl}/${id}/status`, null, { params: { status: 'INATIVO' } });
  }
}
