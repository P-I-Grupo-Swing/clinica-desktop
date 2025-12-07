/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.imepac.clinica.daos;

/**
 *
 * @author Pedro Fernandes
 */
import br.edu.imepac.clinica.entidades.Consulta;
import br.edu.imepac.clinica.entidades.Prontuario;
import br.edu.imepac.clinica.interfaces.Persistente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProntuarioDao extends BaseDao implements Persistente<Prontuario> {

    @Override
    public boolean salvar(Prontuario p) {
        String sql = "INSERT INTO prontuarios (historico, receituario, exames, consulta_id) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, p.getHistorico());
            stmt.setString(2, p.getReceituario());
            stmt.setString(3, p.getExames());
            stmt.setLong(4, p.getConsulta().getId()); // Chave Estrangeira

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao salvar prontuário: " + e.getMessage());
            return false;
        } finally {
            fecharRecursos(conn, stmt);
        }
    }

    @Override
    public boolean atualizar(Prontuario p) {
        String sql = "UPDATE prontuarios SET historico=?, receituario=?, exames=? WHERE id=?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, p.getHistorico());
            stmt.setString(2, p.getReceituario());
            stmt.setString(3, p.getExames());
            stmt.setLong(4, p.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar prontuário: " + e.getMessage());
            return false;
        } finally {
            fecharRecursos(conn, stmt);
        }
    }

    @Override
    public boolean excluir(long id) {
        String sql = "DELETE FROM prontuarios WHERE id=?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Prontuario buscarPorId(long id) {
        return null; // Pouco usado
    }

    /**
     * Método Especial: Busca o prontuário pelo ID da Consulta.
     * Útil para carregar o histórico quando o médico clica na agenda.
     */
    public Prontuario buscarPorIdConsulta(long idConsulta) {
        String sql = "SELECT * FROM prontuarios WHERE consulta_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, idConsulta);
            rs = stmt.executeQuery();

            if (rs.next()) {
                Prontuario p = new Prontuario();
                p.setId(rs.getLong("id"));
                p.setHistorico(rs.getString("historico"));
                p.setReceituario(rs.getString("receituario"));
                p.setExames(rs.getString("exames"));
                
                // Reconstrói o objeto consulta apenas com o ID para referência
                Consulta c = new Consulta();
                c.setId(rs.getLong("consulta_id"));
                p.setConsulta(c);
                
                return p;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar prontuário: " + e.getMessage());
        } finally {
            fecharRecursos(conn, stmt, rs);
        }
        return null;
    }

    @Override
    public List<Prontuario> listarTodos() {
        return new ArrayList<>(); // Prontuário geralmente não se lista todos de uma vez
    }
}