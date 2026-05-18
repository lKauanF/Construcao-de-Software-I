import { CommonModule } from '@angular/common';
import { Component, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Recipe } from '../../models/recipe.model';
import { RecipeService } from '../../services/recipe.service';

@Component({
  selector: 'app-recipe-detail',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './recipe-detail.component.html',
  styleUrl: './recipe-detail.component.css'
})
export class RecipeDetailComponent implements OnInit {
  recipe = signal<Recipe | null>(null);
  isLoading = signal<boolean>(false);
  isDeleting = signal<boolean>(false);
  errorMessage = signal<string>('');

  constructor(
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly recipeService: RecipeService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    if (!id) {
      this.errorMessage.set('Receita inválida.');
      return;
    }

    this.loadRecipe(id);
  }

  loadRecipe(id: number): void {
    this.isLoading.set(true);
    this.errorMessage.set('');

    this.recipeService.getRecipeById(id).subscribe({
      next: (recipe) => {
        this.recipe.set(recipe);
        this.isLoading.set(false);
      },
      error: () => {
        this.errorMessage.set('Receita não encontrada.');
        this.isLoading.set(false);
      }
    });
  }

  deleteRecipe(): void {
    const currentRecipe = this.recipe();

    if (!currentRecipe?.id) {
      return;
    }

    const confirmed = window.confirm(`Deseja realmente excluir a receita "${currentRecipe.nome}"?`);

    if (!confirmed) {
      return;
    }

    this.isDeleting.set(true);

    this.recipeService.deleteRecipe(currentRecipe.id).subscribe({
      next: () => {
        sessionStorage.setItem('recipe-success-message', 'Receita excluída com sucesso.');
        this.router.navigate(['/receitas']);
      },
      error: () => {
        this.errorMessage.set('Não foi possível excluir a receita.');
        this.isDeleting.set(false);
      }
    });
  }
}
