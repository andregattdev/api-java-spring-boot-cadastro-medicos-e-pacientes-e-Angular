package com.ag.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nome;
    private String email;
    private String senha;

    @Enumerated(EnumType.STRING)
    private TipoUsuario tipo;

    @Enumerated(EnumType.STRING)
    private StatusUsuario status;

    // Relacionamento opcional com entidades específicas
    @OneToOne(mappedBy = "usuario")
    private Doutor doutor;

    @OneToOne(mappedBy = "usuario")
    private Paciente paciente;

    @OneToOne(mappedBy = "usuario")
    private Funcionario funcionario;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    @JsonBackReference
    private Empresa empresa;


    public Usuario() {}

    // Construtor sem id (id é gerado pelo JPA)
    public Usuario(String nome, String email, String senha, TipoUsuario tipo, StatusUsuario status, Doutor doutor, Paciente paciente, Funcionario funcionario, Empresa empresa) {

        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.tipo = tipo;
        this.status = status;
        this.doutor = doutor;
        this.paciente = paciente;
        this.funcionario = funcionario;
        this.empresa = empresa;
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

    public String getSenha() {

        return senha;
    }

    public void setSenha(String senha) {

        this.senha = senha;
    }

    public TipoUsuario getTipo() {

        return tipo;
    }

    public void setTipo(TipoUsuario tipo) {

        this.tipo = tipo;
    }

    public StatusUsuario getStatus() {

        return status;
    }

    public void setStatus(StatusUsuario status) {

        this.status = status;
    }

    public Doutor getDoutor() {

        return doutor;
    }

    public void setDoutor(Doutor doutor) {

        this.doutor = doutor;
    }

    public Paciente getPaciente() {

        return paciente;
    }

    public void setPaciente(Paciente paciente) {

        this.paciente = paciente;
    }

    public Funcionario getFuncionario() {

        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {

        this.funcionario = funcionario;
    }

    public Empresa getEmpresa() {

        return empresa;
    }

    public void setEmpresa(Empresa empresa) {

        this.empresa = empresa;
    }
}