package at.jku.dke.ida.csp;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Configuration with services from CSP.
 */
@Configuration
@ComponentScan({"at.jku.dke.ida.csp"})
@EnableConfigurationProperties(ConstraintSatisfactionSettings.class)
public class ConstraintSatisfactionSpringConfiguration {
    /**
     * Instantiates a new instance of class {@linkplain ConstraintSatisfactionSpringConfiguration}.
     */
    public ConstraintSatisfactionSpringConfiguration() {
    }
}
