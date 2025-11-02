package pt.ua.tqs.ZeroMonos;


import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("pt/ua/tqs/ZeroMonos")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "pt.ua.tqs.ZeroMonos")
public class CucumberTest {

}


