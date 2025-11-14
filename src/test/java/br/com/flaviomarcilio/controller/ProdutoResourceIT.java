package br.com.flaviomarcilio.controller;

import br.com.flaviomarcilio.model.Produto;
import br.com.flaviomarcilio.model.enums.TipoProduto;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.*;
import jakarta.inject.Inject;
import br.com.flaviomarcilio.fixtures.Fixture;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
@DisplayName("Testes de integração para o recurso produto")
public class ProdutoResourceIT {

    @Inject
    Fixture fixture;

    @AfterEach
    void tearDown() {
        fixture.excluiProdutos();
    }

    @Nested
    @DisplayName("Testes do endpoint GET /api/v1/produto")
    class BuscarTodosTests {

        @Test
        @DisplayName("Deve retornar lista vazia quando não houver produtos")
        void deveRetornarListaVaziaQuandoNaoHouverProdutos() {

            given()
                    .contentType(ContentType.JSON)
                    .when()
                    .get("/api/v1/produto")
                    .then()
                    .statusCode(Response.Status.OK.getStatusCode())
                    .body("size()", is(0));
        }

        @Test
        @DisplayName("Deve retornar lista de produtos com status 200")
        public void deveRetornarListaDeProdutosComSucesso() {
            Produto petr4 = new Produto(
                    "PETR4",
                    "PETROBRAS S.A.",
                    "33.000.167/0001-01",
                    "PETR4BR",
                    TipoProduto.PN,
                    "Bradesco");

            Produto vale3 = new Produto(
                    "VALE3",
                    "VALE S.A.",
                    "33.592.510/0001-54",
                    "VALE3BR",
                    TipoProduto.ON,
                    "Bradesco");

            fixture.insereProduto(petr4);
            fixture.insereProduto(vale3);

            given()
                    .contentType(ContentType.JSON)
                    .when()
                    .get("/api/v1/produto")
                    .then()
                    .statusCode(Response.Status.OK.getStatusCode())
                    .body("size()", is(2))
                    .body("[0].codigoNegociacao", equalTo("PETR4"))
                    .body("[1].codigoNegociacao", equalTo("VALE3"));
        }
    }

    @Nested
    @DisplayName("Testes do endpoint GET /api/v1/produto/{ticker}")
    class BuscarPorTickerTests {

        @Test
        @DisplayName("Deve retornar produto por ticker com status 200")
        public void deveRetornarProdutoPorTickerComSucesso() {

            Produto petr4 = new Produto(
                    "PETR4",
                    "PETROBRAS S.A.",
                    "33.000.167/0001-01",
                    "PETR4BR",
                    TipoProduto.PN,
                    "Bradesco");

            Produto vale3 = new Produto(
                    "VALE3",
                    "VALE S.A.",
                    "33.592.510/0001-54",
                    "VALE3BR",
                    TipoProduto.ON,
                    "Bradesco");

            fixture.insereProduto(petr4);
            fixture.insereProduto(vale3);

            given()
                    .contentType(ContentType.JSON)
                    .pathParam("ticker", "PETR4")
                    .when()
                    .get("/api/v1/produto/{ticker}")
                    .then()
                    .statusCode(Response.Status.OK.getStatusCode())
                    .body("codigoNegociacao", equalTo("PETR4"))
                    .body("nome", equalTo("PETROBRAS S.A."));
        }

        @Test
        @DisplayName("Deve retornar erro 404 quando ticker não existir")
        void deveRetornarErro404QuandoTickerNaoExistir() {

            Produto petr4 = new Produto(
                    "PETR4",
                    "PETROBRAS S.A.",
                    "33.000.167/0001-01",
                    "PETR4BR",
                    TipoProduto.PN,
                    "Bradesco");

            fixture.insereProduto(petr4);

            given()
                    .contentType(ContentType.JSON)
                    .pathParam("ticker", "VALE3")
                    .when()
                    .get("/api/v1/produto/{ticker}")
                    .then()
                    .statusCode(Response.Status.NOT_FOUND.getStatusCode());
        }
    }

    @Nested
    @DisplayName("Testes do endpoint POST /api/v1/produto")
    class CadastrarTests {

        @Test
        @DisplayName("Deve cadastrar produto com status 201")
        void deveCadastrarProdutoComSucesso() {

            Produto petr4 = new Produto(
                    "PETR4",
                    "PETROBRAS S.A.",
                    "33.000.167/0001-01",
                    "PETR4BR",
                    TipoProduto.PN,
                    "Bradesco");

            given()
                .contentType(ContentType.JSON)
                .body(petr4)
            .when()
                .post("/api/v1/produto")
            .then()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .header("Location", notNullValue());
        }

        @Test
        @DisplayName("Deve retornar Location header no cadastro")
        void deveRetornarLocationHeaderNoCadastro() {

            Produto petr4 = new Produto(
                    "PETR4",
                    "PETROBRAS S.A.",
                    "33.000.167/0001-01",
                    "PETR4BR",
                    TipoProduto.PN,
                    "Bradesco");

            given()
                .contentType(ContentType.JSON)
                .body(petr4)
            .when()
                .post("/api/v1/produto")
            .then()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .header("Location", containsString("/api/v1/produto"));
        }

        @Test
        @DisplayName("Deve retornar erro 400 para body vazio")
        void deveRetornarErro400ParaBodyVazio() {

            given()
                .contentType(ContentType.JSON)
            .when()
                .post("/api/v1/produto")
            .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
        }
    }
}
