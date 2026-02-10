package service;

import entity.Livro;
import repository.LivroRepository;

import java.util.List;

public class LivroService {
    private final LivroRepository repository;

    public LivroService(LivroRepository repository) {
        this.repository = repository;
    }

    public Livro cadastrarLivro(String titulo, String autor, String isbn, int anoPublicacao) {
        try {
            validarDadosNegocio(titulo, autor, isbn, anoPublicacao);

            Livro livro = new Livro(titulo, autor, isbn, anoPublicacao);
            return repository.salvar(livro);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao cadastrar livro: " + e.getMessage(), e);
        }
    }

    public List<Livro> listarLivros() {
        List<Livro> livros = repository.listarTodos();
        livros.sort((l1, l2) -> l1.getTitulo().compareToIgnoreCase(l2.getTitulo()));
        return livros;
    }

    public Livro buscarLivroPorId(int id) {
        Livro livro = repository.buscarPorId(id);
        if (livro == null) {
            throw new RuntimeException("Livro não encontrado com ID: " + id);
        }
        return livro;
    }

    public boolean atualizarLivro(int id, String titulo, String autor, Integer anoPublicacao, Boolean disponivel) {
        Livro livro = repository.buscarPorId(id);

        if (livro == null) {
            throw new RuntimeException("Livro não encontrado com ID: " + id);
        }

        if (titulo != null && !titulo.trim().isEmpty()) {
            livro.setTitulo(titulo);
        }
        if (autor != null && !autor.trim().isEmpty()) {
            livro.setAutor(autor);
        }
        if (anoPublicacao != null && anoPublicacao > 0) {
            livro.setAnoPublicacao(anoPublicacao);
        }
        if (disponivel != null) {
            livro.setDisponivel(disponivel);
        }

        return repository.atualizar(livro);
    }

    public boolean removerLivro(int id) {
        if (repository.buscarPorId(id) == null) {
            throw new RuntimeException("Livro não encontrado com ID: " + id);
        }
        return repository.deletar(id);
    }

    private void validarDadosNegocio(String titulo, String autor, String isbn, int anoPublicacao) {
        if (repository.buscarPorIsbn(isbn) != null) {
            throw new IllegalArgumentException("Já existe livro cadastrado com este ISBN");
        }

        if (titulo == null || titulo.trim().length() < 3) {
            throw new IllegalArgumentException("Título deve ter pelo menos 3 caracteres");
        }

        if (autor == null || autor.trim().length() < 3) {
            throw new IllegalArgumentException("Autor deve ter pelo menos 3 caracteres");
        }

        if (isbn == null || !isbn.trim().matches("\\d{3}-\\d-\\d{4}-\\d{4}-\\d")) {
            throw new IllegalArgumentException("ISBN inválido. Use XXX-X-XXXX-XXXX-X");
        }

        if (anoPublicacao < 1500 || anoPublicacao > 2025) {
            throw new IllegalArgumentException("Ano de publicação deve estar entre 1500 e 2025");
        }
    }
}
