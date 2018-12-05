package br.com.teclibrary.system.preco;

import java.util.Objects;

public class PrecoProduto {

    private int codigo;
    private String descricao;
    private String codigoBarra;
    private String preco;
    private Double precoDbl;

    public PrecoProduto() {

    }

    public PrecoProduto(int codigo, String descricao, String codigoBarra, Boolean calculaPreco) {
        this.codigo = codigo;
        this.descricao = descricao;
        this.codigoBarra = codigoBarra;
        if (calculaPreco) this.preco = PrecoRetriever.getFmtPrecoBruto(this.codigo);
    }

    public PrecoProduto(int codigo, String descricao, String codigoBarra) {
        this.codigo = codigo;
        this.descricao = descricao;
        this.codigoBarra = codigoBarra;
        this.preco = PrecoRetriever.getFmtPrecoBruto(this.codigo);
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCodigoBarra() {
        return codigoBarra;
    }

    public void setCodigoBarra(String codigoBarra) {
        this.codigoBarra = codigoBarra;
    }

    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }

    public Double getPrecoDbl() {
        return precoDbl;
    }

    public void setPrecoDbl(Double precoDbl) {
        this.precoDbl = precoDbl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PrecoProduto)) return false;
        PrecoProduto that = (PrecoProduto) o;
        return getCodigo() == that.getCodigo() &&
                Objects.equals(getDescricao(), that.getDescricao()) &&
                Objects.equals(getCodigoBarra(), that.getCodigoBarra()) &&
                Objects.equals(getPreco(), that.getPreco());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCodigo(), getDescricao(), getCodigoBarra(), getPreco());
    }

    @Override
    public String toString() {
        return "PrecoProduto{" +
                "codigo=" + codigo +
                ", descricao='" + descricao + '\'' +
                ", codigoBarra='" + codigoBarra + '\'' +
                ", preco='" + preco + '\'' +
                ", precoDbl=" + precoDbl +
                '}';
    }
}
