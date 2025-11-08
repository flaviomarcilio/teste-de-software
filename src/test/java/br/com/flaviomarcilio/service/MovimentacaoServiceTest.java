package br.com.flaviomarcilio.service;

import br.com.flaviomarcilio.model.Movimentacao;
import br.com.flaviomarcilio.model.enums.TipoMovimentacao;
import br.com.flaviomarcilio.model.enums.TipoTransacao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes para MovimentacaoService")
class MovimentacaoServiceTest {

    private MovimentacaoService movimentacaoService;
    private Movimentacao movimentacao1;
    private Movimentacao movimentacao2;
    private Movimentacao movimentacao3;

    @BeforeEach
    void setUp() {
        movimentacaoService = new MovimentacaoService();

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

        movimentacao3 = new Movimentacao(3L,
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
            movimentacaoService.cadastrar(movimentacao1);
            movimentacaoService.cadastrar(movimentacao2);
            movimentacaoService.cadastrar(movimentacao3);

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
            movimentacaoService.cadastrar(movimentacao1);
            movimentacaoService.cadastrar(movimentacao2);

            Movimentacao resultado = movimentacaoService.buscarPorId(1L);

            assertNotNull(resultado);
            assertEquals(1L, resultado.getId());
            assertEquals("PETR4", resultado.getCodigoNegociacao());
            assertEquals(new BigDecimal("4.50"), resultado.getPrecoUnitario());
        }

        @Test
        @DisplayName("Deve lançar exceção ao buscar movimentação por ID inexistente")
        void deveLancarExcecaoAoBuscarIdInexistente() {
            movimentacaoService.cadastrar(movimentacao1);

            assertThrows(NoSuchElementException.class, () -> {
                movimentacaoService.buscarPorId(999L);
            });
        }

        @Test
        @DisplayName("Deve lançar exceção ao buscar em lista vazia")
        void deveLancarExcecaoAoBuscarEmListaVazia() {
            assertThrows(NoSuchElementException.class, () -> {
                movimentacaoService.buscarPorId(1L);
            });
        }

        @Test
        @DisplayName("Deve buscar movimentação correta quando houver múltiplas")
        void deveBuscarMovimentacaoCorretaComMultiplas() {
            movimentacaoService.cadastrar(movimentacao1);
            movimentacaoService.cadastrar(movimentacao2);
            movimentacaoService.cadastrar(movimentacao3);

            Movimentacao resultado = movimentacaoService.buscarPorId(2L);

            assertNotNull(resultado);
            assertEquals(2L, resultado.getId());
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

            List<Movimentacao> movimentacoes = movimentacaoService.buscarTodas();

            assertEquals(1, movimentacoes.size());
        }
    }
}