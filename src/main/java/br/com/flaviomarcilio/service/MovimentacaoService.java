package br.com.flaviomarcilio.service;

import br.com.flaviomarcilio.model.Movimentacao;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class MovimentacaoService {

    private List<Movimentacao> movimentacoes;

    public MovimentacaoService() {
        this.movimentacoes = new ArrayList<>();
    }

    public List<Movimentacao> buscarTodas() {
        return movimentacoes;
    }

    public Movimentacao buscarPorId(Long id) {
        return movimentacoes.stream()
                .filter(movimentacao -> movimentacao.getId().equals(id))
                .toList().getFirst();
    }

    public void cadastrar(Movimentacao movimentacao) {
        movimentacoes.add(movimentacao);
    }
}
