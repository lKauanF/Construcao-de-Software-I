package com.example.mercadinho.controller;

import com.example.mercadinho.dto.MovimentacaoRequest;
import com.example.mercadinho.dto.ProdutoRequest;
import com.example.mercadinho.dto.ProdutoResponse;
import com.example.mercadinho.entity.Produto;
import com.example.mercadinho.exception.EstoqueInsuficienteException;
import com.example.mercadinho.service.ProdutoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProdutoResponse cadastrar(@RequestBody ProdutoRequest request) {
        Produto produto = produtoService.cadastrarProduto(request);
        return ProdutoResponse.fromEntity(produto);
    }

    @GetMapping
    public List<ProdutoResponse> listar() {
        return produtoService.listarProdutos()
                .stream()
                .map(ProdutoResponse::fromEntity)
                .toList();
    }

    @GetMapping("/{id}")
    public ProdutoResponse buscarPorId(@PathVariable Long id) {
        Produto produto = produtoService.buscarPorId(id);
        return ProdutoResponse.fromEntity(produto);
    }

    @PostMapping("/{id}/entrada")
    public ProdutoResponse entrada(@PathVariable Long id, @RequestBody MovimentacaoRequest request) {
        Produto produto = produtoService.registrarEntrada(id, request);
        return ProdutoResponse.fromEntity(produto);
    }

    @PostMapping("/{id}/saida")
    public ProdutoResponse saida(@PathVariable Long id, @RequestBody MovimentacaoRequest request) {
        Produto produto = produtoService.registrarSaida(id, request);
        return ProdutoResponse.fromEntity(produto);
    }

    @ExceptionHandler(EstoqueInsuficienteException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String tratarEstoqueInsuficiente(EstoqueInsuficienteException ex) {
        return ex.getMessage();
    }
}