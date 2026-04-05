import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DoutorDTO } from '../models/doutor.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class DoutorService {
  private apiUrl = `${environment.apiUrl}/doutores`;

  constructor(private http: HttpClient) { }

  criarDoutor(doutor: DoutorDTO): Observable<DoutorDTO> {
    return this.http.post<DoutorDTO>(this.apiUrl, doutor);
  }

  obterDoutor(id: number): Observable<DoutorDTO> {
    return this.http.get<DoutorDTO>(`${this.apiUrl}/${id}`);
  }

  listarDoutores(): Observable<DoutorDTO[]> {
    return this.http.get<DoutorDTO[]>(this.apiUrl);
  }

  atualizarDoutor(id: number, doutor: DoutorDTO): Observable<DoutorDTO> {
    return this.http.put<DoutorDTO>(`${this.apiUrl}/${id}`, doutor);
  }

  deletarDoutor(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  // Buscar a contagem de doutores
  getCount(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/count`);
  }
}
