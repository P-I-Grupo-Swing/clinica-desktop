/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.imepac.clinica.daos;

import br.edu.imepac.clinica.entidades.Pacientes;
import br.edu.imepac.clinica.interfaces.Persistente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO responsável pelas operações CRUD da entidade Pacientes.
 * Inclui: Salvar, Atualizar, Excluir e Listar.
 */
public class PacientesDao extends BaseDao implements Persistente<Pacientes> {

    @Override
    public boolean salvar(Pacientes p) {
        String sql = "INSERT INTO pacientes (nome, rg, cpf, telefone) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, p.getNome());
            stmt.setString(2, p.getRg());
            stmt.setString(3, p.getCpf());
            stmt.setString(4, p.getTelefone());

            int linhas = stmt.executeUpdate();
            return linhas > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao salvar paciente: " + e.getMessage());
            return false;
        } finally {
            fecharRecursos(conn, stmt);
        }
    }

    @Override
    public boolean atualizar(Pacientes p) {
        String sql = "UPDATE pacientes SET nome=?, rg=?, cpf=?, telefone=? WHERE id=?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, p.getNome());
            stmt.setString(2, p.getRg());
            stmt.setString(3, p.getCpf());
            stmt.setString(4, p.getTelefone());
            stmt.setLong(5, p.getId());

            int linhas = stmt.executeUpdate();
            return linhas > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar paciente: " + e.getMessage());
            return false;
        } finally {
            fecharRecursos(conn, stmt);
        }
    }

    @Override
    public boolean excluir(long id) {
        String sql = "DELETE FROM pacientes WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, id);
            
            int linhas = stmt.executeUpdate();
            return linhas > 0;
            
        } catch (SQLException e) {
            System.err.println("Erro ao excluir paciente: " + e.getMessage());
            return false;
        } finally {
            fecharRecursos(conn, stmt);
        }
    }

    @Override
    public Pacientes buscarPorId(long id) {
        String sql = "SELECT * FROM pacientes WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Pacientes p = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                p = new Pacientes();
                p.setId(rs.getLong("id"));
                p.setNome(rs.getString("nome"));
                p.setRg(rs.getString("rg"));
                p.setCpf(rs.getString("cpf"));
                p.setTelefone(rs.getString("telefone"));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar paciente por ID: " + e.getMessage());
        } finally {
            fecharRecursos(conn, stmt, rs);
        }
        return p;
    }

    @Override
    public List<Pacientes> listarTodos() {
        System.out.println(">>> DAO: Iniciando listagem de todos os pacientes...");
        
        String sql = "SELECT * FROM pacientes ORDER BY nome";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        List<Pacientes> lista = new ArrayList<>();

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Pacientes p = new Pacientes();
                p.setId(rs.getLong("id"));
                p.setNome(rs.getString("nome"));
                p.setRg(rs.getString("rg"));
                p.setCpf(rs.getString("cpf"));
                p.setTelefone(rs.getString("telefone"));
                
                lista.add(p);
                System.out.println(">>> DAO: Encontrado: " + p.getNome());
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao listar pacientes: " + e.getMessage());
            e.printStackTrace();
        } finally {
            fecharRecursos(conn, stmt, rs);
        }
        
        return lista;
    }
    
    public List<Pacientes> buscarPorNome(String nome) {
        String sql = "SELECT * FROM pacientes WHERE nome LIKE ? ORDER BY nome";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Pacientes> lista = new ArrayList<>();

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + nome + "%");
            rs = stmt.executeQuery();

            while (rs.next()) {
                Pacientes p = new Pacientes();
                p.setId(rs.getLong("id"));
                p.setNome(rs.getString("nome"));
                p.setRg(rs.getString("rg"));
                p.setCpf(rs.getString("cpf"));
                p.setTelefone(rs.getString("telefone"));
                lista.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Erro na busca: " + e.getMessage());
        } finally {
            fecharRecursos(conn, stmt, rs);
        }
        return lista;
    }
}