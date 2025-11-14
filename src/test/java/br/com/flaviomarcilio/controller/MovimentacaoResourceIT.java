package br.com.flaviomarcilio.controller;

import br.com.flaviomarcilio.fixtures.Fixture;
import br.com.flaviomarcilio.model.Movimentacao;
import br.com.flaviomarcilio.model.enums.TipoMovimentacao;
import br.com.flaviomarcilio.model.enums.TipoTransacao;
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
@DisplayName("Testes de Integração para o Recurso Movimentação")
public class MovimentacaoResourceIT {

    @Inject
    Fixture fixture;

    @AfterEach
    void tearDown() {
        fixture.excluiMovimentacoes();
    }

    @Nested
    @DisplayName("Testes do endpoint GET /api/v1/movimentacao")
    class BuscarTodosTests {

        @Test
        @DisplayName("Deve retornar lista vazia quando não houver movimentações")
        void deveRetornarListaVaziaQuandoNaoHouverMovimentacoes() {

            given()
                .contentType(ContentType.JSON)
            .when()
                .get("/api/v1/movimentacao")
            .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("size()", is(0))
                .body("$", empty());
        }

        @Test
        @DisplayName("Deve retornar lista de movimentações com status 200")
        void deveRetornarListaDeMovimentacoesComSucesso() {

            Movimentacao dividendosPetr4 = new Movimentacao(
                    TipoTransacao.CREDITO,
                    LocalDate.now(),
                    TipoMovimentacao.DIVIDENDO,
                    "PETR4",
                    "BB Banco de Investimentos",
                    10,
                    new BigDecimal("4.50"));

            Movimentacao jcpVale3 = new Movimentacao(
                    TipoTransacao.CREDITO,
                    LocalDate.now(),
                    TipoMovimentacao.JUROS_SOBRE_CAPITAL_PROPRIO,
                    "VALE3",
                    "BB Banco de Investimentos",
                    10,
                    new BigDecimal("3.50"));

            fixture.insereMovimentacao(dividendosPetr4);
            fixture.insereMovimentacao(jcpVale3);

            given()
                .contentType(ContentType.JSON)
            .when()
                .get("/api/v1/movimentacao")
            .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("size()", is(2))
                .body("[0].codigoNegociacao", equalTo("PETR4"))
                .body("[0].tipoMovimentacao", equalTo("DIVIDENDO"))
                .body("[1].codigoNegociacao", equalTo("VALE3"))
                .body("[1].tipoMovimentacao", equalTo("JUROS_SOBRE_CAPITAL_PROPRIO"));
        }
    }

    @Nested
    @DisplayName("Testes do endpoint GET /api/v1/movimentacao/{id}")
    class BuscarPorIdTests {

        @Test
        @DisplayName("Deve retornar erro 404 quando ID não existir")
        void deveRetornarErro404QuandoIdNaoExistir() {

            Movimentacao dividendosPetr4 = new Movimentacao(
                    TipoTransacao.CREDITO,
                    LocalDate.now(),
                    TipoMovimentacao.DIVIDENDO,
                    "PETR4",
                    "BB Banco de Investimentos",
                    10,
                    new BigDecimal("4.50"));

            Movimentacao jcpVale3 = new Movimentacao(
                    TipoTransacao.CREDITO,
                    LocalDate.now(),
                    TipoMovimentacao.JUROS_SOBRE_CAPITAL_PROPRIO,
                    "VALE3",
                    "BB Banco de Investimentos",
                    10,
                    new BigDecimal("3.50"));

            fixture.insereMovimentacao(dividendosPetr4);
            fixture.insereMovimentacao(jcpVale3);

            given()
                .contentType(ContentType.JSON)
                .pathParam("id", 999L)
            .when()
                .get("/api/v1/movimentacao/{id}")
            .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
        }

//        @Test
//        @DisplayName("Deve retornar movimentação por ID com status 200")
//        void deveRetornarMovimentacaoPorIdComSucesso() {
//
//            Movimentacao dividendosPetr4 = new Movimentacao(
//                    TipoTransacao.CREDITO,
//                    LocalDate.now(),
//                    TipoMovimentacao.DIVIDENDO,
//                    "PETR4",
//                    "BB Banco de Investimentos",
//                    10,
//                    new BigDecimal("4.50"));
//
//            Movimentacao jcpVale3 = new Movimentacao(
//                    TipoTransacao.CREDITO,
//                    LocalDate.now(),
//                    TipoMovimentacao.JUROS_SOBRE_CAPITAL_PROPRIO,
//                    "VALE3",
//                    "BB Banco de Investimentos",
//                    10,
//                    new BigDecimal("3.50"));
//
//            fixture.insereMovimentacao(dividendosPetr4);
//            fixture.insereMovimentacao(jcpVale3);
//
//            given()
//                .contentType(ContentType.JSON)
//                .pathParam("id", 1L)
//            .when()
//                .get("/api/v1/movimentacao/{id}")
//            .then()
//                .statusCode(Response.Status.OK.getStatusCode())
//                .body("id", equalTo(1))
//                .body("codigoNegociacao", equalTo("PETR4"))
//                .body("tipoMovimentacao", equalTo("DIVIDENDO"));
//        }
    }

    @Nested
    @DisplayName("Testes do endpoint POST /api/v1/movimentacao")
    class CadastrarTests {

        @Test
        @DisplayName("Deve cadastrar movimentação com status 201")
        void deveCadastrarMovimentacaoComSucesso() {

            Movimentacao dividendosPetr4 = new Movimentacao(
                    TipoTransacao.CREDITO,
                    LocalDate.now(),
                    TipoMovimentacao.DIVIDENDO,
                    "PETR4",
                    "BB Banco de Investimentos",
                    10,
                    new BigDecimal("4.50"));

            given()
                .contentType(ContentType.JSON)
                .body(dividendosPetr4)
            .when()
                .post("/api/v1/movimentacao")
            .then()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .header("Location", containsString("/api/v1/movimentacao"));
        }

        @Test
        @DisplayName("Deve retornar erro 400 para body vazio")
        void deveRetornarErro400ParaBodyVazio() {

            given()
                .contentType(ContentType.JSON)
            .when()
                .post("/api/v1/movimentacao")
            .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
        }
    }
}
