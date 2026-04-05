import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { isPlatformBrowser } from '@angular/common';
import { Empresa, EmpresaDTO } from '../models/empresa.model';

@Injectable({
  providedIn: 'root'
})
export class EmpresaService {
  private apiUrl = 'http://localhost:8080/api/empresas';

  constructor(
    private http: HttpClient,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  listarTodas(): Observable<EmpresaDTO[]> {
    if (isPlatformBrowser(this.platformId)) {
      return this.http.get<EmpresaDTO[]>(this.apiUrl);
    }
    return of([]);
  }

  verPorId(id: number): Observable<EmpresaDTO> {
    return this.http.get<EmpresaDTO>(`${this.apiUrl}/${id}`);
  }

  salvar(empresa: Empresa): Observable<EmpresaDTO> {
    return this.http.post<EmpresaDTO>(this.apiUrl, empresa);
  }

  atualizar(id: number, empresa: Empresa): Observable<EmpresaDTO> {
    return this.http.put<EmpresaDTO>(`${this.apiUrl}/${id}`, empresa);
  }

  atualizarStatus(id: number, status: string): Observable<EmpresaDTO> {
    return this.http.put<EmpresaDTO>(`${this.apiUrl}/${id}/status?status=${status}`, {});
  }
}
