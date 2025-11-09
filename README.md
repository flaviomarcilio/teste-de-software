# GERENCIADOR DE INVESTIMENTOS

[![codecov](https://codecov.io/github/flaviomarcilio/teste-de-software/graph/badge.svg?token=3GO8LDA830)](https://codecov.io/github/flaviomarcilio/teste-de-software)

Trabalho prático da disciplina Teste de Software - UFMG 2025/2

## 1. Desenvolvedor

- Flávio Marcilio de Oliveira

## 2. Sistema

O sistema a ser desenvolvido consiste de um Gerenciador de Investimentos. O Gerenciador de Investimentos terá as seguintes funcionalidades:

- Produto
  - Cadastrar produtos de investimentos (Ações e Fundos de Investimento Imobiliário);
  - Listar todos os produtos cadastrados;
  - Pesquisar produto por código do produto;
- Negociação
  - Cadastrar uma negociação realizada (Compra ou Venda);
  - Listar todas as negociações realizadas;
  - Pesquisar uma negociação pelo ID;
- Movimentação
  - Cadastrar uma movimentação em sua carteira de investimento (Rendimento, Dividendo, Juros sobre Capital Próprio);
  - Listar todas as movimentações ocorridas em sua carteira;
  - Pesquisar uma movimentação pelo ID;

## 3. Tecnologias 

O sistema será desenvolvido em Java, utilizando o framework Quarkus - um framework nativo do Kubernetes, projetado para otimizar a linguagem Java para containers. As dependências e build serão gerenciadas com o  Maven.

A camada de persistências será desenvolvida utilizando Hibernate ORM com Panache, uma implementação da JPA. O banco de dados será o PostgreSQL com versionamento gerenciado pelo Flyway.
