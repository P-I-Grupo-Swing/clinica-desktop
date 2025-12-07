/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.imepac.clinica.daos;

import br.edu.imepac.clinica.entidades.Paciente;
import br.edu.imepac.clinica.interfaces.Persistente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PacienteDao extends BaseDao implements Persistente<Paciente> {

    @Override
    public boolean salvar(Paciente p) {
        String sql = "INSERT INTO pacientes (nome, data_nascimento, cpf, telefone) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, p.getNome());
            
            if (p.getDataNascimento() != null) {
                stmt.setDate(2, java.sql.Date.valueOf(p.getDataNascimento()));
            } else {
                stmt.setNull(2, java.sql.Types.DATE);
            }
            
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
    public boolean atualizar(Paciente p) {
        String sql = "UPDATE pacientes SET nome=?, data_nascimento=?, cpf=?, telefone=? WHERE id=?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, p.getNome());
            
            if (p.getDataNascimento() != null) {
                stmt.setDate(2, java.sql.Date.valueOf(p.getDataNascimento()));
            } else {
                stmt.setNull(2, java.sql.Types.DATE);
            }
            
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
    public Paciente buscarPorId(long id) {
        String sql = "SELECT * FROM pacientes WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Paciente p = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                p = criarPacienteDoResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar paciente por ID: " + e.getMessage());
        } finally {
            fecharRecursos(conn, stmt, rs);
        }
        return p;
    }

    @Override
    public List<Paciente> listarTodos() {
        String sql = "SELECT * FROM pacientes ORDER BY nome";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        List<Paciente> lista = new ArrayList<>();

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(criarPacienteDoResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao listar pacientes: " + e.getMessage());
            e.printStackTrace();
        } finally {
            fecharRecursos(conn, stmt, rs);
        }
        
        return lista;
    }
    
    public List<Paciente> listarPorConvenio(long idConvenio) {
        System.out.println(">>> RELATÓRIO: Buscando pacientes do convênio ID: " + idConvenio);
        
        String sql = "SELECT DISTINCT p.* FROM pacientes p " +
                     "INNER JOIN consultas c ON p.id = c.paciente_id " +
                     "WHERE c.convenio_id = ? " +
                     "ORDER BY p.nome";
        
        List<Paciente> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, idConvenio);
            
            rs = stmt.executeQuery();

            while (rs.next()) {
                Paciente p = criarPacienteDoResultSet(rs);
                lista.add(p);
                System.out.println(">>> Encontrado: " + p.getNome());
            }
        } catch (SQLException e) {
            System.err.println("Erro no relatório: " + e.getMessage());
        } finally {
            fecharRecursos(conn, stmt, rs);
        }
        return lista;
    }

    // Método auxiliar
    private Paciente criarPacienteDoResultSet(ResultSet rs) throws SQLException {
        Paciente p = new Paciente();
        p.setId(rs.getLong("id"));
        p.setNome(rs.getString("nome"));
        
        if (rs.getDate("data_nascimento") != null) {
            p.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
        }
        
        p.setCpf(rs.getString("cpf"));
        p.setTelefone(rs.getString("telefone"));
        return p;
    }
}