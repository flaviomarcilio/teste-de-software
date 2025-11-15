package br.com.flaviomarcilio.controller;

import br.com.flaviomarcilio.exceptions.ProdutoNaoCadastradoException;
import br.com.flaviomarcilio.model.Negociacao;
import br.com.flaviomarcilio.service.NegociacaoService;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;

@Path("/api/v1/negociacao")
public class NegociacaoController {

    private final NegociacaoService negociacaoService;

    public NegociacaoController(NegociacaoService negociacaoService) {
        this.negociacaoService = negociacaoService;
    }

    @GET
    public RestResponse<List<Negociacao>> buscarTodas() {
        return RestResponse.ok(negociacaoService.buscarTodas());
    }

    @GET
    @Path("/{id}")
    public RestResponse<Negociacao> buscarPorId(Long id) {

        Negociacao negociacao = negociacaoService.buscarPorId(id);

        if (negociacao == null) {
            return RestResponse.notFound();
        }
        return RestResponse.ok(negociacao);
    }

    @POST
    @Transactional
    public RestResponse<Void> cadastrar(Negociacao negociacao, @Context UriInfo uriInfo) {

        if (negociacao == null) {
            return RestResponse.status(RestResponse.Status.BAD_REQUEST);
        }

        try {
            negociacaoService.cadastrar(negociacao);
            return RestResponse.created(uriInfo.getAbsolutePathBuilder().build());
        } catch (ProdutoNaoCadastradoException e) {
            return RestResponse.status(RestResponse.Status.BAD_REQUEST);
        }
    }
}
