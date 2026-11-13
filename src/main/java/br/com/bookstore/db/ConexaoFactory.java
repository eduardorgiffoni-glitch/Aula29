package br.com.bookstore.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Centraliza o caminho e a criação de conexões SQLite. */
public final class ConexaoFactory {
    private static final String URL_PADRAO = "jdbc:sqlite:bookstore.db";

    private ConexaoFactory() { }

    public static Connection obterConexao() throws SQLException {
        String url = System.getProperty("bookstore.jdbc.url", URL_PADRAO);
        return DriverManager.getConnection(url);
    }
}
