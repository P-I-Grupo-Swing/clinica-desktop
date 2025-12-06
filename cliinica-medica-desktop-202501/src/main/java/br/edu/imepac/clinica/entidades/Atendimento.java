package br.edu.imepac.clinica.entidades;

public class Atendimento {
    private Long id;
    private String historico;
    private String receituario;
    private String exames;
    
    // Relacionamento 1:1 com Consulta
    private Consulta consulta;

    // --- CONSTRUTOR VAZIO (OBRIGATÓRIO) ---
    public Atendimento() {
    }

    public Atendimento(Long id, String historico, String receituario, String exames, Consulta consulta) {
        this.id = id;
        this.historico = historico;
        this.receituario = receituario;
        this.exames = exames;
        this.consulta = consulta;
    }

    // Getters e Setters
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