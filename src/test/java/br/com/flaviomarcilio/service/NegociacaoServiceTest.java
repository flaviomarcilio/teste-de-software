package br.com.flaviomarcilio.service;

import br.com.flaviomarcilio.exceptions.ProdutoNaoCadastradoException;
import br.com.flaviomarcilio.model.Negociacao;
import br.com.flaviomarcilio.model.Produto;
import br.com.flaviomarcilio.model.enums.TipoMercado;
import br.com.flaviomarcilio.model.enums.TipoNegociacao;
import br.com.flaviomarcilio.model.enums.TipoProduto;
import br.com.flaviomarcilio.repository.NegociacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Testes para NegociacaoService")
class NegociacaoServiceTest {

    private NegociacaoRepository negociacaoRepository;
    private ProdutoService produtoService;
    private NegociacaoService negociacaoService;
    private Produto produto1;
    private Produto produto2;

    @BeforeEach
    void setUp() {
        negociacaoRepository = mock(NegociacaoRepository.class);
        produtoService = mock(ProdutoService.class);
        negociacaoService = new NegociacaoService(negociacaoRepository, produtoService);

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
    }

    @Nested
    @DisplayName("Funcionalidade: Consultar")
    class ConsultarTests {

        @Test
        @DisplayName("deve retornar lista vazia quando nenhuma negociação foi cadastrada")
        void deveRetornarListaVazia() {
            when(negociacaoRepository.listAll()).thenReturn(Collections.emptyList());

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

            when(negociacaoRepository.listAll()).thenReturn(List.of(negociacao1, negociacao2));

            List<Negociacao> negociacoes = negociacaoService.buscarTodas();
            assertEquals(2, negociacoes.size());
            assertTrue(negociacoes.contains(negociacao1));
            assertTrue(negociacoes.contains(negociacao2));
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

            when(negociacaoRepository.findById(1L)).thenReturn(negociacao1);

            Negociacao resultado = negociacaoService.buscarPorId(1L);

            assertNotNull(resultado);
            assertEquals(1L, resultado.getId());
            assertEquals("PETR4", resultado.getCodigoNegociacao());
            assertEquals(10, resultado.getQuantidade());
        }

        @Test
        @DisplayName("deve retornar null quando ID não existe")
        void deveRetornarNullQuandoIdNaoExiste() {
            when(negociacaoRepository.findById(999L)).thenReturn(null);
            Negociacao resultado = negociacaoService.buscarPorId(999L);
            assertNull(resultado);
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

            verify(negociacaoRepository, times(1)).persist(negociacao1);
        }

        @Test
        @DisplayName("não deve cadastrar quando produto service retorna null")
        void naoDeveCadastrarQuandoProdutoServiceRetornaNull() {
            when(produtoService.buscarPorTicker(anyString())).thenThrow(new ProdutoNaoCadastradoException());

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

            assertThrows(ProdutoNaoCadastradoException.class, () -> negociacaoService.cadastrar(negociacao1));
            verify(negociacaoRepository, never()).persist(any(br.com.flaviomarcilio.model.Negociacao.class));
        }

        @Test
        @DisplayName("deve lançar exceção ProdutoNaoCadastradoException quando produto não cadastrado")
        void deveLancarExcecaoEspecificaQuandoProdutoNaoCadastrado() {
            when(produtoService.buscarPorTicker("ITUB4")).thenThrow(new ProdutoNaoCadastradoException());

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

            Exception exception = assertThrows(ProdutoNaoCadastradoException.class, 
                    () -> negociacaoService.cadastrar(negociacao1));
            assertInstanceOf(ProdutoNaoCadastradoException.class, exception);
        }
    }
}