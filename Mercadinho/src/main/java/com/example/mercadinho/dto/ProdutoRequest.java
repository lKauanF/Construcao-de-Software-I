package com.example.mercadinho.dto;

public class ProdutoRequest {

    private String nome;
    private Integer quantidadeEstoque;

    public ProdutoRequest() {
    }

    public String getNome() {
        return nome;
    }

    public Integer getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setQuantidadeEstoque(Integer quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }
}