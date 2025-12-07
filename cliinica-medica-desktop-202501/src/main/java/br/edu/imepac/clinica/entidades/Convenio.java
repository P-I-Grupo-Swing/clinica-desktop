/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.imepac.clinica.entidades;

/**
 *
 * @author alcan
 */
public class Convenio {
    // Enum foi nomeado com "Status", para poder ser utilizado em outras classes.
    public enum Status {
        ATIVO, INATIVO;
    }

    private Long id;
    private String nome;
    private String descricao;
    private Status status;

    public Convenio() {
        this.status = Status.ATIVO;
    }

    public Convenio(Long id, String nome, String descricao, Status status) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.status = status;
    }
    
    // Construtor sem ID
    public Convenio(String nome, String descricao, Status status) {
        this.nome = nome;
        this.descricao = descricao;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    //método utilizado para ver se o convênio está funcionando(ativo/inativo)
    public void ativar() { this.status = Status.ATIVO; }
    public void inativar() { this.status = Status.INATIVO; }
    public boolean isAtivo() { return this.status == Status.ATIVO; }

    @Override
    public String toString() {
        return this.nome;
    }
}
