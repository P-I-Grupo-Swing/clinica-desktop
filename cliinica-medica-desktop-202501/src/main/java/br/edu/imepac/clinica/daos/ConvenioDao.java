/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.imepac.clinica.daos;
import br.edu.imepac.clinica.entidades.Convenio;
import br.edu.imepac.clinica.enums.Status; // Importante!
import br.edu.imepac.clinica.interfaces.Persistente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO atualizado com suporte a Status (Ativo/Inativo).
 */
public class ConvenioDao extends BaseDao implements Persistente<Convenio> {

    @Override
    public boolean salvar(Convenio c) {
        // Adicionada a coluna 'status'
        String sql = "INSERT INTO convenios (nome_empresa, cnpj, telefone, status) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, c.getNomeEmpresa());
            stmt.setString(2, c.getCnpj());
            stmt.setString(3, c.getTelefone());
            
            // Converte o Enum para String (Ex: "ATIVO")
            stmt.setString(4, c.getStatus().name());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao salvar convênio: " + e.getMessage());
            return false;
        } finally {
            fecharRecursos(conn, stmt);
        }
    }

    @Override
    public boolean atualizar(Convenio c) {
        // Atualiza também o status
        String sql = "UPDATE convenios SET nome_empresa=?, cnpj=?, telefone=?, status=? WHERE id=?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, c.getNomeEmpresa());
            stmt.setString(2, c.getCnpj());
            stmt.setString(3, c.getTelefone());
            stmt.setString(4, c.getStatus().name()); // Atualiza status
            
            stmt.setLong(5, c.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar convênio: " + e.getMessage());
            return false;
        } finally {
            fecharRecursos(conn, stmt);
        }
    }

    @Override
    public boolean excluir(long id) {
        // DELETE físico (Cuidado: pode quebrar histórico de consultas)
        // Dica: Na tela, ao invés de chamar excluir(), você pode chamar atualizar() mudando o status para INATIVO
        String sql = "DELETE FROM convenios WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao excluir convênio: " + e.getMessage());
            return false;
        } finally {
            fecharRecursos(conn, stmt);
        }
    }

    @Override
    public Convenio buscarPorId(long id) {
        String sql = "SELECT * FROM convenios WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return criarConvenio(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar convênio: " + e.getMessage());
        } finally {
            fecharRecursos(conn, stmt, rs);
        }
        return null;
    }

    @Override
    public List<Convenio> listarTodos() {
        String sql = "SELECT * FROM convenios ORDER BY nome_empresa";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Convenio> lista = new ArrayList<>();

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(criarConvenio(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar convênios: " + e.getMessage());
        } finally {
            fecharRecursos(conn, stmt, rs);
        }
        return lista;
    }
    
    // Método auxiliar para evitar repetição de código
    private Convenio criarConvenio(ResultSet rs) throws SQLException {
        Convenio c = new Convenio();
        c.setId(rs.getLong("id"));
        c.setNomeEmpresa(rs.getString("nome_empresa"));
        c.setCnpj(rs.getString("cnpj"));
        c.setTelefone(rs.getString("telefone"));
        
        // Converte String do banco ("ATIVO") para Enum (Status.ATIVO)
        try {
            String statusStr = rs.getString("status");
            if (statusStr != null) {
                c.setStatus(Status.valueOf(statusStr));
            }
        } catch (IllegalArgumentException e) {
            // Se o banco tiver algo estranho, assume ATIVO por segurança
            c.setStatus(Status.ATIVO);
        }
        
        return c;
    }
}