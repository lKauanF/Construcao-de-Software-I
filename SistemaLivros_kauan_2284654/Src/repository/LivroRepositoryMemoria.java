package repository;

import entity.Livro;

import java.util.ArrayList;
import java.util.List;

public class LivroRepositoryMemoria implements LivroRepository {
    private final List<Livro> livros = new ArrayList<>();

    @Override
    public Livro salvar(Livro livro) {
        if (livro == null) {
            throw new IllegalArgumentException("Livro não pode ser nulo");
        }

        if (buscarPorIsbn(livro.getIsbn()) != null) {
            throw new IllegalArgumentException("Já existe livro com este ISBN");
        }

        livros.add(livro);
        return livro;
    }

    @Override
    public List<Livro> listarTodos() {
        return new ArrayList<>(livros);
    }

    @Override
    public Livro buscarPorId(int id) {
        return livros.stream()
                .filter(l -> l.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Livro buscarPorIsbn(String isbn) {
        if (isbn == null) return null;
        String i = isbn.trim();

        for (Livro livro : livros) {
            if (livro.getIsbn().equals(i)) {
                return livro;
            }
        }
        return null;
    }

    @Override
    public boolean atualizar(Livro livro) {
        if (livro == null) return false;

        for (int idx = 0; idx < livros.size(); idx++) {
            Livro existente = livros.get(idx);

            if (existente.getId() == livro.getId()) {
                Livro porIsbn = buscarPorIsbn(livro.getIsbn());
                if (porIsbn != null && porIsbn.getId() != livro.getId()) {
                    throw new IllegalArgumentException("Já existe livro com este ISBN");
                }

                livros.set(idx, livro);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean deletar(int id) {
        for (int idx = 0; idx < livros.size(); idx++) {
            if (livros.get(idx).getId() == id) {
                livros.remove(idx);
                return true;
            }
        }
        return false;
    }

    @Override
    public int contar() {
        return livros.size();
    }
}
