/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.imepac.clinica.daos;
import br.edu.imepac.clinica.entidades.Convenio;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ConvenioDAO {

    // --- SQL CONSTANTS (Para facilitar a manutenção) ---
    private static final String INSERT_SQL = "INSERT INTO convenios (nome, descricao, status) VALUES (?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE convenios SET nome = ?, descricao = ?, status = ? WHERE id = ?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM convenios";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM convenios WHERE id = ?";
    private static final String SELECT_BY_NOME_SQL = "SELECT * FROM convenios WHERE nome = ?";
    // O delete aqui é apenas LÓGICO, não apaga o registro ele so muda o status(ativo/inativo)
    private static final String INATIVAR_SQL = "UPDATE convenios SET status = 'INATIVO' WHERE id = ?";
    private final Connection connection;

    // Construtor recebendo a conexão
    public ConvenioDAO(Connection connection) {
        this.connection = connection;
    }

    // 1. SALVAR (INSERT)
    public Convenio salvar(Convenio convenio) throws SQLException {
        // O Statement.RETURN_GENERATED_KEYS serve para pegar o ID que o banco criou
        try (PreparedStatement stmt = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, convenio.getNome());
            stmt.setString(2, convenio.getDescricao());
            
            // Converte o ENUM para String (ATIVO -> "ATIVO")
            stmt.setString(3, convenio.getStatus().name());
            stmt.executeUpdate();
            // Pega o ID gerado pelo banco e coloca no objeto
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    convenio.setId(generatedKeys.getLong(1));
                }
            }
        }
        return convenio;
    }

    // 2. Alterar (UPDATE)
    public void alterar(Convenio convenio) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(UPDATE_SQL)) {
            stmt.setString(1, convenio.getNome());
            stmt.setString(2, convenio.getDescricao());
            stmt.setString(3, convenio.getStatus().name());
            stmt.setLong(4, convenio.getId());
            stmt.executeUpdate();
        }
    }

    // 3. Inativar (SOFT DELETE)
    public void inativar(Long id) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(INATIVAR_SQL)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    // 4. Procurar por ID
    public Convenio buscarPorId(Long id) throws SQLException {
        Convenio convenio = null;
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_BY_ID_SQL)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    convenio = mapearResultSet(rs);
                }
            }
        }
        return convenio;
    }

    // 5. Listar todos
    public List<Convenio> listarTodos() throws SQLException {
        List<Convenio> lista = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearResultSet(rs));
            }
        }
        return lista;
    }
    
    // 6. Buscar por nome obs:(Para validação de duplicidade)
    public Convenio buscarPorNome(String nome) throws SQLException {
        Convenio convenio = null;
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_BY_NOME_SQL)) {
            stmt.setString(1, nome);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    convenio = mapearResultSet(rs);
                }
            }
        }
        return convenio;
    }
    // Cria o objeto Convenio a partir dos dados do banco
    private Convenio mapearResultSet(ResultSet rs) throws SQLException {
        Convenio c = new Convenio();
        c.setId(rs.getLong("id"));
        c.setNome(rs.getString("nome"));
        c.setDescricao(rs.getString("descricao"));
        
        // Converte a String do banco ("ATIVO") de volta para o Enum (Status.ATIVO)
        String statusStr = rs.getString("status");
        if (statusStr != null) {
            c.setStatus(Convenio.Status.valueOf(statusStr));
        }
        
        return c;
    }
}
