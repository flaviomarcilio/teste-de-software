package br.com.flaviomarcilio.controller;

import br.com.flaviomarcilio.exceptions.ProdutoNaoCadastradoException;
import br.com.flaviomarcilio.model.Negociacao;
import br.com.flaviomarcilio.model.enums.TipoMercado;
import br.com.flaviomarcilio.model.enums.TipoNegociacao;
import br.com.flaviomarcilio.service.NegociacaoService;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import org.jboss.resteasy.reactive.RestResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Testes para NegociacaoController")
class NegociacaoControllerTest {

    private NegociacaoController negociacaoController;
    private NegociacaoService negociacaoService;
    private UriInfo uriInfo;

    @BeforeEach
    void setUp() {
        negociacaoService = mock(NegociacaoService.class);
        uriInfo = mock(UriInfo.class);
        negociacaoController = new NegociacaoController(negociacaoService);
    }

    @Nested
    @DisplayName("Funcionalidade: Consultar")
    class ConsultarTests {

        @Test
        @DisplayName("Deve retornar todas as negociações cadastradas")
        void deveRetornarTodasNegociacoesCadastradas() {

            Negociacao compraPetr4 = new Negociacao(
                    LocalDate.now(),
                    TipoNegociacao.COMPRA,
                    TipoMercado.FRACIONARIO,
                    LocalDate.of(2025,12,12),
                    "Bradesco",
                    "PETR4",
                    10,
                    new BigDecimal("4.50"));

            Negociacao compraVale3 = new Negociacao(
                    LocalDate.now(),
                    TipoNegociacao.COMPRA,
                    TipoMercado.FRACIONARIO,
                    LocalDate.of(2025,12,12),
                    "Bradesco",
                    "VALE3",
                    10,
                    new BigDecimal("3.50"));

            List<Negociacao> negociacoes = Arrays.asList(compraPetr4, compraVale3);
            when(negociacaoService.buscarTodas()).thenReturn(negociacoes);

            RestResponse<List<Negociacao>> response = negociacaoController.buscarTodas();

            assertNotNull(response);
            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            assertEquals(negociacoes, response.getEntity());
        }

        @Test
        @DisplayName("Deve retornar a negociação pelo ID")
        void deveRetornarNegociacaoPeloId() {

            Negociacao negociacao = new Negociacao(
                    LocalDate.now(),
                    TipoNegociacao.COMPRA,
                    TipoMercado.FRACIONARIO,
                    LocalDate.of(2025,12,12),
                    "Bradesco",
                    "PETR4",
                    10,
                    new BigDecimal("4.50"));

            when(negociacaoService.buscarPorId(1L)).thenReturn(negociacao);

            RestResponse<Negociacao> response = negociacaoController.buscarPorId(1L);

            assertNotNull(response);
            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            assertEquals(negociacao.getCodigoNegociacao(), response.getEntity().getCodigoNegociacao());
            assertEquals(negociacao.getTipoNegociacao(), response.getEntity().getTipoNegociacao());
            assertEquals(negociacao.getInstituicao(), response.getEntity().getInstituicao());
            assertEquals(negociacao.getPrecoUnitario(), response.getEntity().getPrecoUnitario());
        }

        @Test
        @DisplayName("Deve retornar erro 404 para negociação não cadastrada")
        void deveRetornarErro404ParaNegociacaoNaoCadastrada() {
            when(negociacaoService.buscarPorId(999L))
                    .thenReturn(null);

            RestResponse<Negociacao> response = negociacaoController.buscarPorId(999L);

            assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        }
    }

    @Nested
    @DisplayName("Funcionalidade: Cadastrar")
    class CadastrarTests {

        @Test
        @DisplayName("Deve cadastrar uma negociação")
        void deveCadastrarUmaNegociacao() {

            Negociacao negociacao = new Negociacao(
                    LocalDate.now(),
                    TipoNegociacao.COMPRA,
                    TipoMercado.FRACIONARIO,
                    LocalDate.of(2025,12,12),
                    "Bradesco",
                    "PETR4",
                    10,
                    new BigDecimal("4.50"));

            UriBuilder uriBuilder = mock(UriBuilder.class);
            URI uri = URI.create("http://localhost/api/v1/negociacao");

            when(uriInfo.getAbsolutePathBuilder()).thenReturn(uriBuilder);
            when(uriBuilder.build()).thenReturn(uri);
            doNothing().when(negociacaoService).cadastrar(negociacao);

            RestResponse<Void> response = negociacaoController.cadastrar(negociacao, uriInfo);

            assertNotNull(response);
            assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
            assertNotNull(response.getLocation());
            verify(negociacaoService, times(1)).cadastrar(negociacao);
        }

        @Test
        @DisplayName("Deve cadastrar uma negociação e retornar Location header")
        void deveCadastrarUmaNegociacaoERetornarLocationHeader() {

            Negociacao negociacao = new Negociacao(
                    LocalDate.now(),
                    TipoNegociacao.COMPRA,
                    TipoMercado.FRACIONARIO,
                    LocalDate.of(2025,12,12),
                    "Bradesco",
                    "PETR4",
                    10,
                    new BigDecimal("4.50"));

            UriBuilder uriBuilder = mock(UriBuilder.class);
            URI uri = URI.create("http://localhost/api/v1/negociacao/1");

            when(uriInfo.getAbsolutePathBuilder()).thenReturn(uriBuilder);
            when(uriBuilder.build()).thenReturn(uri);
            doNothing().when(negociacaoService).cadastrar(negociacao);

            RestResponse<Void> response = negociacaoController.cadastrar(negociacao, uriInfo);

            assertTrue(response.getLocation().toString().contains("/api/v1/negociacao"));
        }

        @Test
        @DisplayName("Deve retorna erro 400 ao cadastrar negociação de produto não cadastrado")
        void deveRetornarErro400AoCadastrarNegociacaoDeProdutoNaoCadastrado() {

            Negociacao negociacao = new Negociacao(
                    LocalDate.now(),
                    TipoNegociacao.COMPRA,
                    TipoMercado.FRACIONARIO,
                    LocalDate.of(2025,12,12),
                    "Bradesco",
                    "PETR4",
                    10,
                    new BigDecimal("4.50"));

            UriBuilder uriBuilder = mock(UriBuilder.class);
            URI uri = URI.create("http://localhost/api/v1/negociacao");

            when(uriInfo.getAbsolutePathBuilder()).thenReturn(uriBuilder);
            when(uriBuilder.build()).thenReturn(uri);
            doThrow(new ProdutoNaoCadastradoException())
                    .when(negociacaoService).cadastrar(negociacao);

            RestResponse<Void> response = negociacaoController.cadastrar(negociacao, uriInfo);

            assertNotNull(response);
            assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

            verify(negociacaoService, times(1)).cadastrar(negociacao);
        }
    }
}