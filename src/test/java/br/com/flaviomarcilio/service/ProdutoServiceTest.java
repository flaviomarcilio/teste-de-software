package br.com.flaviomarcilio.service;

import br.com.flaviomarcilio.model.Produto;
import br.com.flaviomarcilio.model.enums.TipoProduto;
import br.com.flaviomarcilio.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import br.com.flaviomarcilio.exceptions.ProdutoNaoCadastradoException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Testes Unitários para ProdutoService")
class ProdutoServiceTest {

    private ProdutoRepository produtoRepository;
    private ProdutoService produtoService;
    private Produto produto1;
    private Produto produto2;
    private Produto produto3;

    @BeforeEach
    void setUp() {
        //Arrange
        produtoRepository = mock(ProdutoRepository.class);
        produtoService = new ProdutoService(produtoRepository);
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
        @DisplayName("Deve retornar lista vazia quando não houver produtos cadastrados")
        void deveRetornarListaVaziaQuandoNaoHouverProdutosCadastrados() {
            List<Produto> resultado = produtoService.buscarTodos();

            assertNotNull(resultado);
            assertTrue(resultado.isEmpty());
        }

        @Test
        @DisplayName("Deve retornar todos os produtos cadastrados")
        void deveRetornarTodosProdutosCadastrados() {
            List<Produto> produtosCadastrados = Arrays.asList(produto1, produto2, produto3);
            when(produtoRepository.listAll()).thenReturn(produtosCadastrados);

            List<Produto> resultado = produtoService.buscarTodos();

            assertNotNull(resultado);
            assertEquals(3, resultado.size());
        }

        @Test
        @DisplayName("Deve retornar produto por ticker")
        void deveRetornarProdutoPorTicker() {
            when(produtoRepository.findByTicker("VALE3")).thenReturn(produto2);

            Produto resultado = produtoService.buscarPorTicker("VALE3");

            assertNotNull(resultado);
            assertEquals("VALE3", resultado.getCodigoNegociacao());
            assertEquals(produto2, resultado);
        }

        @Test
        @DisplayName("Deve lançar exceção ao buscar em lista vazia")
        void deveLancarExcecaoAoBuscarEmListaVazia() {
            when(produtoRepository.findByTicker("PETR4")).thenReturn(null);

            assertThrows(ProdutoNaoCadastradoException.class, () -> produtoService.buscarPorTicker("PETR4"));
        }
    }

    @Nested
    @DisplayName("Funcionalidade: Cadastrar")
    class CadastrarTests {

        @Test
        @DisplayName("Deve cadastrar múltiplos produtos")
        void deveCadastrarMultiplosProdutos() {
            List<Produto> produtosCadastrados = Arrays.asList(produto1, produto2, produto3);
            when(produtoRepository.listAll()).thenReturn(produtosCadastrados);

            produtoService.cadastrar(produto1);
            produtoService.cadastrar(produto2);
            produtoService.cadastrar(produto3);

            List<Produto> produtos = produtoService.buscarTodos();

            assertEquals(3, produtos.size());
        }

        @Test
        @DisplayName("Deve persistir produto ao cadastrar")
        void devePersistirProdutoAoCadastrar() {
            produtoService.cadastrar(produto1);

            verify(produtoRepository, times(1)).persist(produto1);
        }
    }
}