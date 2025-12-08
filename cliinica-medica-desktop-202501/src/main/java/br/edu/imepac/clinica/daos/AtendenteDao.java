/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.imepac.clinica.daos;

import br.edu.imepac.clinica.entidades.Atendente;
import br.edu.imepac.clinica.interfaces.Persistente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gerenciamento de Atendentes e Login.
 */
public class AtendenteDao extends BaseDao implements Persistente<Atendente> {

    /**
     * Método exclusivo para realizar o login no sistema.
     * @param usuario O login digitado (ex: "admin")
     * @param senha A senha digitada
     * @return O objeto Atendente se os dados estiverem corretos, ou null se não encontrar.
     */
    public Atendente autenticar(String usuario, String senha) {
        String sql = "SELECT * FROM atendentes WHERE usuario = ? AND senha = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, usuario);
            stmt.setString(2, senha);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                Atendente a = new Atendente();
                a.setId(rs.getLong("id"));
                a.setNome(rs.getString("nome"));
                a.setUsuario(rs.getString("usuario"));
                a.setSenha(rs.getString("senha"));
                return a;
            }
        } catch (SQLException e) {
            System.err.println("Erro na autenticação: " + e.getMessage());
        } finally {
            fecharRecursos(conn, stmt, rs);
        }
        return null;
    }

    @Override
    public boolean salvar(Atendente a) {
        String sql = "INSERT INTO atendentes (nome, usuario, senha) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, a.getNome());
            stmt.setString(2, a.getUsuario());
            stmt.setString(3, a.getSenha());

            int linhas = stmt.executeUpdate();
            return linhas > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao salvar atendente: " + e.getMessage());
            return false;
        } finally {
            fecharRecursos(conn, stmt);
        }
    }

    @Override
    public boolean atualizar(Atendente a) {
        String sql = "UPDATE atendentes SET nome=?, usuario=?, senha=? WHERE id=?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, a.getNome());
            stmt.setString(2, a.getUsuario());
            stmt.setString(3, a.getSenha());
            stmt.setLong(4, a.getId());

            int linhas = stmt.executeUpdate();
            return linhas > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar atendente: " + e.getMessage());
            return false;
        } finally {
            fecharRecursos(conn, stmt);
        }
    }

    @Override
    public boolean excluir(long id) {
        String sql = "DELETE FROM atendentes WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            
            stmt.setLong(1, id);
            
            int linhas = stmt.executeUpdate();
            return linhas > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao excluir atendente: " + e.getMessage());
            return false;
        } finally {
            fecharRecursos(conn, stmt);
        }
    }

    @Override
    public Atendente buscarPorId(long id) {
        String sql = "SELECT * FROM atendentes WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                Atendente a = new Atendente();
                a.setId(rs.getLong("id"));
                a.setNome(rs.getString("nome"));
                a.setUsuario(rs.getString("usuario"));
                a.setSenha(rs.getString("senha"));
                return a;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar atendente: " + e.getMessage());
        } finally {
            fecharRecursos(conn, stmt, rs);
        }
        return null;
    }

    @Override
    public List<Atendente> listarTodos() {
        String sql = "SELECT * FROM atendentes ORDER BY nome";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Atendente> lista = new ArrayList<>();

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Atendente a = new Atendente();
                a.setId(rs.getLong("id"));
                a.setNome(rs.getString("nome"));
                a.setUsuario(rs.getString("usuario"));
                a.setSenha(rs.getString("senha")); // Atenção: evite mostrar senha em tabelas
                lista.add(a);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar atendentes: " + e.getMessage());
        } finally {
            fecharRecursos(conn, stmt, rs);
        }
        return lista;
    }
}