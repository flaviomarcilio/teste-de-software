package br.com.flaviomarcilio.controller;

import br.com.flaviomarcilio.model.Negociacao;
import br.com.flaviomarcilio.model.enums.TipoMercado;
import br.com.flaviomarcilio.model.enums.TipoNegociacao;
import br.com.flaviomarcilio.service.NegociacaoService;
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

    private NegociacaoController controller;
    private NegociacaoService negociacaoService;
    private UriInfo uriInfo;

    @BeforeEach
    void setUp() {
        negociacaoService = mock(NegociacaoService.class);
        uriInfo = mock(UriInfo.class);
        controller = new NegociacaoController(negociacaoService);
    }

    @Nested
    @DisplayName("Funcionalidade: Consultar")
    class ConsultarTests {

        @Test
        @DisplayName("buscarTodas deve delegar para o serviço")
        void buscarTodasDeveDelegarParaServico() {
            List<Negociacao> negociacoes = Arrays.asList(new Negociacao(), new Negociacao());
            when(negociacaoService.buscarTodas()).thenReturn(negociacoes);

            RestResponse<List<Negociacao>> response = controller.buscarTodas();

            assertNotNull(response);
            assertEquals(200, response.getStatus());
            assertEquals(negociacoes, response.getEntity());
        }

        @Test
        @DisplayName("buscarPorId deve delegar para o serviço")
        void buscarPorIdDeveDelegarParaServico() {

            Negociacao negociacao = new Negociacao(
                    1L,
                    LocalDate.now(),
                    TipoNegociacao.COMPRA,
                    TipoMercado.FRACIONARIO,
                    LocalDate.of(2025,12,12),
                    "Bradesco",
                    "PETR4",
                    10,
                    new BigDecimal("4.50"));

            when(negociacaoService.buscarPorId(1L)).thenReturn(negociacao);

            RestResponse<Negociacao> response = controller.buscarPorId(1L);

            assertNotNull(response);
            assertEquals(200, response.getStatus());
            assertEquals(negociacao, response.getEntity());
        }
    }

    @Nested
    @DisplayName("Funcionalidade: Cadastrar")
    class CadastrarTests {

        @Test
        @DisplayName("cadastrar deve delegar para o serviço")
        void cadastrarDeveDelegarParaServico() {

            Negociacao negociacao = new Negociacao(
                    1L,
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

            RestResponse<Void> response = controller.cadastrar(negociacao, uriInfo);

            assertNotNull(response);
            assertEquals(201, response.getStatus());
            assertNotNull(response.getLocation());
            verify(negociacaoService, times(1)).cadastrar(negociacao);
        }

        @Test
        @DisplayName("cadastrar deve retornar Location header")
        void cadastrarDeveRetornarLocationHeader() {

            Negociacao negociacao = new Negociacao(
                    1L,
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

            RestResponse<Void> response = controller.cadastrar(negociacao, uriInfo);

            assertTrue(response.getLocation().toString().contains("/api/v1/negociacao"));
        }
    }
}