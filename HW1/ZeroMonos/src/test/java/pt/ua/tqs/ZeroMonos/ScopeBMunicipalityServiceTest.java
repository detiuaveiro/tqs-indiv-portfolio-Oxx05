// java
package pt.ua.tqs.ZeroMonos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ua.tqs.ZeroMonos.service.MunicipalityService;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ScopeBMunicipalityServiceTest {

    private MunicipalityService service;
    private RestTemplate restTemplate;

    @BeforeEach
    void setup() throws Exception {
        service = new MunicipalityService();

        restTemplate = mock(RestTemplate.class);
        Field rtField = MunicipalityService.class.getDeclaredField("restTemplate");
        rtField.setAccessible(true);

        rtField.set(service, restTemplate);
    }

    @Test
    void fetchMunicipalities_withValidResponse_populatesList() {
        String[] response = new String[] {"Anadia", "Aveiro", "Águeda"};
        when(restTemplate.getForObject(anyString(), eq(String[].class))).thenReturn(response);

        service.fetchMunicipalities();

        List<String> list = service.getMunicipalities();
        assertEquals(3, list.size());
        assertTrue(list.contains("Anadia"));
        assertTrue(list.contains("Aveiro"));
        assertTrue(list.contains("Águeda"));
    }

    @Test
    void fetchMunicipalities_withNullResponse_keepsEmptyList() {
        when(restTemplate.getForObject(anyString(), eq(String[].class))).thenReturn(null);

        service.fetchMunicipalities();

        List<String> list = service.getMunicipalities();
        assertEquals(0, list.size());
    }

    @Test
    void fetchMunicipalities_onException_keepsEmptyList() {
        when(restTemplate.getForObject(anyString(), eq(String[].class))).thenThrow(new RuntimeException("fail"));

        service.fetchMunicipalities();

        List<String> list = service.getMunicipalities();
        assertEquals(0, list.size());
    }

    @Test
    void getMunicipalities_returnsUnmodifiableList() {
        String[] response = new String[] {"Anadia"};
        when(restTemplate.getForObject(anyString(), eq(String[].class))).thenReturn(response);

        service.fetchMunicipalities();

        List<String> list = service.getMunicipalities();
        assertThrows(UnsupportedOperationException.class, () -> list.add("X"));
    }
}
