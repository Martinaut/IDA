package at.jku.dke.ida.data.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * Contains the connection configuration for GraphDB.
 */
@Component
@Primary
@ConfigurationProperties(prefix = "graphdb")
public class GraphDbConfig {

    private GraphDbRemoteConfig remote;

    /**
     * Instantiates a new instance of class {@linkplain GraphDbConfig}.
     */
    public GraphDbConfig() {
    }

    /**
     * Gets the remote connection configuration.
     *
     * @return the remote
     */
    public GraphDbRemoteConfig getRemote() {
        return remote;
    }

    /**
     * Sets the remote connection configuration.
     *
     * @param remote the connection configuration
     */
    public void setRemote(GraphDbRemoteConfig remote) {
        this.remote = remote;
    }
}
