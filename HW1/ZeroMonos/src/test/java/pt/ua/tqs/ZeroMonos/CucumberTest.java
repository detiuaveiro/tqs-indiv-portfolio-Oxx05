package pt.ua.tqs.ZeroMonos;

import io.cucumber.junit.platform.engine.Constants;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import org.springframework.boot.test.context.SpringBootTest;


@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("pt/ua/tqs/ZeroMonos")
@ConfigurationParameter(key = Constants.GLUE_PROPERTY_NAME, value = "pt.ua.tqs.ZeroMonos")
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = "server.port=8080"
)
public class CucumberTest {
}