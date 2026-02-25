package br.com.kauan.farias.biblioteca.domain;

import java.util.regex.Pattern;

public class Livro {

    private static int contadorId = 1;

    private static final Pattern PADRAO_ISBN = Pattern.compile("^\\d{3}-\\d-\\d{4}-\\d{4}-\\d$");

    private final Integer id;
    private String titulo;
    private String autor;
    private String isbn;
    private Integer anoPublicacao;
    private boolean disponivel;

    public Livro(String titulo, String autor, String isbn, Integer anoPublicacao) {
        this.id = contadorId++;
        this.disponivel = true;

        setTitulo(titulo);
        setAutor(autor);
        setIsbn(isbn);
        setAnoPublicacao(anoPublicacao);
    }

    public Integer getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        if (titulo == null || titulo.trim().length() < 3) {
            throw new IllegalArgumentException("Título inválido. Deve ter no mínimo 3 caracteres.");
        }
        this.titulo = titulo.trim();
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        if (autor == null || autor.trim().length() < 3) {
            throw new IllegalArgumentException("Autor inválido. Deve ter no mínimo 3 caracteres.");
        }
        this.autor = autor.trim();
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        if (isbn == null || !PADRAO_ISBN.matcher(isbn).matches()) {
            throw new IllegalArgumentException("ISBN inválido. Formato esperado: XXX-X-XXXX-XXXX-X");
        }
        this.isbn = isbn;
    }

    public Integer getAnoPublicacao() {
        return anoPublicacao;
    }

    public void setAnoPublicacao(Integer anoPublicacao) {
        if (anoPublicacao == null || anoPublicacao < 1500 || anoPublicacao > 2025) {
            throw new IllegalArgumentException("Ano de publicação inválido. Deve estar entre 1500 e 2025.");
        }
        this.anoPublicacao = anoPublicacao;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void marcarComoDisponivel() {
        this.disponivel = true;
    }

    public void marcarComoIndisponivel() {
        this.disponivel = false;
    }
}