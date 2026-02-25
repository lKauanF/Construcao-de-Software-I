package br.com.kauan.farias.biblioteca.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.kauan.farias.biblioteca.domain.Livro;
import br.com.kauan.farias.biblioteca.service.LivroService;

@RestController
@RequestMapping("/livros")
public class LivroController {

    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody LivroRequest req) {
        try {
            Livro livro = new Livro(req.titulo, req.autor, req.isbn, req.anoPublicacao);
            Livro criado = livroService.cadastrarLivro(livro);
            return ResponseEntity.status(HttpStatus.CREATED).body(criado);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Livro>> listar() {
        return ResponseEntity.ok(livroService.listarLivros());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(livroService.buscarLivroPorId(id));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Integer id, @RequestBody LivroRequest req) {
        try {
            Livro dados = new Livro(req.titulo, req.autor, req.isbn, req.anoPublicacao);
            Livro atualizado = livroService.atualizarLivro(id, dados);
            return ResponseEntity.ok(atualizado);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PatchMapping("/{id}/indisponivel")
    public ResponseEntity<?> indisponivel(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(livroService.marcarComoIndisponivel(id));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PatchMapping("/{id}/disponivel")
    public ResponseEntity<?> disponivel(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(livroService.marcarComoDisponivel(id));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> remover(@PathVariable Integer id) {
        try {
            livroService.removerLivro(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
}