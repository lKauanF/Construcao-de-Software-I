package com.recipebook.exception;

public class RecipeNotFoundException extends RuntimeException {

    public RecipeNotFoundException(Long id) {
        super("Receita não encontrada com o id: " + id);
    }
}
