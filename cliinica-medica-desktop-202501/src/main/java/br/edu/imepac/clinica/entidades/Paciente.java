package br.edu.imepac.clinica.entidades;

import java.time.LocalDate;

public class Paciente {

    private Long id;
    private String nome;
    private LocalDate dataNascimento; // Mudança: RG -> Data de Nascimento
    private String cpf;
    private String telefone;

    public Paciente() {
    }

    public Paciente(Long id, String nome, LocalDate dataNascimento, String cpf, String telefone) {
        this.id = id;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.cpf = cpf;
        this.telefone = telefone;
    }

    // --- Getters e Setters ---

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

    // Novo Getter
    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    // Novo Setter
    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    @Override
    public String toString() {
        return this.nome;
    }
}