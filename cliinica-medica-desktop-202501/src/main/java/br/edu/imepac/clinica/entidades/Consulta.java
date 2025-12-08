/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.imepac.clinica.entidades;

/**
 *
 * @author Pedro Fernandes
 */
import br.edu.imepac.clinica.enums.StatusConsulta;
import java.time.LocalDateTime;

public class Consulta {
    
    private Long id;
    private LocalDateTime dataHora; // Data e Hora juntas (Java 8+)
    private String observacoes;     // Notas simples (ex: "Primeira vez")
    
    // Status padrão é AGENDADA ao criar
    private StatusConsulta status = StatusConsulta.AGENDADA; 
    
    // --- RELACIONAMENTOS (Objetos Completos) ---
    // Em vez de salvar só o ID (int), guardamos o objeto inteiro.
    // Isso ajuda muito na hora de mostrar o nome na tela.
    private Medico medico;
    private Paciente paciente;
    private Convenio convenio;

    // Construtor Vazio
    public Consulta() {
    }

    // Construtor Completo
    public Consulta(Long id, LocalDateTime dataHora, String observacoes, Medico medico, Paciente paciente, Convenio convenio) {
        this.id = id;
        this.dataHora = dataHora;
        this.observacoes = observacoes;
        this.medico = medico;
        this.paciente = paciente;
        this.convenio = convenio;
    }

    // --- Getters e Setters ---

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

    public StatusConsulta getStatus() {
        return status;
    }

    public void setStatus(StatusConsulta status) {
        this.status = status;
    }

    // --- Getters e Setters dos Relacionamentos ---

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Convenio getConvenio() {
        return convenio;
    }

    public void setConvenio(Convenio convenio) {
        this.convenio = convenio;
    }
    
    @Override
    public String toString() {
        // Formatação útil para logs ou testes
        return "Consulta: " + dataHora + " - Dr. " + (medico != null ? medico.getNome() : "N/A");
    }
}