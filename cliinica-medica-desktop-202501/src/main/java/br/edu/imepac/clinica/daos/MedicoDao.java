/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.imepac.clinica.daos;

import br.edu.imepac.clinica.entidades.Especialidade;
import br.edu.imepac.clinica.entidades.Medico;
import br.edu.imepac.clinica.interfaces.Persistente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MedicoDao extends BaseDao implements Persistente<Medico> {

    /**
     * Tenta logar como Médico usando CRM e SENHA.
     */
    public Medico autenticar(String crm, String senha) {
        // MUDANÇA: Verifica se o CRM e a Senha batem
        String sql = "SELECT m.*, e.nome as esp_nome FROM medicos m " +
                     "LEFT JOIN especialidades e ON m.especialidade_id = e.id " +
                     "WHERE m.crm = ? AND m.senha = ?";
        
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, crm);
            stmt.setString(2, senha);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return criarMedico(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao logar médico: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean salvar(Medico m) {
        // MUDANÇA: Removemos 'usuario'. Salvamos Nome, CRM, Senha e Especialidade.
        String sql = "INSERT INTO medicos (nome, crm, senha, especialidade_id) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, m.getNome());
            stmt.setString(2, m.getCrm());
            stmt.setString(3, m.getSenha());
            
            // Verifica se o ID vem do objeto Especialidade ou do campo auxiliar
            long espId = (m.getEspecialidade() != null) ? m.getEspecialidade().getId() : m.getEspecialidadeId();
            stmt.setLong(4, espId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao salvar médico: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean atualizar(Medico m) {
        String sql = "UPDATE medicos SET nome=?, crm=?, senha=?, especialidade_id=? WHERE id=?";
        
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, m.getNome());
            stmt.setString(2, m.getCrm());
            stmt.setString(3, m.getSenha());
            
            long espId = (m.getEspecialidade() != null) ? m.getEspecialidade().getId() : m.getEspecialidadeId();
            stmt.setLong(4, espId);
            
            stmt.setLong(5, m.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar médico: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean excluir(long id) {
        String sql = "DELETE FROM medicos WHERE id=?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public List<Medico> listarTodos() {
        String sql = "SELECT m.*, e.nome as esp_nome FROM medicos m " +
                     "LEFT JOIN especialidades e ON m.especialidade_id = e.id " +
                     "ORDER BY m.nome";
        List<Medico> lista = new ArrayList<>();
        
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(criarMedico(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public Medico buscarPorId(long id) {
        String sql = "SELECT m.*, e.nome as esp_nome FROM medicos m " +
                     "LEFT JOIN especialidades e ON m.especialidade_id = e.id WHERE m.id=?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return criarMedico(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Método auxiliar para evitar repetição
    private Medico criarMedico(ResultSet rs) throws SQLException {
        Medico m = new Medico();
        m.setId(rs.getLong("id"));
        m.setNome(rs.getString("nome"));
        m.setCrm(rs.getString("crm"));
        m.setSenha(rs.getString("senha"));
        
        // Removemos a linha m.setUsuario(...), pois não existe mais na classe

        Especialidade e = new Especialidade();
        e.setId(rs.getLong("especialidade_id"));
        e.setNome(rs.getString("esp_nome"));
        m.setEspecialidade(e);
        m.setEspecialidadeId(e.getId());
        
        return m;
    }
}
