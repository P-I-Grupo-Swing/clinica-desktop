/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.imepac.clinica.entidades;

public class Medico {
    
    private Long id;
    private String nome;
    private String crm; // Este será o LOGIN
    private String senha;
    private Especialidade especialidade;
    
    // Auxiliar para DAO
    private Long especialidadeId; 

    public Medico() {
    }

    public Medico(Long id, String nome, String crm, String senha, Especialidade especialidade) {
        this.id = id;
        this.nome = nome;
        this.crm = crm;
        this.senha = senha;
        this.especialidade = especialidade;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCrm() { return crm; }
    public void setCrm(String crm) { this.crm = crm; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public Especialidade getEspecialidade() { return especialidade; }
    public void setEspecialidade(Especialidade especialidade) { this.especialidade = especialidade; }

    public Long getEspecialidadeId() { return especialidadeId; }
    public void setEspecialidadeId(Long especialidadeId) { this.especialidadeId = especialidadeId; }

    @Override
    public String toString() {
        return this.nome;
    }
}