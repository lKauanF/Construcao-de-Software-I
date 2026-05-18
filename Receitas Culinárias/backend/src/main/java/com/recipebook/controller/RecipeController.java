package com.recipebook.controller;

import com.recipebook.entity.Recipe;
import com.recipebook.exception.DuplicateRecipeNameException;
import com.recipebook.exception.RecipeNotFoundException;
import com.recipebook.repository.RecipeRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/receitas")
@CrossOrigin(origins = "http://localhost:4200")
public class RecipeController {

    private final RecipeRepository recipeRepository;

    public RecipeController(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @GetMapping
    public ResponseEntity<List<Recipe>> listRecipes() {
        List<Recipe> recipes = recipeRepository.findAllByOrderByDataCadastroDesc();
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException(id));

        return ResponseEntity.ok(recipe);
    }

    @PostMapping
    public ResponseEntity<Recipe> createRecipe(@Valid @RequestBody Recipe recipe) {
        if (recipeRepository.existsByNomeIgnoreCase(recipe.getNome())) {
            throw new DuplicateRecipeNameException(recipe.getNome());
        }

        recipe.setId(null);
        Recipe savedRecipe = recipeRepository.save(recipe);

        return ResponseEntity
                .created(URI.create("/api/receitas/" + savedRecipe.getId()))
                .body(savedRecipe);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
        if (!recipeRepository.existsById(id)) {
            throw new RecipeNotFoundException(id);
        }

        recipeRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
