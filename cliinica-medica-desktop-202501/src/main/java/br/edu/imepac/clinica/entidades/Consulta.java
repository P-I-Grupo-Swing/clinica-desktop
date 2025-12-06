package br.edu.imepac.clinica.entidades;

import java.time.LocalDateTime;

public class Consulta {
    private Long id;
    private LocalDateTime dataHora;
    private String observacoes;
    private String status; // Ex: "AGENDADA", "REALIZADA", "CANCELADA"
    
    // Relacionamentos (Objetos)
    private Medico medico;
    private Paciente paciente;
    private Convenio convenio; // Pode ser null (Particular)

    // --- CONSTRUTOR VAZIO (OBRIGATÓRIO PARA O DAO) ---
    public Consulta() {
    }

    // Construtor Completo
    public Consulta(Long id, LocalDateTime dataHora, String observacoes, String status, Medico medico, Paciente paciente, Convenio convenio) {
        this.id = id;
        this.dataHora = dataHora;
        this.observacoes = observacoes;
        this.status = status;
        this.medico = medico;
        this.paciente = paciente;
        this.convenio = convenio;
    }
    
    // Construtor Apenas ID (Útil para vincular em Atendimentos)
    public Consulta(Long id) {
        this.id = id;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Medico getMedico() { return medico; }
    public void setMedico(Medico medico) { this.medico = medico; }

    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }

    public Convenio getConvenio() { return convenio; }
    public void setConvenio(Convenio convenio) { this.convenio = convenio; }
}