package br.com.flaviomarcilio.controller;

import br.com.flaviomarcilio.fixtures.Fixture;
import br.com.flaviomarcilio.model.Negociacao;
import br.com.flaviomarcilio.model.Produto;
import br.com.flaviomarcilio.model.enums.TipoMercado;
import br.com.flaviomarcilio.model.enums.TipoNegociacao;
import br.com.flaviomarcilio.model.enums.TipoProduto;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.empty;

@QuarkusTest
@DisplayName("Testes de integração para o recurso negociação")
public class NegociacaoResourceIT {

    @Inject
    Fixture fixture;

    @AfterEach
    void teadDown() {
        fixture.excluirNegociacao();
    }

    @Nested
    @DisplayName("Teste do endpoint GET /api/v1/negociacao")
    class BuscarTodasTests {

        @Test
        @DisplayName("deve retornar lista de negociações com status 200")
        void deveRetornarListaComSucesso() {

            Negociacao compraPetr4 = new Negociacao(
                    LocalDate.now(),
                    TipoNegociacao.COMPRA,
                    TipoMercado.FRACIONARIO,
                    LocalDate.of(2025,12,12),
                    "Bradesco",
                    "PETR4",
                    10,
                    new BigDecimal("4.50"));

            Negociacao vendaVale3 = new Negociacao(
                    LocalDate.now(),
                    TipoNegociacao.VENDA,
                    TipoMercado.FRACIONARIO,
                    LocalDate.of(2025,12,12),
                    "Bradesco",
                    "VALE3",
                    10,
                    new BigDecimal("3.50"));

            fixture.insereNegociacao(compraPetr4);
            fixture.insereNegociacao(vendaVale3);

            given()
                .contentType(ContentType.JSON)
            .when()
                .get("/api/v1/negociacao")
            .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("size()", is(2))
                .body("[0].codigoNegociacao", equalTo("PETR4"))
                .body("[0].tipoNegociacao", equalTo("COMPRA"))
                .body("[1].codigoNegociacao", equalTo("VALE3"))
                .body("[1].tipoNegociacao", equalTo("VENDA"));
        }

        @Test
        @DisplayName("deve retornar lista vazia quando não houver negociações")
        void deveRetornarListaVazia() {

            given()
                .contentType(ContentType.JSON)
            .when()
                .get("/api/v1/negociacao")
            .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("size()", is(0))
                .body("$", empty());
        }

    }

    @Nested
    @DisplayName("Teste do endpoint GET /api/v1/negociacao/{id}")
    class BuscarPorIdTests {

//        @Test
//        @DisplayName("deve retornar negociação quando ID existe")
//        void deveRetornarNegociacaoQuandoIdExiste() {
//
//            Negociacao compraPetr4 = new Negociacao(
//                    LocalDate.now(),
//                    TipoNegociacao.COMPRA,
//                    TipoMercado.FRACIONARIO,
//                    LocalDate.of(2025,12,12),
//                    "Bradesco",
//                    "PETR4",
//                    10,
//                    new BigDecimal("4.50"));
//
//            fixture.insereNegociacao(compraPetr4);
//
//            given()
//                .contentType(ContentType.JSON)
//                .pathParam("id", 1L)
//            .when()
//                .get("/api/v1/negociacao/{id}")
//            .then()
//                .statusCode(Response.Status.OK.getStatusCode())
//                .body("id", equalTo(1))
//                .body("codigoNegociacao", equalTo("PETR4"))
//                .body("tipoNegociacao", equalTo("COMPRA"));
//        }

        @Test
        @DisplayName("deve retornar erro 404 quando ID não existe")
        void deveRetornarErro404QuandoIdNaoExiste() {

            Negociacao compraPetr4 = new Negociacao(
                    LocalDate.now(),
                    TipoNegociacao.COMPRA,
                    TipoMercado.FRACIONARIO,
                    LocalDate.of(2025,12,12),
                    "Bradesco",
                    "PETR4",
                    10,
                    new BigDecimal("4.50"));

            fixture.insereNegociacao(compraPetr4);

            given()
                .contentType(ContentType.JSON)
                .pathParam("id", 999L)
            .when()
                .get("/api/v1/negociacao/{id}")
            .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
        }
    }

    @Nested
    @DisplayName("Teste do endpoint POST /api/v1/negociacao")
    class CadastrarTests {

        @Test
        @DisplayName("deve cadastrar negociação de compra com sucesso quando produto estiver cadastrado")
        void deveCadastrarNegociacaoDeCompraComSucesso() {

            Produto petr4 = new Produto(
                    "PETR4",
                    "PETROBRAS S.A.",
                    "33.000.167/0001-01",
                    "PETR4BR",
                    TipoProduto.PN,
                    "Bradesco");

            fixture.insereProduto(petr4);

            Negociacao compraPetr4 = new Negociacao(
                    LocalDate.now(),
                    TipoNegociacao.COMPRA,
                    TipoMercado.FRACIONARIO,
                    LocalDate.of(2025,12,12),
                    "Bradesco",
                    "PETR4",
                    10,
                    new BigDecimal("4.50"));

            given()
                .contentType(ContentType.JSON)
                .body(compraPetr4)
            .when()
                .post("/api/v1/negociacao")
            .then()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .header("Location",  containsString("/api/v1/negociacao"));
        }

        @Test
        @DisplayName("deve retornar erro 400 para body vazio")
        void deveRetornarErro400ParaBodyVazio() {

            given()
                .contentType(ContentType.JSON)
            .when()
                .post("/api/v1/negociacao")
            .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
        }

        @Test
        @DisplayName("deve retornar erro 400 quando produto não está cadastrado")
        void deveRetornarErro400QuandoProdutoNaoCadastrado() {

            Negociacao vendaVale3 = new Negociacao(
                    LocalDate.now(),
                    TipoNegociacao.VENDA,
                    TipoMercado.FRACIONARIO,
                    LocalDate.of(2025,12,12),
                    "Bradesco",
                    "VALE3",
                    10,
                    new BigDecimal("3.50"));

            given()
                .contentType(ContentType.JSON)
                .body(vendaVale3)
            .when()
                .post("/api/v1/negociacao")
            .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
        }
    }
}
