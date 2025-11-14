package br.com.flaviomarcilio.service;

import br.com.flaviomarcilio.model.Movimentacao;
import br.com.flaviomarcilio.model.enums.TipoMovimentacao;
import br.com.flaviomarcilio.model.enums.TipoTransacao;
import br.com.flaviomarcilio.repository.MovimentacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Testes para MovimentacaoService")
class MovimentacaoServiceTest {

    private MovimentacaoRepository movimentacaoRepository;
    private MovimentacaoService movimentacaoService;
    private Movimentacao movimentacao1;
    private Movimentacao movimentacao2;
    private Movimentacao movimentacao3;

    @BeforeEach
    void setUp() {

        movimentacaoRepository = mock(MovimentacaoRepository.class);
        movimentacaoService = new MovimentacaoService(movimentacaoRepository);

        movimentacao1 = new Movimentacao(
                TipoTransacao.CREDITO,
                LocalDate.now(),
                TipoMovimentacao.DIVIDENDO,
                "PETR4",
                "BB Banco de Investimentos",
                10,
                new BigDecimal("4.50"));

        movimentacao2 = new Movimentacao(
                TipoTransacao.CREDITO,
                LocalDate.now(),
                TipoMovimentacao.JUROS_SOBRE_CAPITAL_PROPRIO,
                "VALE3",
                "BB Banco de Investimentos",
                10,
                new BigDecimal("3.50"));

        movimentacao3 = new Movimentacao(
                TipoTransacao.CREDITO,
                LocalDate.now(),
                TipoMovimentacao.DIVIDENDO,
                "ITUB4",
                "BB Banco de Investimentos",
                10,
                new BigDecimal("1.50"));
    }

    @Nested
    @DisplayName("Funcionalidade: Consultar")
    class ConsultarTests {

        @Test
        @DisplayName("Deve retornar todas as movimentações cadastradas")
        void deveBuscarTodasMovimentacoes() {
            when(movimentacaoRepository.listAll()).thenReturn(List.of(movimentacao1, movimentacao2, movimentacao3));

            List<Movimentacao> resultado = movimentacaoService.buscarTodas();

            assertNotNull(resultado);
            assertEquals(3, resultado.size());
            assertTrue(resultado.contains(movimentacao1));
            assertTrue(resultado.contains(movimentacao2));
            assertTrue(resultado.contains(movimentacao3));
        }

        @Test
        @DisplayName("Deve buscar movimentação por ID existente")
        void deveBuscarMovimentacaoPorIdExistente() {

            when(movimentacaoRepository.findById(1L)).thenReturn(movimentacao1);

            Movimentacao resultado = movimentacaoService.buscarPorId(1L);

            assertNotNull(resultado);
            assertEquals("PETR4", resultado.getCodigoNegociacao());
            assertEquals(new BigDecimal("4.50"), resultado.getPrecoUnitario());
        }

        @Test
        @DisplayName("Deve retornar null ao buscar movimentação por ID inexistente")
        void deveRetornarNullAoBuscarIdInexistente() {
            when(movimentacaoRepository.findById(999L)).thenReturn(null);
            Movimentacao resultado = movimentacaoService.buscarPorId(999L);
            assertNull(resultado);
        }

        @Test
        @DisplayName("Deve retornar null quando repository não encontra")
        void deveRetornarNullQuandoRepositoryNaoEncontra() {
            when(movimentacaoRepository.findById(1L)).thenReturn(null);
            Movimentacao resultado = movimentacaoService.buscarPorId(1L);
            assertNull(resultado);
        }

        @Test
        @DisplayName("Deve buscar movimentação correta quando houver múltiplas")
        void deveBuscarMovimentacaoCorretaComMultiplas() {
            when(movimentacaoRepository.findById(2L)).thenReturn(movimentacao2);

            Movimentacao resultado = movimentacaoService.buscarPorId(2L);

            assertNotNull(resultado);
            assertEquals("VALE3", resultado.getCodigoNegociacao());
            assertEquals(movimentacao2, resultado);
        }
    }

    @Nested
    @DisplayName("Funcionalidade: Cadastrar")
    class CadastrarTests {

        @Test
        @DisplayName("Deve cadastrar uma movimentação com sucesso")
        void deveCadastrarMovimentacaoComSucesso() {
            movimentacaoService.cadastrar(movimentacao1);

            verify(movimentacaoRepository, times(1)).persist(movimentacao1);
        }
    }
}