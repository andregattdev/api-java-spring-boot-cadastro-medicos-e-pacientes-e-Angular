package com.ag.app.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class ConsultaDTO {
    private Long id;

    @NotNull(message = "Data e hora são obrigatórias")
    @Future(message = "Data e hora devem ser no futuro")
    private LocalDateTime dataHora;

    @Size(max = 500, message = "Observações não podem ter mais de 500 caracteres")
    private String observacoes;

    @NotNull(message = "ID do doutor é obrigatório")
    private Long doutorId;

    private String doutorNome;

    @NotNull(message = "ID do paciente é obrigatório")
    private Long pacienteId;

    private String pacienteNome;

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
}
