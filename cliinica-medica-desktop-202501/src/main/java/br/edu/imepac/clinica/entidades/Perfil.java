/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.imepac.clinica.entidades;

public class Perfil {
    private long id;
    private String tipo; // Ex: "MEDICO", "PACIENTE", "ADMIN"
    
    // Objetos vinculados (podem ser nulos)
    private Medico medico;
    private Paciente paciente;
    private Atendente atendente;

    public Perfil() {}
    
    public Perfil(long id, String tipo) {
        this.id = id;
        this.tipo = tipo;
    }

    // Getters e Setters normais
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    // Getters e Setters dos Vínculos
    public Medico getMedico() { return medico; }
    public void setMedico(Medico medico) { this.medico = medico; }

    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }

    public Atendente getAtendente() { return atendente; }
    public void setAtendente(Atendente atendente) { this.atendente = atendente; }

    // Método auxiliar para facilitar na hora de exibir na tela
    @Override
    public String toString() {
        if (medico != null) return tipo + " - " + medico.getNome();
        if (paciente != null) return tipo + " - " + paciente.getNome();
        if (atendente != null) return tipo + " - " + atendente.getNome();
        return tipo + " (Sem vínculo)";
    }
}