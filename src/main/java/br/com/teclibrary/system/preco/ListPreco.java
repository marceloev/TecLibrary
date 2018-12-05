package br.com.teclibrary.system.preco;

import java.util.ArrayList;
import java.util.List;

public class ListPreco {

    private List<PrecoProduto> precoProdutoList = new ArrayList<>();

    public List<PrecoProduto> getPrecoProdutoList() {
        return precoProdutoList;
    }

    public void setPrecoProdutoList(List<PrecoProduto> precoProdutoList) {
        this.precoProdutoList = precoProdutoList;
    }

    public void addToList(PrecoProduto precoProduto) {
        this.precoProdutoList.add(precoProduto);
    }

    @Override
    public String toString() {
        return "ListPrecos{" +
                "precoProdutoList=" + precoProdutoList +
                '}';
    }


}
