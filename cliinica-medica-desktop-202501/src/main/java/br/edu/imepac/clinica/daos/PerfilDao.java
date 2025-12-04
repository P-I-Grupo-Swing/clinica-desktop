/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.imepac.clinica.daos;

/**
 *
 * @author alcan
 */

import br.edu.imepac.clinica.entidades.Atendente;
import br.edu.imepac.clinica.entidades.Medico;
import br.edu.imepac.clinica.entidades.Pacientes;
import br.edu.imepac.clinica.entidades.Perfil;
import br.edu.imepac.clinica.interfaces.Persistente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PerfilDao extends BaseDao implements Persistente<Perfil> {

    @Override
    public boolean salvar(Perfil entidade) {
        String sql = "INSERT INTO perfis (tipo, id_medico, id_paciente, id_atendente) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, entidade.getTipo());

            if (entidade.getMedico() != null) stmt.setLong(2, entidade.getMedico().getId());
            else stmt.setNull(2, java.sql.Types.INTEGER);

            if (entidade.getPacientes() != null) stmt.setLong(3, entidade.getPacientes().getId());
            else stmt.setNull(3, java.sql.Types.INTEGER);

            if (entidade.getAtendente() != null) stmt.setLong(4, entidade.getAtendente().getId());
            else stmt.setNull(4, java.sql.Types.INTEGER);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao salvar perfil: " + e.getMessage());
            return false;
        } finally {
            fecharRecursos(conn, stmt);
        }
    }

    @Override
    public boolean atualizar(Perfil entidade) {
        String sql = "UPDATE perfis SET tipo=?, id_medico=?, id_paciente=?, id_atendente=? WHERE id=?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, entidade.getTipo());

            if (entidade.getMedico() != null) stmt.setLong(2, entidade.getMedico().getId());
            else stmt.setNull(2, java.sql.Types.INTEGER);

            if (entidade.getPacientes() != null) stmt.setLong(3, entidade.getPacientes().getId());
            else stmt.setNull(3, java.sql.Types.INTEGER);

            if (entidade.getAtendente() != null) stmt.setLong(4, entidade.getAtendente().getId());
            else stmt.setNull(4, java.sql.Types.INTEGER);

            stmt.setLong(5, entidade.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar perfil: " + e.getMessage());
            return false;
        } finally {
            fecharRecursos(conn, stmt);
        }
    }

    @Override
    public boolean excluir(long id) {
        String sql = "DELETE FROM perfis WHERE id = ?";
        
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Perfil buscarPorId(long id) {
     
        return null; 
    }

    @Override
    public List<Perfil> listarTodos() {
      
        String sql = "SELECT p.*, " +
                     "m.id as mid, m.nome as mnome, m.crm as mcrm, " +
                     "pac.id as pid, pac.nome as pnome, pac.cpf as pcpf, " +
                     "at.id as aid, at.nome as anome " +
                     "FROM perfis p " +
                     "LEFT JOIN medicos m ON p.id_medico = m.id " +
                     "LEFT JOIN pacientes pac ON p.id_paciente = pac.id " + // Assumindo tabela 'pacientes'
                     "LEFT JOIN atendentes at ON p.id_atendente = at.id";   // Assumindo tabela 'atendentes'

        List<Perfil> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Perfil p = new Perfil();
                p.setId(rs.getLong("id"));
                p.setTipo(rs.getString("tipo"));

                long idMedico = rs.getLong("mid");
                if (idMedico > 0) {
                    Medico m = new Medico();
                    m.setId(idMedico);
                    m.setNome(rs.getString("mnome"));
                    m.setCrm(rs.getString("mcrm"));
                    p.setMedico(m);
                }

                long idPaciente = rs.getLong("pid");
                if (idPaciente > 0) {
                    Paciente pac = new Paciente();
                    pac.setId(idPaciente);
                    pac.setNome(rs.getString("pnome"));
                    pac.setCpf(rs.getString("pcpf"));
                    p.setPaciente(pac);
                }

                long idAtendente = rs.getLong("aid");
                if (idAtendente > 0) {
                    Atendente at = new Atendente();
                    at.setId(idAtendente);
                    at.setNome(rs.getString("anome"));
                    p.setAtendente(at);
                }

                lista.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar perfis: " + e.getMessage());
        } finally {
            fecharRecursos(conn, stmt, rs);
        }
        return lista;
    }
}
