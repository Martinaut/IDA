package at.jku.dke.inga.data.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Configuration with services from data-library.
 */
@Configuration
@ComponentScan({"at.jku.dke.inga.data.repositories", "at.jku.dke.inga.data.configuration"})
@EnableConfigurationProperties(GraphDbConfig.class)
public class DataSpringConfiguration {
    /**
     * Instantiates a new instance of class {@linkplain DataSpringConfiguration}.
     */
    public DataSpringConfiguration() {
    }
}
