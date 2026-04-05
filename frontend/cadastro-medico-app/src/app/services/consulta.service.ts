import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ConsultaDTO } from '../models/consulta.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ConsultaService {
  private apiUrl = `${environment.apiUrl}/consultas`;

  constructor(private http: HttpClient) { }

  criarConsulta(consulta: ConsultaDTO): Observable<ConsultaDTO> {
    return this.http.post<ConsultaDTO>(this.apiUrl, consulta);
  }

  obterConsulta(id: number): Observable<ConsultaDTO> {
    return this.http.get<ConsultaDTO>(`${this.apiUrl}/${id}`);
  }

  listarConsultas(): Observable<ConsultaDTO[]> {
    return this.http.get<ConsultaDTO[]>(this.apiUrl);
  }

  listarConsultasPorPaciente(pacienteId: number): Observable<ConsultaDTO[]> {
    return this.http.get<ConsultaDTO[]>(`${this.apiUrl}/paciente/${pacienteId}`);
  }

  listarConsultasPorDoutor(doutorId: number): Observable<ConsultaDTO[]> {
    return this.http.get<ConsultaDTO[]>(`${this.apiUrl}/doutor/${doutorId}`);
  }

  atualizarConsulta(id: number, consulta: ConsultaDTO): Observable<ConsultaDTO> {
    return this.http.put<ConsultaDTO>(`${this.apiUrl}/${id}`, consulta);
  }

  deletarConsulta(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  // Buscar a contagem de consultas
  getCount(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/count`);
  }

  getCountByDay(date: string): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/count/day/${date}`);
  }

  getCountByWeek(start: string, end: string): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/count/week?start=${start}&end=${end}`);
  }

  getCountByMonth(year: number, month: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/count/month/${year}/${month}`);
  }

  // lista de consultas do dia
  getConsultasDoDia(date: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/day/${date}/list`);
  }

}
