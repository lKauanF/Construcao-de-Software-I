package com.example.mercadinho.service;

import com.example.mercadinho.dto.MovimentacaoRequest;
import com.example.mercadinho.dto.ProdutoRequest;
import com.example.mercadinho.entity.Movimentacao;
import com.example.mercadinho.entity.Produto;
import com.example.mercadinho.entity.TipoMovimentacao;
import com.example.mercadinho.exception.EstoqueInsuficienteException;
import com.example.mercadinho.repository.MovimentacaoRepository;
import com.example.mercadinho.repository.ProdutoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final MovimentacaoRepository movimentacaoRepository;

    public ProdutoService(ProdutoRepository produtoRepository, MovimentacaoRepository movimentacaoRepository) {
        this.produtoRepository = produtoRepository;
        this.movimentacaoRepository = movimentacaoRepository;
    }

    public Produto cadastrarProduto(ProdutoRequest request) {
        if (request.getNome() == null || request.getNome().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O nome do produto é obrigatório.");
        }

        if (request.getQuantidadeEstoque() == null || request.getQuantidadeEstoque() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A quantidade inicial deve ser maior ou igual a zero.");
        }

        Produto produto = new Produto(request.getNome(), request.getQuantidadeEstoque());
        return produtoRepository.save(produto);
    }

    public List<Produto> listarProdutos() {
        return produtoRepository.findAll();
    }

    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado."));
    }

    public Produto registrarEntrada(Long produtoId, MovimentacaoRequest request) {
        Produto produto = buscarPorId(produtoId);

        if (request.getQuantidade() == null || request.getQuantidade() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A quantidade de entrada deve ser maior que zero.");
        }

        produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() + request.getQuantidade());
        produtoRepository.save(produto);

        Movimentacao movimentacao = new Movimentacao(
                produto,
                TipoMovimentacao.ENTRADA,
                request.getQuantidade(),
                LocalDateTime.now()
        );
        movimentacaoRepository.save(movimentacao);

        return produto;
    }

    public Produto registrarSaida(Long produtoId, MovimentacaoRequest request) {
        Produto produto = buscarPorId(produtoId);

        if (request.getQuantidade() == null || request.getQuantidade() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A quantidade de saída deve ser maior que zero.");
        }

        if (produto.getQuantidadeEstoque() < request.getQuantidade()) {
            throw new EstoqueInsuficienteException("Estoque insuficiente para realizar a saída.");
        }

        produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - request.getQuantidade());
        produtoRepository.save(produto);

        Movimentacao movimentacao = new Movimentacao(
                produto,
                TipoMovimentacao.SAIDA,
                request.getQuantidade(),
                LocalDateTime.now()
        );
        movimentacaoRepository.save(movimentacao);

        return produto;
    }
}