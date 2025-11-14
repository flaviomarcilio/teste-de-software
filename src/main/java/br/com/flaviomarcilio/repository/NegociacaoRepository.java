package br.com.flaviomarcilio.repository;

import br.com.flaviomarcilio.model.Negociacao;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class NegociacaoRepository implements PanacheRepository<Negociacao> {
}
