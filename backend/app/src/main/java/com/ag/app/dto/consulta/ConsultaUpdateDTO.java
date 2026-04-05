package com.ag.app.dto.consulta;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public class ConsultaUpdateDTO {

    @NotNull(message = "Data e hora são obrigatórias")
    @Future(message = "Data e hora devem ser no futuro")
    private LocalDateTime dataHora;

    @Size(max = 500, message = "Observações não podem ter mais de 500 caracteres")
    private String observacoes;

    @NotNull(message = "ID do doutor é obrigatório")
    private Long doutorId;

    @NotNull(message = "ID do paciente é obrigatório")
    private Long pacienteId;

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

    public Long getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(Long pacienteId) {
        this.pacienteId = pacienteId;
    }
}
