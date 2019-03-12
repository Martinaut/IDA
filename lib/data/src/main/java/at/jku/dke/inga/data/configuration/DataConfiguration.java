package at.jku.dke.inga.data.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"at.jku.dke.inga.data.repositories"})
@EnableConfigurationProperties(GraphDbConfig.class)
public class DataConfiguration {
}
