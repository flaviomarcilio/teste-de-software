package br.com.flaviomarcilio.fixtures;

import br.com.flaviomarcilio.model.Movimentacao;
import br.com.flaviomarcilio.model.Negociacao;
import br.com.flaviomarcilio.model.Produto;
import br.com.flaviomarcilio.repository.MovimentacaoRepository;
import br.com.flaviomarcilio.repository.NegociacaoRepository;
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

    @Inject
    NegociacaoRepository negociacaoRepository;

    @Transactional
    public void insereProduto(Produto produto) {
        produtoRepository.persist(produto);
    }

    @Transactional
    public void excluiProdutos() {
        produtoRepository.deleteAll();
    }

    @Transactional
    public void insereMovimentacao(Movimentacao movimentacao) {
        movimentacaoRepository.persist(movimentacao);
    }

    @Transactional
    public void excluiMovimentacoes() {
        movimentacaoRepository.deleteAll();
    }

    @Transactional
    public void insereNegociacao(Negociacao negociacao) {
        negociacaoRepository.persist(negociacao);
    }

    @Transactional
    public void excluirNegociacao() {
        negociacaoRepository.deleteAll();
    }
}
