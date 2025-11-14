package br.com.flaviomarcilio.service;

import br.com.flaviomarcilio.exceptions.ProdutoNaoCadastradoException;
import br.com.flaviomarcilio.model.Produto;
import br.com.flaviomarcilio.repository.ProdutoRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository repository) {
        produtoRepository = repository;
    }

    public List<Produto> buscarTodos() {
        return produtoRepository.listAll();
    }

    public Produto buscarPorTicker(String ticker) {
        Produto produto = produtoRepository.findByTicker(ticker);
        if (produto == null) {
            throw new ProdutoNaoCadastradoException("Produto com ticker " + ticker + " não cadastrado.");
        }
        return produto;
    }

    public void cadastrar(Produto produto) {
        produtoRepository.persist(produto);
    }
}
