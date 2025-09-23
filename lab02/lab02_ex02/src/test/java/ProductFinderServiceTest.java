
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pt.ua.tqs.ISimpleHttpClient;
import pt.ua.tqs.Product;
import pt.ua.tqs.ProductFinderService;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(MockitoExtension.class)
public class ProductFinderServiceTest {

    @Mock
    private ISimpleHttpClient httpClient;

    @InjectMocks
    private ProductFinderService service;

    @Test
    public void testProductFinderService() {
        String jsonResponse1 = """
                {
                    "id":1,
                    "image":"https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_.jpg",
                    "description":"Your perfect pack for everyday use and walks in the forest. Stash your laptop (up to 15 inches) in the padded sleeve, your everyday",
                    "price":109.95,
                    "title":"Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops",
                    "category":"men's clothing"
                }""";

        String jsonResponse2 = """
                {
                    "id":2,
                    "image":"https://fakestoreapi.com/img/71YXzeOuslL._AC_UY879_.jpg",
                    "description":"Slim-fitting style, contrast raglan long sleeve, three-button henley placket, light weight & soft fabric for breathable and comfortable wearing. And Solid stitched shirts with round neck made for durability and a great fit for casual fashion wear and diehard baseball fans. The Henley style round neckline includes a three-button placket.",
                    "price":22.3,
                    "title":"Mens Casual Premium Slim Fit T-Shirts ",
                    "category":"men's clothing"
                }""";

        String jsonResponse3 = """
                {
                    "id":3,
                    "image":"https://fakestoreapi.com/img/71li-ujtlUL._AC_UX679_.jpg",
                    "description":"great outerwear jackets for Spring/Autumn/Winter, suitable for many occasions, such as working, hiking, camping, mountain/rock climbing, cycling, traveling or other outdoors. Good gift choice for you or your family member. A warm hearted love to Father, husband or son in this thanksgiving or Christmas Day.",
                    "price":55.99,
                    "title":"Mens Cotton Jacket",
                    "category":"men's clothing"
                }""";

        Product product1 = new Product("1", "Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops", "Your perfect pack for everyday use and walks in the forest. Stash your laptop (up to 15 inches) in the padded sleeve, your everyday", 109.95, "Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops", null);
        Product product2 = new Product("2", "Mens Casual Premium Slim Fit T-Shirts ", "Slim-fitting style, contrast raglan long sleeve, three-button henley placket, light weight & soft fabric for breathable and comfortable wearing. And Solid stitched shirts with round neck made for durability and a great fit for casual fashion wear and diehard baseball fans. The Henley style round neckline includes a three-button placket.", 22.3, "Mens Casual Premium Slim Fit T-Shirts ", null);
        Product product3 = new Product("3", "Mens Cotton Jacket", "great outerwear jackets for Spring/Autumn/Winter, suitable for many occasions, such as working, hiking, camping, mountain/rock climbing, cycling, traveling or other outdoors. Good gift choice for you or your family member. A warm hearted love to Father, husband or son in this thanksgiving or Christmas Day.", 55.99, "Mens Cotton Jacket", null);


        when(httpClient.doHttpGet(service.API_PRODUCTS + "/" + "1")).thenReturn(jsonResponse1);
        when(httpClient.doHttpGet( service.API_PRODUCTS + "/" + "2")).thenReturn(jsonResponse2);
        when(httpClient.doHttpGet(service.API_PRODUCTS + "/" + "3")).thenReturn(jsonResponse3);

        assertEquals(product1.id, service.findProductDetails(1).isPresent() ? service.findProductDetails(1).get().id : null);
        assertEquals(product2.id, service.findProductDetails(2).isPresent() ? service.findProductDetails(2).get().id : null);
        assertEquals(product3.id, service.findProductDetails(3).isPresent() ? service.findProductDetails(3).get().id : null);

        assertThat(service.findProductDetails(3).get(), notNullValue());
        assertEquals(3, service.findProductDetails(3).get().id);
        assertEquals("Mens Cotton Jacket", service.findProductDetails(3).get().title);

        assertThat(service.findProductDetails(300).isPresent(), equalTo(false));

    }
}
