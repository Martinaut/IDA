package at.jku.dke.ida.scxml.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Configuration with services from scxml-library.
 */
@Configuration
@ComponentScan({"at.jku.dke.ida.scxml.configuration", "at.jku.dke.ida.scxml.query"})
@EnableConfigurationProperties(SCXMLConfig.class)
public class SCXMLSpringConfiguration {
    /**
     * Instantiates a new instance of class {@linkplain SCXMLSpringConfiguration}.
     */
    public SCXMLSpringConfiguration() {
    }
}
