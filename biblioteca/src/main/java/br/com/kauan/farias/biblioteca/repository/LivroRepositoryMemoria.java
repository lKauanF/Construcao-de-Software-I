package br.com.kauan.farias.biblioteca.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import br.com.kauan.farias.biblioteca.domain.Livro;

@Repository
public class LivroRepositoryMemoria implements LivroRepository {

    private final Map<Integer, Livro> banco = new HashMap<>();

    @Override
    public Livro salvar(Livro livro) {
        if (livro == null) {
            throw new IllegalArgumentException("Livro não pode ser nulo.");
        }
        banco.put(livro.getId(), livro);
        return livro;
    }

    @Override
    public List<Livro> listarTodos() {
        return new ArrayList<>(banco.values());
    }

    @Override
    public Optional<Livro> buscarPorId(Integer id) {
        return Optional.ofNullable(banco.get(id));
    }

    @Override
    public Optional<Livro> buscarPorIsbn(String isbn) {
        return banco.values()
                .stream()
                .filter(l -> l.getIsbn().equals(isbn))
                .findFirst();
    }

    @Override
    public Livro atualizar(Livro livro) {
        if (livro == null) {
            throw new IllegalArgumentException("Livro não pode ser nulo.");
        }
        banco.put(livro.getId(), livro);
        return livro;
    }

    @Override
    public void deletar(Integer id) {
        banco.remove(id);
    }
}