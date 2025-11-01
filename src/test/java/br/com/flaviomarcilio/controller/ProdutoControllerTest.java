package br.com.flaviomarcilio.controller;

import br.com.flaviomarcilio.model.Produto;
import br.com.flaviomarcilio.model.enums.TipoProduto;
import br.com.flaviomarcilio.service.ProdutoService;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import org.jboss.resteasy.reactive.RestResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProdutoControllerTest {

    private ProdutoController controller;
    private ProdutoService produtoService;
    private UriInfo uriInfo;

    @BeforeEach
    void setUp() {
        produtoService = mock(ProdutoService.class);
        uriInfo = mock(UriInfo.class);
        controller = new ProdutoController(produtoService);
    }

    @Test
    @DisplayName("BuscarTodos deve chamar service e retornar RestResponse")
    void buscarTodosDeveChamarServiceERetornarRestResponse() {
        List<Produto> produtos = Arrays.asList(new Produto(), new Produto());
        when(produtoService.buscarTodos()).thenReturn(produtos);

        RestResponse<List<Produto>> response = controller.buscarTodos();

        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(produtos, response.getEntity());
        verify(produtoService, times(1)).buscarTodos();
    }

    @Test
    @DisplayName("BuscarPorTicker deve chamar service com ticker correto")
    void buscarPorTickerDeveChamarServiceComTickerCorreto() {
        Produto produto = new Produto(
                "PETR4",
                "PETROBRAS S.A.",
                "33.000.167/0001-01",
                "PETR4BR",
                TipoProduto.PN,
                "Bradesco");
        when(produtoService.buscarPorTicker("PETR4")).thenReturn(produto);

        RestResponse<Produto> response = controller.buscarPorTicker("PETR4");

        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(produto, response.getEntity());
        verify(produtoService, times(1)).buscarPorTicker("PETR4");
    }

    @Test
    @DisplayName("Cadastrar deve chamar service e retornar Created")
    void cadastrarDeveChamarServiceERetornarCreated() {
        Produto produto = new Produto();
        UriBuilder uriBuilder = mock(UriBuilder.class);
        URI uri = URI.create("http://localhost/api/v1/produto");

        when(uriInfo.getAbsolutePathBuilder()).thenReturn(uriBuilder);
        when(uriBuilder.build()).thenReturn(uri);
        doNothing().when(produtoService).cadastrar(produto);

        RestResponse<Void> response = controller.cadastrar(produto, uriInfo);

        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        verify(produtoService, times(1)).cadastrar(produto);
    }
}