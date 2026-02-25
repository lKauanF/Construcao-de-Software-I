package br.com.kauan.farias.biblioteca.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.kauan.farias.biblioteca.domain.Livro;
import br.com.kauan.farias.biblioteca.repository.LivroRepository;

@Service
public class LivroService {

    private final LivroRepository livroRepository;

    public LivroService(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    public Livro cadastrarLivro(Livro livro) {
        boolean isbnJaExiste = livroRepository.buscarPorIsbn(livro.getIsbn()).isPresent();
        if (isbnJaExiste) {
            throw new IllegalArgumentException("ISBN já cadastrado.");
        }
        return livroRepository.salvar(livro);
    }

    public List<Livro> listarLivros() {
        return livroRepository.listarTodos();
    }

    public Livro buscarLivroPorId(Integer id) {
        return livroRepository.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado."));
    }

    public Livro atualizarLivro(Integer id, Livro dados) {
        Livro existente = buscarLivroPorId(id);

        if (!existente.getIsbn().equals(dados.getIsbn())) {
            throw new IllegalArgumentException("Não é permitido alterar o ISBN.");
        }

        existente.setTitulo(dados.getTitulo());
        existente.setAutor(dados.getAutor());
        existente.setAnoPublicacao(dados.getAnoPublicacao());

        return livroRepository.atualizar(existente);
    }

    public void removerLivro(Integer id) {
        buscarLivroPorId(id);
        livroRepository.deletar(id);
    }

    public Livro marcarComoIndisponivel(Integer id) {
        Livro livro = buscarLivroPorId(id);
        livro.marcarComoIndisponivel();
        return livroRepository.atualizar(livro);
    }

    public Livro marcarComoDisponivel(Integer id) {
        Livro livro = buscarLivroPorId(id);
        livro.marcarComoDisponivel();
        return livroRepository.atualizar(livro);
    }
}