package com.ag.app.dto.consulta;

import java.time.LocalDateTime;

public class ConsultaResponseDTO {

    private Long id;
    private LocalDateTime dataHora;
    private String observacoes;
    private Long doutorId;
    private String doutorNome;
    private Long pacienteId;
    private String pacienteNome;
    private Long empresaId;

    public ConsultaResponseDTO() {
    }

    public ConsultaResponseDTO(Long id, LocalDateTime dataHora, String observacoes, Long doutorId,
                              String doutorNome, Long pacienteId, String pacienteNome, Long empresaId) {
        this.id = id;
        this.dataHora = dataHora;
        this.observacoes = observacoes;
        this.doutorId = doutorId;
        this.doutorNome = doutorNome;
        this.pacienteId = pacienteId;
        this.pacienteNome = pacienteNome;
        this.empresaId = empresaId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Long getDoutorId() {
        return doutorId;
    }

    public void setDoutorId(Long doutorId) {
        this.doutorId = doutorId;
    }

    public String getDoutorNome() {
        return doutorNome;
    }

    public void setDoutorNome(String doutorNome) {
        this.doutorNome = doutorNome;
    }

    public Long getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(Long pacienteId) {
        this.pacienteId = pacienteId;
    }

    public String getPacienteNome() {
        return pacienteNome;
    }

    public void setPacienteNome(String pacienteNome) {
        this.pacienteNome = pacienteNome;
    }

    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }
}
