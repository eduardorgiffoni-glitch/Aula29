package br.com.bookstore.model;

import java.util.Locale;

/** Entidade de domínio independente de SQL e infraestrutura. */
public class Livro {
    private Long id;
    private final String titulo;
    private final String autor;
    private final double preco;

    public Livro(Long id, String titulo, String autor, double preco) {
        if (titulo == null || titulo.trim().length() < 2) {
            throw new IllegalArgumentException("O título deve conter pelo menos 2 caracteres.");
        }
        if (autor == null || autor.trim().isEmpty()) {
            throw new IllegalArgumentException("O autor é obrigatório.");
        }
        if (!Double.isFinite(preco) || preco <= 0.0) {
            throw new IllegalArgumentException("O preço de venda deve ser estritamente positivo.");
        }
        if (id != null && id <= 0) {
            throw new IllegalArgumentException("O ID deve ser positivo.");
        }
        this.id = id;
        this.titulo = titulo.trim();
        this.autor = autor.trim();
        this.preco = preco;
    }

    public Livro(String titulo, String autor, double preco) {
        this(null, titulo, autor, preco);
    }

    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public double getPreco() { return preco; }

    /** Único setter: sincroniza a chave primária gerada pelo banco. */
    public void setId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("O ID gerado deve ser positivo.");
        }
        if (this.id != null && !this.id.equals(id)) {
            throw new IllegalStateException("O ID de um livro persistido não pode ser substituído.");
        }
        this.id = id;
    }

    @Override
    public String toString() {
        return String.format(Locale.forLanguageTag("pt-BR"),
                "Livro{id=%s, titulo='%s', autor='%s', preco=R$ %.2f}", id, titulo, autor, preco);
    }
}
