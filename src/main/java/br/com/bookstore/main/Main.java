package br.com.bookstore.main;

import br.com.bookstore.dao.LivroDAO;
import br.com.bookstore.dao.LivroDAOSqlite;
import br.com.bookstore.model.Livro;
import java.util.List;
import java.util.Locale;

/** Executa o roteiro de testes descrito na lista oficial da Aula 29. */
public final class Main {
    private Main() { }

    public static void main(String[] args) {
        LivroDAO dao = new LivroDAOSqlite(); // Controller/Main depende da interface.
        List<Livro> novos = List.of(
                new Livro("Clean Code", "Robert C. Martin", 89.90),
                new Livro("Design Patterns", "Erich Gamma e colaboradores", 119.90),
                new Livro("Refactoring", "Martin Fowler", 99.90));

        System.out.println("=== CADASTRANDO LIVROS ===");
        for (Livro livro : novos) {
            dao.salvar(livro);
            System.out.println("ID gerado: " + livro.getId() + " | " + livro.getTitulo());
        }

        // Em um banco novo, os primeiros IDs serão 1, 2 e 3.
        System.out.println("\n=== BUSCA PELO ID 2 ===");
        dao.buscarPorId(2L).ifPresentOrElse(
                livro -> System.out.println("Encontrado: " + livro),
                () -> System.out.println("Livro de ID 2 não encontrado."));

        System.out.println("\n=== BUSCA PELO ID 999 ===");
        dao.buscarPorId(999L).ifPresentOrElse(
                livro -> System.out.println("Encontrado: " + livro),
                () -> System.out.println("Livro de ID 999 não encontrado (sem NullPointerException)."));

        System.out.println("\n=== CATÁLOGO COMPLETO ===");
        System.out.printf("%-6s %-25s %-32s %12s%n", "ID", "TÍTULO", "AUTOR", "PREÇO (R$)");
        System.out.println("-".repeat(80));
        for (Livro livro : dao.listarTodos()) {
            System.out.printf(Locale.forLanguageTag("pt-BR"), "%-6d %-25s %-32s %12.2f%n",
                    livro.getId(), livro.getTitulo(), livro.getAutor(), livro.getPreco());
        }
    }
}
