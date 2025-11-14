package br.com.flaviomarcilio.service;

import br.com.flaviomarcilio.model.Movimentacao;
import br.com.flaviomarcilio.repository.MovimentacaoRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class MovimentacaoService {

    private MovimentacaoRepository movimentacaoRepository;

    public MovimentacaoService(MovimentacaoRepository movimentacaoRepository) {
        this.movimentacaoRepository = movimentacaoRepository;
    }

    public List<Movimentacao> buscarTodas() {
        return movimentacaoRepository.listAll();
    }

    public Movimentacao buscarPorId(Long id) {
        return movimentacaoRepository.findById(id);
    }

    public void cadastrar(Movimentacao movimentacao) {
        movimentacaoRepository.persist(movimentacao);
    }
}
