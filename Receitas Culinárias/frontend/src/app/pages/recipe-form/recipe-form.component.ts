import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, signal } from '@angular/core';
import { FormArray, FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ApiErrorResponse, Categoria, Recipe } from '../../models/recipe.model';
import { RecipeService } from '../../services/recipe.service';

@Component({
  selector: 'app-recipe-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './recipe-form.component.html',
  styleUrl: './recipe-form.component.css'
})
export class RecipeFormComponent {
  categories: Categoria[] = ['DOCE', 'SALGADO', 'BEBIDA', 'SOBREMESA'];
  isSaving = signal<boolean>(false);
  serverErrors = signal<Record<string, string>>({});
  generalError = signal<string>('');

  form = this.formBuilder.nonNullable.group({
    nome: ['', [Validators.required, Validators.minLength(3)]],
    categoria: ['DOCE' as Categoria, [Validators.required]],
    tempoPreparo: [1, [Validators.required, Validators.min(1)]],
    porcoes: [1, [Validators.required, Validators.min(1)]],
    ingredientes: this.formBuilder.array([
      this.formBuilder.nonNullable.control('', [Validators.required])
    ]),
    modoPreparo: ['', [Validators.required, Validators.minLength(10)]]
  });

  constructor(
    private readonly formBuilder: FormBuilder,
    private readonly recipeService: RecipeService,
    private readonly router: Router
  ) {}

  get ingredientes(): FormArray {
    return this.form.controls.ingredientes;
  }

  addIngredient(): void {
    this.ingredientes.push(this.formBuilder.nonNullable.control('', [Validators.required]));
  }

  removeIngredient(index: number): void {
    if (this.ingredientes.length === 1) {
      return;
    }

    this.ingredientes.removeAt(index);
  }

  getFieldError(fieldName: string): string {
    const control = this.form.get(fieldName);
    const errors = this.serverErrors();

    if (errors[fieldName]) {
      return errors[fieldName];
    }

    if (!control || !control.touched || !control.errors) {
      return '';
    }

    if (control.errors['required']) {
      return 'Campo obrigatório.';
    }

    if (control.errors['minlength']) {
      const requiredLength = control.errors['minlength'].requiredLength;
      return `Informe pelo menos ${requiredLength} caracteres.`;
    }

    if (control.errors['min']) {
      return `O valor mínimo é ${control.errors['min'].min}.`;
    }

    return 'Campo inválido.';
  }

  getIngredientError(index: number): string {
    const control = this.ingredientes.at(index);

    if (!control.touched || !control.errors) {
      return '';
    }

    return 'Informe o ingrediente ou remova este campo.';
  }

  submit(): void {
    this.form.markAllAsTouched();
    this.serverErrors.set({});
    this.generalError.set('');

    if (this.form.invalid) {
      return;
    }

    const rawValue = this.form.getRawValue();
    const recipe: Recipe = {
      nome: rawValue.nome.trim(),
      categoria: rawValue.categoria,
      tempoPreparo: Number(rawValue.tempoPreparo),
      porcoes: Number(rawValue.porcoes),
      ingredientes: rawValue.ingredientes.map((ingredient) => ingredient.trim()).filter(Boolean),
      modoPreparo: rawValue.modoPreparo.trim()
    };

    this.isSaving.set(true);

    this.recipeService.createRecipe(recipe).subscribe({
      next: () => {
        sessionStorage.setItem('recipe-success-message', 'Receita cadastrada com sucesso.');
        this.router.navigate(['/receitas']);
      },
      error: (error: HttpErrorResponse) => {
        const response = error.error as ApiErrorResponse | undefined;
        this.serverErrors.set(response?.errors ?? {});
        this.generalError.set(response?.message ?? 'Não foi possível cadastrar a receita.');
        this.isSaving.set(false);
      }
    });
  }
}
