import { CommonModule } from '@angular/common';
import { Component, OnInit, computed, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { Recipe } from '../../models/recipe.model';
import { RecipeService } from '../../services/recipe.service';

@Component({
  selector: 'app-recipe-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './recipe-list.component.html',
  styleUrl: './recipe-list.component.css'
})
export class RecipeListComponent implements OnInit {
  recipes = signal<Recipe[]>([]);
  searchTerm = signal<string>('');
  isLoading = signal<boolean>(false);
  successMessage = signal<string>('');
  errorMessage = signal<string>('');

  filteredRecipes = computed(() => {
    const term = this.searchTerm().trim().toLowerCase();

    if (!term) {
      return this.recipes();
    }

    return this.recipes().filter((recipe) =>
      recipe.nome.toLowerCase().includes(term)
    );
  });

  constructor(private readonly recipeService: RecipeService) {}

  ngOnInit(): void {
    this.loadRecipes();
    this.loadSuccessMessage();
  }

  loadRecipes(): void {
    this.isLoading.set(true);
    this.errorMessage.set('');

    this.recipeService.listRecipes().subscribe({
      next: (recipes) => {
        this.recipes.set(recipes);
        this.isLoading.set(false);
      },
      error: () => {
        this.errorMessage.set('Não foi possível carregar as receitas. Verifique se o backend está rodando na porta 8080.');
        this.isLoading.set(false);
      }
    });
  }

  updateSearchTerm(value: string): void {
    this.searchTerm.set(value);
  }

  private loadSuccessMessage(): void {
    const message = sessionStorage.getItem('recipe-success-message');

    if (!message) {
      return;
    }

    this.successMessage.set(message);
    sessionStorage.removeItem('recipe-success-message');

    setTimeout(() => {
      this.successMessage.set('');
    }, 4000);
  }
}
