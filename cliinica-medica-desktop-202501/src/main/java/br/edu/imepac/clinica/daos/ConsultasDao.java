/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.imepac.clinica.daos;

import br.edu.imepac.clinica.entidades.Consulta;
import br.edu.imepac.clinica.entidades.Convenio;
import br.edu.imepac.clinica.entidades.Medico;
import br.edu.imepac.clinica.entidades.Paciente;
import br.edu.imepac.clinica.enums.StatusConsulta;
import br.edu.imepac.clinica.interfaces.Persistente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConsultasDao extends BaseDao implements Persistente<Consulta> {

    // --- A NOVA REGRA DE OURO (VALIDAÇÃO) ---
    public boolean verificarConflito(Long idMedico, java.time.LocalDateTime dataHora) {
        // Verifica se existe agendamento ATIVO para este médico nesta hora
        // Ignoramos as consultas CANCELADAS, pois o horário liberou
        String sql = "SELECT COUNT(*) FROM consultas " +
                     "WHERE medico_id = ? AND data_hora = ? " +
                     "AND status != 'CANCELADA'";
        
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idMedico);
            stmt.setTimestamp(2, Timestamp.valueOf(dataHora));
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int quantidade = rs.getInt(1);
                return quantidade > 0; // Se for maior que 0, TEM CONFLITO
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean salvar(Consulta c) {
        // 1. ANTES DE SALVAR, VERIFICA O CONFLITO
        if (verificarConflito(c.getMedico().getId(), c.getDataHora())) {
            System.err.println("ERRO: Já existe consulta para este médico neste horário!");
            return false; // Aborta a gravação
        }

        // 2. Se passou, segue o salvamento normal
        String sql = "INSERT INTO consultas (data_hora, observacoes, status, medico_id, paciente_id, convenio_id) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(c.getDataHora()));
            stmt.setString(2, c.getObservacoes());
            stmt.setString(3, c.getStatus().name());
            stmt.setLong(4, c.getMedico().getId());
            stmt.setLong(5, c.getPaciente().getId());
            stmt.setLong(6, c.getConvenio().getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao agendar consulta: " + e.getMessage());
            return false;
        }
        // O finally é feito automaticamente pelo try-with-resources
    }

    // ... (O Restante do código permanece igual) ...

    @Override
    public boolean atualizar(Consulta c) {
        String sql = "UPDATE consultas SET data_hora=?, observacoes=?, status=?, medico_id=?, paciente_id=?, convenio_id=? WHERE id=?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(c.getDataHora()));
            stmt.setString(2, c.getObservacoes());
            stmt.setString(3, c.getStatus().name());
            stmt.setLong(4, c.getMedico().getId());
            stmt.setLong(5, c.getPaciente().getId());
            stmt.setLong(6, c.getConvenio().getId());
            stmt.setLong(7, c.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean excluir(long id) {
        String sql = "DELETE FROM consultas WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Consulta buscarPorId(long id) {
        String sql = getSqlCompleto() + " WHERE c.id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return criarConsultaDoResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Consulta> listarTodos() {
        String sql = getSqlCompleto() + " ORDER BY c.data_hora DESC";
        List<Consulta> lista = new ArrayList<>();
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) lista.add(criarConsultaDoResultSet(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    public List<Consulta> listarPorData(java.time.LocalDate dataFiltro) {
        String sql = getSqlCompleto() + " WHERE DATE(c.data_hora) = ? ORDER BY c.data_hora";
        List<Consulta> lista = new ArrayList<>();
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, java.sql.Date.valueOf(dataFiltro));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) lista.add(criarConsultaDoResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    // --- MÉTODOS AUXILIARES ---
    private String getSqlCompleto() {
        return "SELECT c.*, m.nome as nome_medico, m.crm, p.nome as nome_paciente, p.cpf, conv.nome_empresa " +
               "FROM consultas c " +
               "INNER JOIN medicos m ON c.medico_id = m.id " +
               "INNER JOIN pacientes p ON c.paciente_id = p.id " +
               "INNER JOIN convenios conv ON c.convenio_id = conv.id";
    }

    private Consulta criarConsultaDoResultSet(ResultSet rs) throws SQLException {
        Consulta c = new Consulta();
        c.setId(rs.getLong("id"));
        if (rs.getTimestamp("data_hora") != null) c.setDataHora(rs.getTimestamp("data_hora").toLocalDateTime());
        c.setObservacoes(rs.getString("observacoes"));
        try { c.setStatus(StatusConsulta.valueOf(rs.getString("status"))); } catch (Exception e) { c.setStatus(StatusConsulta.AGENDADA); }

        Medico m = new Medico(); m.setId(rs.getLong("medico_id")); m.setNome(rs.getString("nome_medico")); m.setCrm(rs.getString("crm"));
        c.setMedico(m);
        Paciente p = new Paciente(); p.setId(rs.getLong("paciente_id")); p.setNome(rs.getString("nome_paciente")); p.setCpf(rs.getString("cpf"));
        c.setPaciente(p);
        Convenio conv = new Convenio(); conv.setId(rs.getLong("convenio_id")); conv.setNomeEmpresa(rs.getString("nome_empresa"));
        c.setConvenio(conv);
        return c;
    }
     public List<Consulta> listarPorMedico(Long idMedico) {
        // Busca todas as consultas daquele médico
        String sql = getSqlCompleto() + " WHERE c.medico_id = ? ORDER BY c.data_hora DESC";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Consulta> lista = new ArrayList<>();

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, idMedico);
            
            rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(criarConsultaDoResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao filtrar por médico: " + e.getMessage());
        } finally {
            fecharRecursos(conn, stmt, rs);
        }
        return lista;
    }
}