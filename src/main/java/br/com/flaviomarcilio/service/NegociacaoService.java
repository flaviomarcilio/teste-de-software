package br.com.flaviomarcilio.service;

import br.com.flaviomarcilio.exceptions.ProdutoNaoCadastradoException;
import br.com.flaviomarcilio.model.Negociacao;
import br.com.flaviomarcilio.model.Produto;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class NegociacaoService {

    private List<Negociacao> negociacoes;
    private ProdutoService produtoService;

    public NegociacaoService(ProdutoService produtoService) {
        this.negociacoes = new ArrayList<>();
        this.produtoService = produtoService;
    }

    public List<Negociacao> buscarTodas() {
        return negociacoes;
    }

    public Negociacao buscarPorId(Long id) {
        return negociacoes.stream()
                .filter(negociacao -> negociacao.getId().equals(id))
                .toList().getFirst();
    }

    public void cadastrar(Negociacao negociacao) {
        String ticker = negociacao.getCodigoNegociacao();
        Produto produto = produtoService.buscarPorTicker(ticker);

        if (produto != null) {
            negociacoes.add(negociacao);
        } else {
            throw new ProdutoNaoCadastradoException();
        }
    }
}
