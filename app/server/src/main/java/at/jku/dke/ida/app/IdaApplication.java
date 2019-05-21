package at.jku.dke.ida.app;

import at.jku.dke.ida.csp.ConstraintSatisfactionSpringConfiguration;
import at.jku.dke.ida.data.configuration.DataSpringConfiguration;
import at.jku.dke.ida.scxml.configuration.SCXMLSpringConfiguration;
import at.jku.dke.ida.shared.spring.SharedSpringConfiguration;
import at.jku.dke.ida.web.configuration.WebSpringConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * The main class of the application.
 */
@SpringBootApplication
@Import({SharedSpringConfiguration.class, DataSpringConfiguration.class, WebSpringConfiguration.class,
        ConstraintSatisfactionSpringConfiguration.class, SCXMLSpringConfiguration.class})
public class IdaApplication {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(IdaApplication.class, args);
    }
}
