package br.com.flaviomarcilio.controller;

import br.com.flaviomarcilio.model.Produto;
import br.com.flaviomarcilio.service.ProdutoService;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;

@Path("/api/v1/produto")
public class ProdutoController {

    private final ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    @GET
    public RestResponse<List<Produto>> buscarTodos() {
        return RestResponse.ok(service.buscarTodos());
    }

    @GET
    @Path("/{ticker}")
    public RestResponse<Produto> buscarPorTicker(String ticker) {
        return RestResponse.ok(service.buscarPorTicker(ticker));
    }

    @POST
    @Transactional
    public RestResponse<Void> cadastrar(Produto produto, @Context UriInfo uriInfo) {
        service.cadastrar(produto);
        return RestResponse.created(uriInfo.getAbsolutePathBuilder().build());
    }
}
