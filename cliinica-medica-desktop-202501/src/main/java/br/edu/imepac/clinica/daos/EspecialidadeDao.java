/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.imepac.clinica.daos;

import br.edu.imepac.clinica.entidades.Especialidade;
import br.edu.imepac.clinica.interfaces.Persistente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EspecialidadeDao extends BaseDao implements Persistente<Especialidade> {

    @Override
    public boolean salvar(Especialidade e) {
        String sql = "INSERT INTO especialidades (nome, descricao) VALUES (?, ?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, e.getNome());
            stmt.setString(2, e.getDescricao());
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("Erro ao salvar: " + ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean atualizar(Especialidade e) {
        String sql = "UPDATE especialidades SET nome=?, descricao=? WHERE id=?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, e.getNome());
            stmt.setString(2, e.getDescricao());
            stmt.setLong(3, e.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            return false;
        }
    }

    @Override
    public boolean excluir(long id) {
        String sql = "DELETE FROM especialidades WHERE id=?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("Erro ao excluir (pode ter médico vinculado): " + ex.getMessage());
            return false;
        }
    }

    @Override
    public Especialidade buscarPorId(long id) {
        String sql = "SELECT * FROM especialidades WHERE id=?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Especialidade(rs.getLong("id"), rs.getString("nome"), rs.getString("descricao"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Especialidade> listarTodos() {
        List<Especialidade> lista = new ArrayList<>();
        String sql = "SELECT * FROM especialidades ORDER BY nome";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new Especialidade(rs.getLong("id"), rs.getString("nome"), rs.getString("descricao")));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }
}