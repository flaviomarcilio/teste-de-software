package br.com.flaviomarcilio.service;

import br.com.flaviomarcilio.exceptions.ProdutoNaoCadastradoException;
import br.com.flaviomarcilio.model.Negociacao;
import br.com.flaviomarcilio.model.Produto;
import br.com.flaviomarcilio.repository.NegociacaoRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class NegociacaoService {

    private final NegociacaoRepository negociacaoRepository;
    private final ProdutoService produtoService;

    public NegociacaoService(NegociacaoRepository negociacaoRepository, ProdutoService produtoService) {
        this.negociacaoRepository = negociacaoRepository;
        this.produtoService = produtoService;
    }

    public List<Negociacao> buscarTodas() {
        return negociacaoRepository.listAll();
    }

    public Negociacao buscarPorId(Long id) {
        return negociacaoRepository.findById(id);
    }

    public void cadastrar(Negociacao negociacao) {
        String ticker = negociacao.getCodigoNegociacao();

        try {
            Produto produto = produtoService.buscarPorTicker(ticker);
            negociacaoRepository.persist(negociacao);
        } catch (ProdutoNaoCadastradoException e) {
            throw new ProdutoNaoCadastradoException();
        }
    }
}
