package entity;

import java.time.LocalDate;
import java.util.Objects;

public class Livro {
    private static int contadorId = 1;

    private int id;
    private String titulo;
    private String autor;
    private String isbn;
    private int anoPublicacao;
    private boolean disponivel;
    private LocalDate dataCadastro;

    public Livro(String titulo, String autor, String isbn, int anoPublicacao) {
        this.id = contadorId++;
        this.disponivel = true;
        this.dataCadastro = LocalDate.now();

        setTitulo(titulo);
        setAutor(autor);
        setIsbn(isbn);
        setAnoPublicacao(anoPublicacao);
    }

    public void setTitulo(String titulo) {
        if (titulo == null || titulo.trim().length() < 3) {
            throw new IllegalArgumentException("Título deve ter pelo menos 3 caracteres");
        }
        this.titulo = titulo.trim();
    }

    public void setAutor(String autor) {
        if (autor == null || autor.trim().length() < 3) {
            throw new IllegalArgumentException("Autor deve ter pelo menos 3 caracteres");
        }
        this.autor = autor.trim();
    }

    public void setIsbn(String isbn) {
        if (isbn == null) {
            throw new IllegalArgumentException("ISBN inválido");
        }

        String i = isbn.trim();
        if (!i.matches("\\d{3}-\\d-\\d{4}-\\d{4}-\\d")) {
            throw new IllegalArgumentException("ISBN deve estar no formato XXX-X-XXXX-XXXX-X");
        }

        this.isbn = i;
    }

    public void setAnoPublicacao(int anoPublicacao) {
        if (anoPublicacao < 1500 || anoPublicacao > 2025) {
            throw new IllegalArgumentException("Ano de publicação deve estar entre 1500 e 2025");
        }
        this.anoPublicacao = anoPublicacao;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getAnoPublicacao() {
        return anoPublicacao;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public String getIsbnFormatado() {
        return isbn;
    }

    @Override
    public String toString() {
        return String.format(
                "Livro{id=%d, titulo='%s', autor='%s', isbn='%s', anoPublicacao=%d, disponivel=%s, dataCadastro=%s}",
                id, titulo, autor, getIsbnFormatado(), anoPublicacao, disponivel, dataCadastro
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Livro livro = (Livro) obj;
        return Objects.equals(isbn, livro.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }
}
