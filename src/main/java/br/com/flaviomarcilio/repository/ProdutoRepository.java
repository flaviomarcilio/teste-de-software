package br.com.flaviomarcilio.repository;

import br.com.flaviomarcilio.model.Produto;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProdutoRepository implements PanacheRepository<Produto> {

    public Produto findByTicker(String ticker) {
        return find("codigoNegociacao", ticker).firstResult();
    }
}
