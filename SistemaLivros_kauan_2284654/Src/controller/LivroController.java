package controller;

import entity.Livro;
import service.LivroService;

import java.util.List;

public class LivroController {

    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    public Livro salvarLivro(String titulo,
                             String autor,
                             String isbn,
                             Integer anoPublicacao) {
        if (anoPublicacao == null) {
            throw new IllegalArgumentException("Ano de publicação deve ser informado");
        }
        return this.livroService.cadastrarLivro(titulo, autor, isbn, anoPublicacao);
    }

    public List<Livro> listarLivros() {
        return this.livroService.listarLivros();
    }

    public Livro buscarPorId(Integer id) {
        if (id == null) throw new IllegalArgumentException("ID deve ser informado");
        return this.livroService.buscarLivroPorId(id);
    }

    public boolean deletarLivro(Integer id) {
        if (id == null) throw new IllegalArgumentException("ID deve ser informado");
        return this.livroService.removerLivro(id);
    }

    public boolean atualizarLivro(Integer id,
                                  String titulo,
                                  String autor,
                                  Integer anoPublicacao,
                                  Boolean disponivel) {
        if (id == null) throw new IllegalArgumentException("ID deve ser informado");
        return this.livroService.atualizarLivro(id, titulo, autor, anoPublicacao, disponivel);
    }
}
