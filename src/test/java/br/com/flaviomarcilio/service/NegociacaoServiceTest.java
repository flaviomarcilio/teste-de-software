package br.com.flaviomarcilio.service;

import br.com.flaviomarcilio.exceptions.ProdutoNaoCadastradoException;
import br.com.flaviomarcilio.model.Negociacao;
import br.com.flaviomarcilio.model.Produto;
import br.com.flaviomarcilio.model.enums.TipoMercado;
import br.com.flaviomarcilio.model.enums.TipoNegociacao;
import br.com.flaviomarcilio.model.enums.TipoProduto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Testes para NegociacaoService")
class NegociacaoServiceTest {

    @Mock
    private ProdutoService produtoService;

    private NegociacaoService negociacaoService;
    private Produto produto1;
    private Produto produto2;
    private Produto produto3;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        negociacaoService = new NegociacaoService(produtoService);

        produto1 = new Produto(
                "PETR4",
                "PETROBRAS S.A.",
                "33.000.167/0001-01",
                "PETR4BR",
                TipoProduto.PN,
                "Bradesco");
        produto2 = new Produto(
                "VALE3",
                "VALE S.A.",
                "33.592.510/0001-54",
                "VALE3BR",
                TipoProduto.ON,
                "Bradesco");
        produto3 = new Produto(
                "ITUB4",
                "ITAU S.A.",
                "60.701.190/0001-04",
                "ITAU4BR",
                TipoProduto.PN,
                "Bradesco");
    }

    @Nested
    @DisplayName("Funcionalidade: Consultar")
    class ConsultarTests {

        @Test
        @DisplayName("deve retornar lista vazia quando nenhuma negociação foi cadastrada")
        void deveRetornarListaVazia() {
            List<Negociacao> resultado = negociacaoService.buscarTodas();

            assertNotNull(resultado);
            assertTrue(resultado.isEmpty());
        }

        @Test
        @DisplayName("deve retornar todas as negociações cadastradas")
        void deveRetornarTodasNegociacoes() {

            when(produtoService.buscarPorTicker("PETR4")).thenReturn(produto1);
            when(produtoService.buscarPorTicker("VALE3")).thenReturn(produto2);

            Negociacao negociacao1 = new Negociacao(
                    1L,
                    LocalDate.now(),
                    TipoNegociacao.COMPRA,
                    TipoMercado.FRACIONARIO,
                    LocalDate.of(2025,12,12),
                    "Bradesco",
                    "PETR4",
                    10,
                    new BigDecimal("4.50"));

            Negociacao negociacao2 = new Negociacao(
                    2L,
                    LocalDate.now(),
                    TipoNegociacao.COMPRA,
                    TipoMercado.FRACIONARIO,
                    LocalDate.of(2025,12,12),
                    "Bradesco",
                    "VALE3",
                    10,
                    new BigDecimal("3.50"));

            negociacaoService.cadastrar(negociacao1);
            negociacaoService.cadastrar(negociacao2);

            List<Negociacao> negociacoes = negociacaoService.buscarTodas();
            assertEquals(2, negociacoes.size());
            verify(produtoService, times(1)).buscarPorTicker("PETR4");
            verify(produtoService, times(1)).buscarPorTicker("VALE3");
        }

        @Test
        @DisplayName("deve retornar negociação quando ID existe")
        void deveRetornarNegociacaoQuandoIdExiste() {

            when(produtoService.buscarPorTicker("PETR4")).thenReturn(produto1);

            Negociacao negociacao1 = new Negociacao(
                    1L,
                    LocalDate.now(),
                    TipoNegociacao.COMPRA,
                    TipoMercado.FRACIONARIO,
                    LocalDate.of(2025,12,12),
                    "Bradesco",
                    "PETR4",
                    10,
                    new BigDecimal("4.50"));

            negociacaoService.cadastrar(negociacao1);

            Negociacao resultado = negociacaoService.buscarPorId(1L);

            assertNotNull(resultado);
            assertEquals(1L, resultado.getId());
            assertEquals("PETR4", resultado.getCodigoNegociacao());
            assertEquals(10, resultado.getQuantidade());
        }

        @Test
        @DisplayName("deve lançar exceção quando ID não existe")
        void deveLancarExcecaoQuandoIdNaoExiste() {
            assertThrows(NoSuchElementException.class, () -> {
                negociacaoService.buscarPorId(999L);
            });
        }
    }

    @Nested
    @DisplayName("Funcionalidade: Cadastrar")
    class CadastrarTests {

        @Test
        @DisplayName("deve cadastrar negociação quando produto existe")
        void deveCadastrarNegociacaoQuandoProdutoExiste() {

            when(produtoService.buscarPorTicker("PETR4")).thenReturn(produto1);

            Negociacao negociacao1 = new Negociacao(
                    1L,
                    LocalDate.now(),
                    TipoNegociacao.COMPRA,
                    TipoMercado.FRACIONARIO,
                    LocalDate.of(2025,12,12),
                    "Bradesco",
                    "PETR4",
                    10,
                    new BigDecimal("4.50"));

            assertDoesNotThrow(() -> negociacaoService.cadastrar(negociacao1));

            List<Negociacao> negociacoes = negociacaoService.buscarTodas();
            assertEquals(1, negociacoes.size());
            assertEquals(negociacao1, negociacoes.get(0));
        }

        @Test
        @DisplayName("não deve cadastrar quando produto service retorna null")
        void naoDeveCadastrarQuandoProdutoServiceRetornaNull() {
            when(produtoService.buscarPorTicker(anyString())).thenReturn(null);

            Negociacao negociacao1 = new Negociacao(
                    1L,
                    LocalDate.now(),
                    TipoNegociacao.COMPRA,
                    TipoMercado.FRACIONARIO,
                    LocalDate.of(2025,12,12),
                    "Bradesco",
                    "ITUB4",
                    10,
                    new BigDecimal("4.50"));

            assertThrows(ProdutoNaoCadastradoException.class, () -> {
                negociacaoService.cadastrar(negociacao1);
            });

            assertEquals(0, negociacaoService.buscarTodas().size());
        }

        @Test
        @DisplayName("deve lançar exceção ProdutoNaoCadastradoException quando produto não cadastrado")
        void deveLancarExcecaoEspecificaQuandoProdutoNaoCadastrado() {
            when(produtoService.buscarPorTicker("ITUB4")).thenReturn(null);

            Negociacao negociacao1 = new Negociacao(
                    1L,
                    LocalDate.now(),
                    TipoNegociacao.COMPRA,
                    TipoMercado.FRACIONARIO,
                    LocalDate.of(2025,12,12),
                    "Bradesco",
                    "ITUB4",
                    10,
                    new BigDecimal("4.50"));

            Exception exception = assertThrows(ProdutoNaoCadastradoException.class, () -> {
                negociacaoService.cadastrar(negociacao1);
            });

            assertInstanceOf(ProdutoNaoCadastradoException.class, exception);
        }
    }
}