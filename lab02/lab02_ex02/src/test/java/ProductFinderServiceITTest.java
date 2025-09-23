import org.junit.jupiter.api.Test;
import pt.ua.tqs.ProductFinderService;
import pt.ua.tqs.TqsBasicHttpClient;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductFinderServiceITTest {

    @Test
    public void testProductFinderService() {
        TqsBasicHttpClient client = new TqsBasicHttpClient();
        ProductFinderService service = new ProductFinderService(client);

        assertEquals(1, service.findProductDetails(1).isPresent() ? service.findProductDetails(1).get().id : null);
        assertEquals(2, service.findProductDetails(2).isPresent() ? service.findProductDetails(2).get().id : null);
        assertEquals(3, service.findProductDetails(3).isPresent() ? service.findProductDetails(3).get().id : null);

        assertEquals("Mens Cotton Jacket", service.findProductDetails(3).get().title);

        assertThat(service.findProductDetails(300).isPresent(), equalTo(false));

    }
}
