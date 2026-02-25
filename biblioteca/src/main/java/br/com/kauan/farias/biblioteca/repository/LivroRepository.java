package br.com.kauan.farias.biblioteca.repository;

import java.util.List;
import java.util.Optional;

import br.com.kauan.farias.biblioteca.domain.Livro;

public interface LivroRepository {

    Livro salvar(Livro livro);

    List<Livro> listarTodos();

    Optional<Livro> buscarPorId(Integer id);

    Optional<Livro> buscarPorIsbn(String isbn);

    Livro atualizar(Livro livro);

    void deletar(Integer id);
}