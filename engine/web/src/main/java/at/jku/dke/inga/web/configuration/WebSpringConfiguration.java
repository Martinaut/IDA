package at.jku.dke.inga.web.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Configuration with services from web-library.
 */
@Configuration
@ComponentScan({"at.jku.dke.inga.web.configuration", "at.jku.dke.inga.web.controllers", "at.jku.dke.inga.web.listeners"})
public class WebSpringConfiguration {
    /**
     * Instantiates a new instance of class {@linkplain WebSpringConfiguration}.
     */
    public WebSpringConfiguration() {
    }
}
