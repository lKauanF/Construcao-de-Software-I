package com.recipebook.exception;

public class DuplicateRecipeNameException extends RuntimeException {

    public DuplicateRecipeNameException(String nome) {
        super("Já existe uma receita cadastrada com o nome: " + nome);
    }
}
