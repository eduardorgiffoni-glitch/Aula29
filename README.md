# Aula 29 — BookStore Enterprise (DAO + SQLite)

Implementação da **Lista Oficial Prática da Aula 29**: catálogo de livros em Java 17, com arquitetura em camadas, padrão DAO, JDBC e banco SQLite.

## Requisitos
- JDK 17 ou superior
- Apache Maven 3.8+ (baixa automaticamente o driver SQLite JDBC)

## Executar

```bash
mvn clean compile exec:java
```

Na primeira execução, o programa cria `bookstore.db`, cria a tabela `livros` caso não exista e salva 3 livros. Depois, consulta os IDs 2 e 999 e lista o catálogo. **Em um banco vazio**, os IDs iniciais são 1, 2 e 3. Em execuções seguintes, os dados persistem e novos IDs são gerados, sem apagar os registros existentes.

## Arquitetura

```text
src/main/java/br/com/bookstore/
├── model/Livro.java           # Entidade com validações fail-fast
├── dao/LivroDAO.java          # Contrato de persistência
├── dao/LivroDAOSqlite.java    # JDBC, INSERT, SELECT, mapeamento ResultSet
├── db/ConexaoFactory.java     # Obtenção de conexões SQLite
└── main/Main.java             # Roteiro de demonstração
```

### Regras implementadas
- `Livro` possui `id`, `titulo`, `autor`, `preco`; construtores completo e de negócio; getters, `setId` e `toString`.
- Título com no mínimo dois caracteres, autor obrigatório e preço maior que zero.
- `LivroDAO`: `salvar`, `buscarPorId` (retorna `Optional<Livro>`) e `listarTodos`.
- `LivroDAOSqlite` usa `CREATE TABLE IF NOT EXISTS`, `PreparedStatement` parametrizado, `try-with-resources`, `RETURN_GENERATED_KEYS` e `mapearLinha`.
- `Main` depende da interface DAO e usa `ifPresentOrElse` nas consultas.

## Testes

```bash
mvn test
```

Os testes usam um banco SQLite temporário, sem alterar o `bookstore.db` principal.
