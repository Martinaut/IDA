package at.jku.dke.inga.data.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Contains the configuration for GraphDB.
 */
@Component
@ConfigurationProperties(prefix = "graphdb")
public class GraphDbConfig {

    private GraphDbEmbeddedConfig embedded;
    private GraphDbHttpConfig http;

    /**
     * Instantiates a new instance of class {@linkplain GraphDbConfig}.
     */
    public GraphDbConfig() {
    }

    /**
     * Gets the embedded connection configuration.
     *
     * @return the embedded connection configuration
     */
    public GraphDbEmbeddedConfig getEmbedded() {
        return embedded;
    }

    /**
     * Sets the embedded connection configuration.
     *
     * @param embedded the embedded connection configuration
     */
    public void setEmbedded(GraphDbEmbeddedConfig embedded) {
        this.embedded = embedded;
    }

    /**
     * Gets the http connection configuration.
     *
     * @return the http connection configuration
     */
    public GraphDbHttpConfig getHttp() {
        return http;
    }

    /**
     * Sets the http connection configuration.
     *
     * @param http the http connection configuration
     */
    public void setHttp(GraphDbHttpConfig http) {
        this.http = http;
    }
}
