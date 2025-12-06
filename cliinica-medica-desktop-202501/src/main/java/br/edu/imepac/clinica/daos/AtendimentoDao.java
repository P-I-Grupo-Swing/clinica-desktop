package br.edu.imepac.clinica.daos;

import br.edu.imepac.clinica.entidades.Atendimento;
import br.edu.imepac.clinica.entidades.Consulta;
import br.edu.imepac.clinica.interfaces.Persistente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AtendimentoDao extends BaseDao implements Persistente<Atendimento> {

    @Override
    public boolean salvar(Atendimento a) {
        String sql = "INSERT INTO prontuarios (historico, receituario, exames, consulta_id) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, a.getHistorico());
            stmt.setString(2, a.getReceituario());
            stmt.setString(3, a.getExames());
            stmt.setLong(4, a.getConsulta().getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao salvar atendimento: " + e.getMessage());
            return false;
        } finally {
            fecharRecursos(conn, stmt);
        }
    }

    @Override
    public boolean atualizar(Atendimento a) {
        String sql = "UPDATE prontuarios SET historico=?, receituario=?, exames=? WHERE id=?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, a.getHistorico());
            stmt.setString(2, a.getReceituario());
            stmt.setString(3, a.getExames());
            stmt.setLong(4, a.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar atendimento: " + e.getMessage());
            return false;
        } finally {
            fecharRecursos(conn, stmt);
        }
    }

    @Override
    public boolean excluir(long id) {
        String sql = "DELETE FROM prontuarios WHERE id=?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao excluir atendimento: " + e.getMessage());
            return false;
        } finally {
            fecharRecursos(conn, stmt);
        }
    }

    @Override
    public Optional<Atendimento> buscarPorId(long id) {
        String sql = "SELECT * FROM prontuarios WHERE id=?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                Atendimento a = new Atendimento();
                a.setId(rs.getLong("id"));
                a.setHistorico(rs.getString("historico"));
                a.setReceituario(rs.getString("receituario"));
                a.setExames(rs.getString("exames"));
                
                // Vincula apenas o ID da consulta
                a.setConsulta(new Consulta(rs.getLong("consulta_id")));
                
                return Optional.of(a);
            }
            return Optional.empty();
        } catch (SQLException e) {
            System.err.println("Erro ao buscar atendimento: " + e.getMessage());
            return Optional.empty();
        } finally {
            fecharRecursos(conn, stmt, rs);
        }
    }

    // Método Extra: Buscar o atendimento específico de uma consulta
    public Optional<Atendimento> buscarPorConsultaId(long consultaId) {
        String sql = "SELECT * FROM prontuarios WHERE consulta_id=?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, consultaId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                Atendimento a = new Atendimento();
                a.setId(rs.getLong("id"));
                a.setHistorico(rs.getString("historico"));
                a.setReceituario(rs.getString("receituario"));
                a.setExames(rs.getString("exames"));
                a.setConsulta(new Consulta(consultaId));
                return Optional.of(a);
            }
            return Optional.empty();
        } catch (SQLException e) {
            System.err.println("Erro ao buscar atendimento por consulta: " + e.getMessage());
            return Optional.empty();
        } finally {
            fecharRecursos(conn, stmt, rs);
        }
    }

    @Override
    public List<Atendimento> listarTodos() {
        return new ArrayList<>(); // Implemente se necessário
    }
}