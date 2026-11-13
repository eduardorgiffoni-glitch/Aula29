package br.com.bookstore;

import br.com.bookstore.dao.LivroDAO;
import br.com.bookstore.dao.LivroDAOSqlite;
import br.com.bookstore.model.Livro;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LivroDAOSqliteTest {
    private Path banco;
    private LivroDAO dao;

    @BeforeEach void iniciar() throws IOException {
        banco = Files.createTempFile("bookstore-test-", ".db");
        System.setProperty("bookstore.jdbc.url", "jdbc:sqlite:" + banco.toAbsolutePath());
        dao = new LivroDAOSqlite();
    }

    @AfterEach void finalizar() throws IOException {
        System.clearProperty("bookstore.jdbc.url");
        Files.deleteIfExists(banco);
    }

    @Test void salvaGeraIdEConsulta() {
        Livro livro = new Livro("Clean Code", "Robert C. Martin", 89.90);
        assertNull(livro.getId());
        dao.salvar(livro);
        assertEquals(1L, livro.getId());
        assertEquals("Clean Code", dao.buscarPorId(1L).orElseThrow().getTitulo());
        assertTrue(dao.buscarPorId(999L).isEmpty());
    }

    @Test void listaLivrosEmOrdemDeId() {
        dao.salvar(new Livro("Livro A", "Autor A", 10));
        dao.salvar(new Livro("Livro B", "Autor B", 20));
        List<Livro> livros = dao.listarTodos();
        assertEquals(2, livros.size());
        assertEquals(1L, livros.get(0).getId());
        assertEquals(2L, livros.get(1).getId());
    }

    @Test void validaDominio() {
        assertThrows(IllegalArgumentException.class, () -> new Livro(" ", "Autor", 10));
        assertThrows(IllegalArgumentException.class, () -> new Livro("A", "Autor", 10));
        assertThrows(IllegalArgumentException.class, () -> new Livro("Título", " ", 10));
        assertThrows(IllegalArgumentException.class, () -> new Livro("Título", "Autor", 0));
        assertThrows(IllegalArgumentException.class, () -> new Livro("Título", "Autor", -2));
        assertThrows(IllegalArgumentException.class, () -> new Livro("Título", "Autor", Double.NaN));
    }
}
