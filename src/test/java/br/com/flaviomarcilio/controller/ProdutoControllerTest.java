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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Testes Unitários para ProdutoController")
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

    @Nested
    @DisplayName("Funcionalidade: Consultar")
    class ConsultarTests {

        @Test
        @DisplayName("Deve retornar lista de todos produtos cadastrados")
        void deveRetornarListaDeTodosProdutosCadastrados() {
            Produto petr4 = new Produto(
                    "PETR4",
                    "PETROBRAS S.A.",
                    "33.000.167/0001-01",
                    "PETR4BR",
                    TipoProduto.PN,
                    "Bradesco");
            Produto vale3 = new Produto(
                    "VALE3",
                    "VALE S.A.",
                    "33.592.510/0001-54",
                    "VALE3BR",
                    TipoProduto.ON,
                    "Bradesco");
            List<Produto> produtos = Arrays.asList(petr4, vale3);
            when(produtoService.buscarTodos()).thenReturn(produtos);

            RestResponse<List<Produto>> response = controller.buscarTodos();

            assertNotNull(response);
            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            assertEquals(produtos, response.getEntity());
            verify(produtoService, times(1)).buscarTodos();
        }

        @Test
        @DisplayName("Deve retornar produto com ticker correto")
        void deveRetornarProdutoComTickerCorreto() {

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
            assertEquals(produto.getTipoProduto(), response.getEntity().getTipoProduto());
            assertEquals(produto.getNome(), response.getEntity().getNome());
            assertEquals(produto.getCodigoNegociacao(), response.getEntity().getCodigoNegociacao());
            assertEquals(produto.getCnpj(), response.getEntity().getCnpj());
            assertEquals(produto.getAdministrador(), response.getEntity().getAdministrador());
            verify(produtoService, times(1)).buscarPorTicker("PETR4");
        }
    }

    @Nested
    @DisplayName("Funcionalidade: Cadastrar")
    class CadastrarTests {

        @Test
        @DisplayName("Deve cadastrar produto")
        void cadastrarDeveChamarServiceERetornarCreated() {

            Produto produto = new Produto(
                    "PETR4",
                    "PETROBRAS S.A.",
                    "33.000.167/0001-01",
                    "PETR4BR",
                    TipoProduto.PN,
                    "Bradesco");

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
}