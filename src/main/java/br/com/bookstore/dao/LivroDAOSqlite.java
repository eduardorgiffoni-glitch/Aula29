package br.com.bookstore.dao;

import br.com.bookstore.db.ConexaoFactory;
import br.com.bookstore.model.Livro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/** Implementação SQLite do contrato DAO; toda operação JDBC fica nesta camada. */
public class LivroDAOSqlite implements LivroDAO {
    private static final String CRIAR_TABELA =
            "CREATE TABLE IF NOT EXISTS livros (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "titulo TEXT NOT NULL, " +
            "autor TEXT NOT NULL, " +
            "preco REAL NOT NULL);";

    public LivroDAOSqlite() {
        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(CRIAR_TABELA)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Erro ao preparar o banco de dados: " + e.getMessage(), e);
        }
    }

    @Override
    public void salvar(Livro livro) {
        Objects.requireNonNull(livro, "O livro não pode ser nulo.");
        if (livro.getId() != null) {
            throw new IllegalArgumentException("O livro já possui ID; o método salvar insere novos registros.");
        }
        String sql = "INSERT INTO livros (titulo, autor, preco) VALUES (?, ?, ?);";
        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, livro.getTitulo());
            stmt.setString(2, livro.getAutor());
            stmt.setDouble(3, livro.getPreco());
            if (stmt.executeUpdate() != 1) {
                throw new SQLException("Nenhuma linha foi inserida.");
            }
            try (ResultSet chaves = stmt.getGeneratedKeys()) {
                if (chaves.next()) {
                    livro.setId(chaves.getLong(1));
                } else {
                    throw new SQLException("O banco não retornou o ID gerado.");
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Erro ao salvar livro: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Livro> buscarPorId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("O ID de consulta deve ser positivo.");
        }
        String sql = "SELECT id, titulo, autor, preco FROM livros WHERE id = ?;";
        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearLinha(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Erro ao consultar livro por ID: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Livro> listarTodos() {
        String sql = "SELECT id, titulo, autor, preco FROM livros ORDER BY id;";
        List<Livro> livros = new ArrayList<>();
        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                livros.add(mapearLinha(rs));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Erro ao listar livros: " + e.getMessage(), e);
        }
        return livros;
    }

    private Livro mapearLinha(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String titulo = rs.getString("titulo");
        String autor = rs.getString("autor");
        double preco = rs.getDouble("preco");
        return new Livro(id, titulo, autor, preco);
    }
}
