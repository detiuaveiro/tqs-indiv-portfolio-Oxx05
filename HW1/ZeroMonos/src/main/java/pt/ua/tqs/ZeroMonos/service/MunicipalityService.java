package pt.ua.tqs.ZeroMonos.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class MunicipalityService {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(MunicipalityService.class);

    private static final String MUNICIPALITIES_URL = "https://geoapi.pt/municipios?json=1";
    private final List<String> municipalities = new CopyOnWriteArrayList<>();
    private final RestTemplate restTemplate = new RestTemplate();

    @PostConstruct
    public void init() {
        fetchMunicipalities();
    }

    public synchronized void fetchMunicipalities() {
        try {
            String[] response = restTemplate.getForObject(MUNICIPALITIES_URL, String[].class);
            if (response != null && response.length > 0) {
                municipalities.clear();
                Collections.addAll(municipalities, response);
                logger.info("Updated {} municipalities", municipalities.size());
            }
        } catch (Exception e) {
            logger.error("Failed to fetch municipalities", e);
        }
    }

    public List<String> getMunicipalities() {
        return Collections.unmodifiableList(municipalities);
    }
}
