import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pt.ua.tqs.IStockmarketService;
import pt.ua.tqs.Stock;
import pt.ua.tqs.StocksPortfolio;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)

public class StocksPortfolioTest {

    @Mock
    IStockmarketService stockmarketService;
    @InjectMocks
    StocksPortfolio stocksPortfolio;

    @Test
    void testTotalValue() {

    when(stockmarketService.lookUpPrice("Google")).thenReturn(3000.0);
    when(stockmarketService.lookUpPrice("Amazon")).thenReturn(3500.0);

    Stock google = new Stock("Google", 1);
    Stock amazon = new Stock("Amazon", 2);


    stocksPortfolio.addStock(google);
    stocksPortfolio.addStock(amazon);

    double totalValue = stocksPortfolio.totalValue();
    assertThat(totalValue).isEqualTo(10000.0);
    assertThat(totalValue).isBetween(9999.0, 10001.0);

    // Note: Poderia também fazer sem o @Mock e @InjectMocks
    // IStockmarketService stockmarketService = Mockito.mock(IStockmarketService.class);
    // StocksPortfolio stocksPortfolio = new StocksPortfolio(stockmarketService);
    }

    @Test
    void topNStocks() {
        stocksPortfolio.addStock(new Stock("Google", 1));
        stocksPortfolio.addStock(new Stock("Amazon", 2));
        System.out.println(stockmarketService.lookUpPrice("Google"));
        System.out.println(stockmarketService.lookUpPrice("Amazon"));

        when(stockmarketService.lookUpPrice("Google")).thenReturn(3000.0);
        when(stockmarketService.lookUpPrice("Amazon")).thenReturn(3500.0);

        System.out.println(stockmarketService.lookUpPrice("Google"));
        System.out.println(stockmarketService.lookUpPrice("Amazon"));

        assertThat(stocksPortfolio.mostValuableStocks(1).get(0).getLabel()).isEqualTo("Amazon");
    }
}
