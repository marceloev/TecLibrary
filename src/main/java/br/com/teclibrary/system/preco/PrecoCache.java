package br.com.teclibrary.system.preco;

public class PrecoCache {

    private PrecoProduto precoProduto;
    private Double vlrBruto;
    private Double vlrLiquido;

    public PrecoCache(PrecoProduto precoProduto, Double vlrBruto, Double vlrLiquido) {
        this.precoProduto = precoProduto;
        this.vlrBruto = vlrBruto;
        this.vlrLiquido = vlrLiquido;
    }

    public PrecoProduto getPrecoProduto() {
        return precoProduto;
    }

    public void setPrecoProduto(PrecoProduto precoProduto) {
        this.precoProduto = precoProduto;
    }

    public Double getVlrBruto() {
        return vlrBruto;
    }

    public void setVlrBruto(Double vlrBruto) {
        this.vlrBruto = vlrBruto;
    }

    public Double getVlrLiquido() {
        return vlrLiquido;
    }

    public void setVlrLiquido(Double vlrLiquido) {
        this.vlrLiquido = vlrLiquido;
    }

    @Override
    public String toString() {
        return "PrecoCache{" +
                "precoProduto=" + precoProduto +
                ", vlrBruto=" + vlrBruto +
                ", vlrLiquido=" + vlrLiquido +
                '}';
    }
}
