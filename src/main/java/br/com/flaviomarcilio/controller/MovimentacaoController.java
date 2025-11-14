package br.com.flaviomarcilio.controller;

import br.com.flaviomarcilio.model.Movimentacao;
import br.com.flaviomarcilio.service.MovimentacaoService;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;

@Path("/api/v1/movimentacao")
public class MovimentacaoController {

    private final MovimentacaoService service;

    public MovimentacaoController(MovimentacaoService service) {
        this.service = service;
    }

    @GET
    public RestResponse<List<Movimentacao>> buscarTodas() {
        return RestResponse.ok(service.buscarTodas());
    }

    @GET
    @Path("/{id}")
    public RestResponse<Movimentacao> buscarPorId(Long id) {
        Movimentacao movimentacao = service.buscarPorId(id);

        if (movimentacao == null) {
            return RestResponse.notFound();
        }
        return RestResponse.ok(movimentacao);
    }

    @POST
    @Transactional
    public RestResponse<Void> cadastrar(Movimentacao movimentacao, @Context UriInfo uriInfo) {

        if (movimentacao == null) {
            return RestResponse.status(RestResponse.Status.BAD_REQUEST);
        }

        service.cadastrar(movimentacao);
        return RestResponse.created(uriInfo.getAbsolutePathBuilder().build());
    }
}