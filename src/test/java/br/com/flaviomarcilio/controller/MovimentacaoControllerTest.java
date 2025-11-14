package br.com.flaviomarcilio.controller;

import br.com.flaviomarcilio.model.Movimentacao;
import br.com.flaviomarcilio.model.enums.TipoMovimentacao;
import br.com.flaviomarcilio.model.enums.TipoTransacao;
import br.com.flaviomarcilio.service.MovimentacaoService;
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

@DisplayName("Testes Unitários do MovimentacaoController")
class MovimentacaoControllerTest {

    private MovimentacaoController controller;
    private MovimentacaoService movimentacaoService;
    private UriInfo uriInfo;
    private Movimentacao movimentacao1;
    private Movimentacao movimentacao2;

    @BeforeEach
    void setUp() {
        movimentacaoService = mock(MovimentacaoService.class);
        uriInfo = mock(UriInfo.class);
        controller = new MovimentacaoController(movimentacaoService);

        movimentacao1 = new Movimentacao(1L,
                TipoTransacao.CREDITO,
                LocalDate.now(),
                TipoMovimentacao.DIVIDENDO,
                "PETR4",
                "BB Banco de Investimentos",
                10,
                new BigDecimal("4.50"));

        movimentacao2 = new Movimentacao(2L,
                TipoTransacao.CREDITO,
                LocalDate.now(),
                TipoMovimentacao.JUROS_SOBRE_CAPITAL_PROPRIO,
                "VALE3",
                "BB Banco de Investimentos",
                10,
                new BigDecimal("3.50"));
    }

    @Nested
    @DisplayName("Funcionalidade: Consultar")
    class ConsultarTests {

        @Test
        @DisplayName("BuscarTodas deve chamar service e retornar RestResponse")
        void buscarTodasDeveChamarServiceERetornarRestResponse() {
            List<Movimentacao> movimentacoes = Arrays.asList(movimentacao1, movimentacao2);
            when(movimentacaoService.buscarTodas()).thenReturn(movimentacoes);

            RestResponse<List<Movimentacao>> response = controller.buscarTodas();

            assertNotNull(response);
            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            assertEquals(movimentacoes, response.getEntity());
            verify(movimentacaoService, times(1)).buscarTodas();
        }

        @Test
        @DisplayName("BuscarPorId deve chamar service com ID correto")
        void buscarPorIdDeveChamarServiceComIdCorreto() {
            when(movimentacaoService.buscarPorId(1L)).thenReturn(movimentacao1);

            RestResponse<Movimentacao> response = controller.buscarPorId(1L);

            assertNotNull(response);
            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            assertEquals(movimentacao1, response.getEntity());
            assertEquals(1L, response.getEntity().getId());
            verify(movimentacaoService, times(1)).buscarPorId(1L);
        }
    }

    @Nested
    @DisplayName("Funcionalidade: Cadastrar")
    class CadastrarTests {

        @Test
        @DisplayName("Cadastrar deve chamar service e retornar Created")
        void cadastrarDeveChamarServiceERetornarCreated() {

            UriBuilder uriBuilder = mock(UriBuilder.class);
            URI uri = URI.create("http://localhost:8080/api/v1/movimentacao");

            when(uriInfo.getAbsolutePathBuilder()).thenReturn(uriBuilder);
            when(uriBuilder.build()).thenReturn(uri);
            doNothing().when(movimentacaoService).cadastrar(movimentacao1);

            RestResponse<Void> response = controller.cadastrar(movimentacao1, uriInfo);

            assertNotNull(response);
            assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
            verify(movimentacaoService, times(1)).cadastrar(movimentacao1);
            verify(uriInfo, times(1)).getAbsolutePathBuilder();
        }

        @Test
        @DisplayName("Cadastrar deve retornar Location header")
        void cadastrarDeveRetornarLocationHeader() {
            Movimentacao movimentacao = new Movimentacao();
            UriBuilder uriBuilder = mock(UriBuilder.class);
            URI uri = URI.create("http://localhost:8080/api/v1/movimentacao/1");

            when(uriInfo.getAbsolutePathBuilder()).thenReturn(uriBuilder);
            when(uriBuilder.build()).thenReturn(uri);
            doNothing().when(movimentacaoService).cadastrar(movimentacao);

            RestResponse<Void> response = controller.cadastrar(movimentacao, uriInfo);

            assertNotNull(response);
            assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
            assertNotNull(response.getLocation());
            assertTrue(response.getLocation().toString().contains("/api/v1/movimentacao"));
        }
    }
}