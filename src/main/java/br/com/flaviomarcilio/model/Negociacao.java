package br.com.flaviomarcilio.model;

import br.com.flaviomarcilio.model.enums.TipoMercado;
import br.com.flaviomarcilio.model.enums.TipoNegociacao;
import io.smallrye.common.constraint.NotNull;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "negociacoes")
public class Negociacao {

    public Negociacao() {
    }

    public Negociacao(Long id, LocalDate data, TipoNegociacao tipoNegociacao, TipoMercado tipoMercado, LocalDate vencimento,
                      String instituicao, String codigoNegociacao, Integer quantidade, BigDecimal precoUnitario) {
        this.id = id;
        this.data = data;
        this.tipoNegociacao = tipoNegociacao;
        this.mercado = tipoMercado;
        this.vencimento = vencimento;
        this.instituicao = instituicao;
        this.codigoNegociacao = codigoNegociacao;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDate data;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_negociacao")
    private TipoNegociacao tipoNegociacao;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoMercado mercado;

    private LocalDate vencimento;

    private String instituicao;

    @NotNull
    @Column(name = "codigo_negociacao")
    private String codigoNegociacao;

    @NotNull
    private Integer quantidade;

    @NotNull
    @Column(name = "preco_unitario")
    private BigDecimal precoUnitario;

    public Long getId() {
        return id;
    }

    public LocalDate getData() {
        return data;
    }

    public TipoNegociacao getTipoNegociacao() {
        return tipoNegociacao;
    }

    public TipoMercado getMercado() {
        return mercado;
    }

    public LocalDate getVencimento() {
        return vencimento;
    }

    public String getInstituicao() {
        return instituicao;
    }

    public String getCodigoNegociacao() {
        return codigoNegociacao;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }
}
