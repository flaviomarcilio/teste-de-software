package br.com.flaviomarcilio.model;

import br.com.flaviomarcilio.model.enums.TipoProduto;
import io.smallrye.common.constraint.NotNull;
import jakarta.persistence.*;

@Entity
@Table(name = "produtos")
public class Produto {

    public Produto() {
    }

    public Produto(String codigoNegociacao, String nome, String cnpj, String codigoISIN,
                   TipoProduto tipoProduto, String administrador) {

        this.codigoNegociacao = codigoNegociacao;
        this.nome = nome;
        this.cnpj = cnpj;
        this.codigoISIN = codigoISIN;
        this.tipoProduto = tipoProduto;
        this.administrador = administrador;
    }

    @Id
    @Column(name = "codigo_negociacao")
    private String codigoNegociacao;

    @NotNull
    private String nome;

    @NotNull
    private String cnpj;

    @NotNull
    @Column(name = "codigo_isin")
    private String codigoISIN;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_produto")
    private TipoProduto tipoProduto;

    private String administrador;

    public String getCodigoNegociacao() {
        return codigoNegociacao;
    }

    public String getNome() {
        return nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getCodigoISIN() {
        return codigoISIN;
    }

    public TipoProduto getTipoProduto() {
        return tipoProduto;
    }

    public String getAdministrador() {
        return administrador;
    }
}
