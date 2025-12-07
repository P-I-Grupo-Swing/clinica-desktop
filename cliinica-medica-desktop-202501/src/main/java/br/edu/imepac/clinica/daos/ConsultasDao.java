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

    @Override
    public boolean salvar(Consulta c) {
        String sql = "INSERT INTO consultas (data_hora, observacoes, status, medico_id, paciente_id, convenio_id) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);

            // 1. Data e Hora (Conversão Java -> SQL)
            stmt.setTimestamp(1, Timestamp.valueOf(c.getDataHora()));
            
            // 2. Texto
            stmt.setString(2, c.getObservacoes());
            
            // 3. Enum -> String
            stmt.setString(3, c.getStatus().name());

            // 4. Chaves Estrangeiras (Pega os IDs dos objetos)
            stmt.setLong(4, c.getMedico().getId());
            stmt.setLong(5, c.getPaciente().getId());
            stmt.setLong(6, c.getConvenio().getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao agendar consulta: " + e.getMessage());
            return false;
        } finally {
            fecharRecursos(conn, stmt);
        }
    }

    @Override
    public boolean atualizar(Consulta c) {
        String sql = "UPDATE consultas SET data_hora=?, observacoes=?, status=?, medico_id=?, paciente_id=?, convenio_id=? WHERE id=?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);

            stmt.setTimestamp(1, Timestamp.valueOf(c.getDataHora()));
            stmt.setString(2, c.getObservacoes());
            stmt.setString(3, c.getStatus().name());
            stmt.setLong(4, c.getMedico().getId());
            stmt.setLong(5, c.getPaciente().getId());
            stmt.setLong(6, c.getConvenio().getId());
            stmt.setLong(7, c.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar consulta: " + e.getMessage());
            return false;
        } finally {
            fecharRecursos(conn, stmt);
        }
    }

    @Override
    public boolean excluir(long id) {
        String sql = "DELETE FROM consultas WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao cancelar consulta: " + e.getMessage());
            return false;
        } finally {
            fecharRecursos(conn, stmt);
        }
    }

    @Override
    public Consulta buscarPorId(long id) {
        // Reutiliza a lógica do JOIN para buscar um só
        String sql = getSqlCompleto() + " WHERE c.id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return criarConsultaDoResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar consulta: " + e.getMessage());
        } finally {
            fecharRecursos(conn, stmt, rs);
        }
        return null;
    }

    @Override
    public List<Consulta> listarTodos() {
        String sql = getSqlCompleto() + " ORDER BY c.data_hora DESC";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Consulta> lista = new ArrayList<>();

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(criarConsultaDoResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar consultas: " + e.getMessage());
            e.printStackTrace();
        } finally {
            fecharRecursos(conn, stmt, rs);
        }
        return lista;
    }
    
    // --- MÉTODOS AUXILIARES (A MÁGICA ACONTECE AQUI) ---

    // SQL com JOIN para trazer os nomes em vez de só números
    private String getSqlCompleto() {
        return "SELECT c.*, " +
               "m.nome as nome_medico, m.crm, " +
               "p.nome as nome_paciente, p.cpf, " +
               "conv.nome_empresa " +
               "FROM consultas c " +
               "INNER JOIN medicos m ON c.medico_id = m.id " +
               "INNER JOIN pacientes p ON c.paciente_id = p.id " +
               "INNER JOIN convenios conv ON c.convenio_id = conv.id";
    }

    // Monta o objeto completo lendo do banco
    private Consulta criarConsultaDoResultSet(ResultSet rs) throws SQLException {
        Consulta c = new Consulta();
        c.setId(rs.getLong("id"));
        c.setDataHora(rs.getTimestamp("data_hora").toLocalDateTime());
        c.setObservacoes(rs.getString("observacoes"));
        
        // Status Enum
        try {
            c.setStatus(StatusConsulta.valueOf(rs.getString("status")));
        } catch (Exception e) {
            c.setStatus(StatusConsulta.AGENDADA);
        }

        // Montar Médico (Parcial, só o que veio no JOIN)
        Medico m = new Medico();
        m.setId(rs.getLong("medico_id"));
        m.setNome(rs.getString("nome_medico"));
        m.setCrm(rs.getString("crm"));
        c.setMedico(m);

        // Montar Paciente
        Paciente p = new Paciente();
        p.setId(rs.getLong("paciente_id"));
        p.setNome(rs.getString("nome_paciente"));
        p.setCpf(rs.getString("cpf"));
        c.setPaciente(p);

        // Montar Convênio
        Convenio conv = new Convenio();
        conv.setId(rs.getLong("convenio_id"));
        conv.setNomeEmpresa(rs.getString("nome_empresa"));
        c.setConvenio(conv);

        return c;
    }
}
