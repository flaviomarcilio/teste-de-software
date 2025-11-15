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

@DisplayName("Testes Unitários para NegociacaoService")
class NegociacaoServiceTest {

    private NegociacaoRepository negociacaoRepository;
    private ProdutoService produtoService;
    private NegociacaoService negociacaoService;
    private Produto produto1;

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
    }

    @Nested
    @DisplayName("Funcionalidade: Consultar")
    class ConsultarTests {

        @Test
        @DisplayName("Deve retornar lista vazia quando nenhuma negociação foi cadastrada")
        void deveRetornarListaVaziaQuandoNenhumaNegociacaoFoiCadastrada() {
            when(negociacaoRepository.listAll()).thenReturn(Collections.emptyList());

            List<Negociacao> resultado = negociacaoService.buscarTodas();

            assertNotNull(resultado);
            assertTrue(resultado.isEmpty());
        }

        @Test
        @DisplayName("Deve retornar todas as negociações cadastradas")
        void deveRetornarTodasNegociacoesCadastradas() {

            Negociacao negociacao1 = new Negociacao(
                    LocalDate.now(),
                    TipoNegociacao.COMPRA,
                    TipoMercado.FRACIONARIO,
                    LocalDate.of(2025,12,12),
                    "Bradesco",
                    "PETR4",
                    10,
                    new BigDecimal("4.50"));

            Negociacao negociacao2 = new Negociacao(
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
        @DisplayName("Deve retornar negociação quando ID existe")
        void deveRetornarNegociacaoQuandoIdExiste() {

            Negociacao negociacao1 = new Negociacao(
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
            assertEquals("PETR4", resultado.getCodigoNegociacao());
            assertEquals(10, resultado.getQuantidade());
        }

        @Test
        @DisplayName("Deve retornar null quando ID não existe")
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
        @DisplayName("Deve cadastrar negociação quando produto existe")
        void deveCadastrarNegociacaoQuandoProdutoExiste() {

            when(produtoService.buscarPorTicker("PETR4")).thenReturn(produto1);

            Negociacao negociacao1 = new Negociacao(
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
        @DisplayName("Não deve cadastrar quando produto service retorna null")
        void naoDeveCadastrarQuandoProdutoServiceRetornaNull() {
            when(produtoService.buscarPorTicker(anyString())).thenThrow(new ProdutoNaoCadastradoException());

            Negociacao negociacao1 = new Negociacao(
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
        @DisplayName("Deve lançar exceção ProdutoNaoCadastradoException quando produto não cadastrado")
        void deveLancarExcecaoEspecificaQuandoProdutoNaoCadastrado() {
            when(produtoService.buscarPorTicker("ITUB4")).thenThrow(new ProdutoNaoCadastradoException());

            Negociacao negociacao1 = new Negociacao(
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