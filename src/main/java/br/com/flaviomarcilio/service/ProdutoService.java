package br.com.flaviomarcilio.service;

import br.com.flaviomarcilio.model.Produto;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ProdutoService {

    private List<Produto> produtos;

    public ProdutoService() {
        produtos = new ArrayList<>();
    }

    public List<Produto> buscarTodos() {
        return this.produtos;
    }

    public Produto buscarPorTicker(String ticker) {
        return produtos.stream()
                .filter(produto -> produto.getCodigoNegociacao().equals(ticker))
                .toList().getFirst();
    }

    public void cadastrar(Produto produto) {
        produtos.add(produto);
    }
}
