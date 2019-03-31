package at.jku.dke.inga.app;

import at.jku.dke.inga.data.configuration.DataSpringConfiguration;
import at.jku.dke.inga.shared.spring.SharedSpringConfiguration;
import at.jku.dke.inga.web.configuration.WebSpringConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * The main class of the application.
 */
@SpringBootApplication
@Import({SharedSpringConfiguration.class, DataSpringConfiguration.class, WebSpringConfiguration.class})
public class IngaApplication {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(IngaApplication.class, args);
    }
}
