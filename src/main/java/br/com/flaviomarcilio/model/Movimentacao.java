package br.com.flaviomarcilio.model;

import br.com.flaviomarcilio.model.enums.TipoMovimentacao;
import br.com.flaviomarcilio.model.enums.TipoTransacao;
import io.smallrye.common.constraint.NotNull;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "movimentacoes")
public class Movimentacao {

    public Movimentacao() {
    }

    public Movimentacao(Long id, TipoTransacao tipoTransacao, Date data, TipoMovimentacao tipoMovimentacao,
                        String codigoNegociacao, String instituicao, Integer quantidade, BigDecimal precoUnitario) {
        this.id = id;
        this.tipoTransacao = tipoTransacao;
        this.data = data;
        this.tipoMovimentacao = tipoMovimentacao;
        this.codigoNegociacao = codigoNegociacao;
        this.instituicao = instituicao;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoTransacao tipoTransacao;

    @NotNull
    private Date data;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_movimentacao")
    private TipoMovimentacao tipoMovimentacao;

    @NotNull
    @Column(name = "codigo_negociacao")
    private String codigoNegociacao;

    @NotNull
    private String instituicao;

    @NotNull
    private Integer quantidade;

    @NotNull
    @Column(name = "preco_unitario")
    private BigDecimal precoUnitario;

    public Long getId() {
        return id;
    }

    public TipoTransacao getTransacao() {
        return tipoTransacao;
    }

    public Date getData() {
        return data;
    }

    public TipoMovimentacao getTipoMovimentacao() {
        return tipoMovimentacao;
    }

    public String getCodigoNegociacao() {
        return codigoNegociacao;
    }

    public String getInstituicao() {
        return instituicao;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }
}
