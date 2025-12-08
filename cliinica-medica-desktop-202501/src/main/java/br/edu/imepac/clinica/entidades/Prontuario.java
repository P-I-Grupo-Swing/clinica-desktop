/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.imepac.clinica.entidades;

/**
 *
 * @author Pedro Fernandes
 */
public class Prontuario {
    
    private Long id;
    private String historico;   // Anamnese / O que o paciente sentiu
    private String receituario; // Remédios receitados
    private String exames;      // Pedidos de exames
    
    // Vínculo Obrigatório: Um prontuário pertence a UMA consulta
    private Consulta consulta;

    public Prontuario() {
    }

    public Prontuario(Long id, String historico, String receituario, String exames, Consulta consulta) {
        this.id = id;
        this.historico = historico;
        this.receituario = receituario;
        this.exames = exames;
        this.consulta = consulta;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getHistorico() { return historico; }
    public void setHistorico(String historico) { this.historico = historico; }

    public String getReceituario() { return receituario; }
    public void setReceituario(String receituario) { this.receituario = receituario; }

    public String getExames() { return exames; }
    public void setExames(String exames) { this.exames = exames; }

    public Consulta getConsulta() { return consulta; }
    public void setConsulta(Consulta consulta) { this.consulta = consulta; }
}