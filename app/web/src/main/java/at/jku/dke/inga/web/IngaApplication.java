package at.jku.dke.inga.web;

import at.jku.dke.inga.data.configuration.DataConfiguration;
import at.jku.dke.inga.shared.SharedSpringConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({SharedSpringConfiguration.class, DataConfiguration.class})
public class IngaApplication {
    public static void main(String[] args) {
        SpringApplication.run(IngaApplication.class, args);
    }
}
