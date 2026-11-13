package br.com.bookstore.dao;

import java.util.List;
import java.util.Optional;
import br.com.bookstore.model.Livro;

/** Contrato de persistência: não expõe detalhes de JDBC. */
public interface LivroDAO {
    void salvar(Livro livro);
    Optional<Livro> buscarPorId(Long id);
    List<Livro> listarTodos();
}
