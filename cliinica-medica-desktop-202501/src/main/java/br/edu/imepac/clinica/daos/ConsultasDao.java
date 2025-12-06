package br.edu.imepac.clinica.daos;

import br.edu.imepac.clinica.entidades.Consulta;
import br.edu.imepac.clinica.entidades.Convenio;
import br.edu.imepac.clinica.entidades.Medico;
import br.edu.imepac.clinica.entidades.Paciente;
import br.edu.imepac.clinica.interfaces.Persistente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class ConsultasDao extends BaseDao implements Persistente<Consulta> {

    @Override
    public boolean salvar(Consulta c) {
        String sql = "INSERT INTO consultas (data_hora, observacoes, status, medico_id, paciente_id, convenio_id) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);

            stmt.setTimestamp(1, Timestamp.valueOf(c.getDataHora()));
            stmt.setString(2, c.getObservacoes());
            stmt.setString(3, c.getStatus());
            stmt.setLong(4, c.getMedico().getId());
            stmt.setLong(5, c.getPaciente().getId());

            if (c.getConvenio() != null && c.getConvenio().getId() != null) {
                stmt.setLong(6, (long) c.getConvenio().getId());
            } else {
                stmt.setNull(6, java.sql.Types.BIGINT);
            }

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao salvar consulta: " + e.getMessage());
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
            stmt.setString(3, c.getStatus());
            stmt.setLong(4, c.getMedico().getId());
            stmt.setLong(5, c.getPaciente().getId());

            if (c.getConvenio() != null && c.getConvenio().getId() != null) {
                stmt.setLong(6, (long) c.getConvenio().getId());
            } else {
                stmt.setNull(6, java.sql.Types.BIGINT);
            }
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
        String sql = "DELETE FROM consultas WHERE id=?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao excluir consulta: " + e.getMessage());
            return false;
        } finally {
            fecharRecursos(conn, stmt);
        }
    }

    @Override
    public Optional<Consulta> buscarPorId(long id) {
        String sql = "SELECT c.*, m.nome as nome_medico, p.nome as nome_paciente, cv.nome_empresa " +
                     "FROM consultas c " +
                     "INNER JOIN medicos m ON c.medico_id = m.id " +
                     "INNER JOIN pacientes p ON c.paciente_id = p.id " +
                     "LEFT JOIN convenios cv ON c.convenio_id = cv.id " +
                     "WHERE c.id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapearConsulta(rs));
            }
            return Optional.empty();

        } catch (SQLException e) {
            System.err.println("Erro ao buscar consulta: " + e.getMessage());
            return Optional.empty();
        } finally {
            fecharRecursos(conn, stmt, rs);
        }
    }

    @Override
    public List<Consulta> listarTodos() {
        String sql = "SELECT c.*, m.nome as nome_medico, p.nome as nome_paciente, cv.nome_empresa " +
                     "FROM consultas c " +
                     "INNER JOIN medicos m ON c.medico_id = m.id " +
                     "INNER JOIN pacientes p ON c.paciente_id = p.id " +
                     "LEFT JOIN convenios cv ON c.convenio_id = cv.id " +
                     "ORDER BY c.data_hora DESC";
        
        List<Consulta> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(mapearConsulta(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar consultas: " + e.getMessage());
        } finally {
            fecharRecursos(conn, stmt, rs);
        }
        return lista;
    }

    private Consulta mapearConsulta(ResultSet rs) throws SQLException {
        Consulta c = new Consulta();
        c.setId(rs.getLong("id"));
        c.setDataHora(rs.getTimestamp("data_hora").toLocalDateTime());
        c.setObservacoes(rs.getString("observacoes"));
        c.setStatus(rs.getString("status"));

        // OBS: Medico e Paciente PRECISAM ter construtor vazio
        Medico m = new Medico();
        m.setId(rs.getLong("medico_id"));
        m.setNome(rs.getString("nome_medico"));
        c.setMedico(m);

        Paciente p = new Paciente();
        p.setId(rs.getLong("paciente_id"));
        p.setNome(rs.getString("nome_paciente"));
        c.setPaciente(p);

        long convenioId = rs.getLong("convenio_id");
        if (!rs.wasNull()) {
            Convenio cv = new Convenio();
            cv.setId(convenioId);
            cv.setNomeEmpresa(rs.getString("nome_empresa"));
            c.setConvenio(cv);
        }
        return c;
    }
}