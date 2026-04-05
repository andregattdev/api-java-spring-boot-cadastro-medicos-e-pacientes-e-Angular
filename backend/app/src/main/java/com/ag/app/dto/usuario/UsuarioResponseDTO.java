package com.ag.app.dto.usuario;

public class UsuarioResponseDTO {

    private Long id;
    private String nome;
    private String email;
    private String tipo;
    private String status;
    private Long empresaId;
    private String empresaNome;

    public UsuarioResponseDTO() {
    }

    public UsuarioResponseDTO(Long id, String nome, String email, String tipo, String status, 
                             Long empresaId, String empresaNome) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.tipo = tipo;
        this.status = status;
        this.empresaId = empresaId;
        this.empresaNome = empresaNome;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }

    public String getEmpresaNome() {
        return empresaNome;
    }

    public void setEmpresaNome(String empresaNome) {
        this.empresaNome = empresaNome;
    }
}
