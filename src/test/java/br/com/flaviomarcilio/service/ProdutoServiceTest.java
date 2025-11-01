package br.com.flaviomarcilio.service;

import br.com.flaviomarcilio.model.Produto;
import br.com.flaviomarcilio.model.enums.TipoProduto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class ProdutoServiceTest {

    private ProdutoService produtoService;
    private Produto produto1;
    private Produto produto2;
    private Produto produto3;

    @BeforeEach
    void setUp() {
        //Arrange
        produtoService = new ProdutoService();
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

    @Test
    @DisplayName("Deve retornar lista vazia quando não houver produtos cadastrados")
    void deveBuscarTodosComListaVazia() {
        List<Produto> resultado = produtoService.buscarTodos();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Deve cadastrar um produto com sucesso")
    void deveCadastrarProduto() {
        produtoService.cadastrar(produto1);

        List<Produto> produtos = produtoService.buscarTodos();

        assertEquals(1, produtos.size());
        assertEquals("PETR4", produtos.get(0).getCodigoNegociacao());
    }

    @Test
    @DisplayName("Deve cadastrar múltiplos produtos")
    void deveCadastrarMultiplosProdutos() {
        produtoService.cadastrar(produto1);
        produtoService.cadastrar(produto2);
        produtoService.cadastrar(produto3);

        List<Produto> produtos = produtoService.buscarTodos();

        assertEquals(3, produtos.size());
    }

    @Test
    @DisplayName("Deve buscar todos os produtos cadastrados")
    void deveBuscarTodosProdutos() {
        produtoService.cadastrar(produto1);
        produtoService.cadastrar(produto2);

        List<Produto> resultado = produtoService.buscarTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(produto1));
        assertTrue(resultado.contains(produto2));
    }

    @Test
    @DisplayName("Deve buscar produto por ticker existente")
    void deveBuscarProdutoPorTickerExistente() {
        produtoService.cadastrar(produto1);
        produtoService.cadastrar(produto2);

        Produto resultado = produtoService.buscarPorTicker("VALE3");

        assertNotNull(resultado);
        assertEquals("VALE3", resultado.getCodigoNegociacao());
        assertEquals(produto2, resultado);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar produto por ticker inexistente")
    void deveLancarExcecaoAoBuscarTickerInexistente() {
        produtoService.cadastrar(produto1);

        assertThrows(NoSuchElementException.class, () -> {
            produtoService.buscarPorTicker("ABCD3");
        });
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar em lista vazia")
    void deveLancarExcecaoAoBuscarEmListaVazia() {
        assertThrows(NoSuchElementException.class, () -> {
            produtoService.buscarPorTicker("PETR4");
        });
    }
}