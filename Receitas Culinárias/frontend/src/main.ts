import { bootstrapApplication } from '@angular/platform-browser';
import { provideHttpClient } from '@angular/common/http';
import { provideRouter, Routes } from '@angular/router';
import { AppComponent } from './app/app.component';
import { RecipeListComponent } from './app/pages/recipe-list/recipe-list.component';
import { RecipeFormComponent } from './app/pages/recipe-form/recipe-form.component';
import { RecipeDetailComponent } from './app/pages/recipe-detail/recipe-detail.component';

const routes: Routes = [
  { path: '', redirectTo: 'receitas', pathMatch: 'full' },
  { path: 'receitas', component: RecipeListComponent },
  { path: 'receitas/nova', component: RecipeFormComponent },
  { path: 'receitas/:id', component: RecipeDetailComponent },
  { path: '**', redirectTo: 'receitas' }
];

bootstrapApplication(AppComponent, {
  providers: [
    provideRouter(routes),
    provideHttpClient()
  ]
}).catch((error: unknown) => console.error(error));
