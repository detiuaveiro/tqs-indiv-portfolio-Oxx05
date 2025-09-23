package pt.ua.tqs;

import java.util.ArrayList;
import java.util.List;

public class StocksPortfolio {
    private final IStockmarketService stocks;
    private final List<Stock> stockmarket = new ArrayList<>();

    public StocksPortfolio(IStockmarketService stock){
        stocks = stock;
    }

    public void addStock(Stock s){
        stockmarket.add(s);
    }

    public double totalValue(){
        double val = 0.0;
        for (Stock s : stockmarket){
            val += s.getQuantity() * stocks.lookUpPrice(s.getLabel());
        }
        return val;
    }



    public List<Stock> mostValuableStocks(int topN) {
        return stockmarket.stream()
                .sorted((s1, s2) -> Double.compare(
                        s2.getQuantity() * stocks.lookUpPrice(s2.getLabel()),
                        s1.getQuantity() * stocks.lookUpPrice(s1.getLabel())))
                .limit(topN)
                .toList();
    }
}
