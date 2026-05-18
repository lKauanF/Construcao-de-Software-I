import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  template: `
    <header class="app-header">
      <div class="container header-content">
        <a class="brand" href="/receitas">RecipeBook</a>
        <span>Catálogo de receitas culinárias</span>
      </div>
    </header>

    <main class="container page-content">
      <router-outlet></router-outlet>
    </main>
  `
})
export class AppComponent {}
