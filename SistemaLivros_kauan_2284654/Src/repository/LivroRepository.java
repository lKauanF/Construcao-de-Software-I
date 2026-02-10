package repository;

import entity.Livro;

import java.util.List;

public interface LivroRepository {
    Livro salvar(Livro livro);

    List<Livro> listarTodos();

    Livro buscarPorId(int id);

    Livro buscarPorIsbn(String isbn);

    boolean atualizar(Livro livro);

    boolean deletar(int id);

    int contar();
}
