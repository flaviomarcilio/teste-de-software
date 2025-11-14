package br.com.flaviomarcilio.fixtures;

import br.com.flaviomarcilio.model.Produto;
import br.com.flaviomarcilio.repository.ProdutoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class Fixture {

    @Inject
    ProdutoRepository produtoRepository;

    @Transactional
    public void insereProduto(Produto p) {
        produtoRepository.persist(p);
    }

    @Transactional
    public void excluiProdutos() {
        produtoRepository.deleteAll();
    }
}
