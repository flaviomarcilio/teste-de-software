package br.com.flaviomarcilio.repository;

import br.com.flaviomarcilio.model.Movimentacao;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MovimentacaoRepository implements PanacheRepository<Movimentacao> {
}