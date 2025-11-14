package br.com.flaviomarcilio.fixtures;

import br.com.flaviomarcilio.model.Movimentacao;
import br.com.flaviomarcilio.model.Produto;
import br.com.flaviomarcilio.repository.MovimentacaoRepository;
import br.com.flaviomarcilio.repository.ProdutoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class Fixture {

    @Inject
    ProdutoRepository produtoRepository;

    @Inject
    MovimentacaoRepository movimentacaoRepository;

    @Transactional
    public void insereProduto(Produto p) {
        produtoRepository.persist(p);
    }

    @Transactional
    public void excluiProdutos() {
        produtoRepository.deleteAll();
    }

    @Transactional
    public void insereMovimentacao(Movimentacao m) {
        movimentacaoRepository.persist(m);
    }

    @Transactional
    public void excluiMovimentacoes() {
        movimentacaoRepository.deleteAll();
    }
}
