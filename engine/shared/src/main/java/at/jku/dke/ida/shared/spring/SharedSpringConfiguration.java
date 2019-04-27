package at.jku.dke.ida.shared.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Configuration with services from shared-library.
 */
@Configuration
@ComponentScan("at.jku.dke.ida.shared.spring")
public class SharedSpringConfiguration {
    /**
     * Instantiates a new instance of class {@linkplain SharedSpringConfiguration}.
     */
    public SharedSpringConfiguration() {
    }
}
