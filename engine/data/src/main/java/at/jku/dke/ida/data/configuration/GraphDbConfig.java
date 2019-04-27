package at.jku.dke.ida.data.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;

/**
 * Contains the connection configuration for GraphDB.
 */
@Component
@Primary
@ConfigurationProperties(prefix = "graphdb")
public class GraphDbConfig {

    private String serverUrl;
    private String repositoryId;

    /**
     * Instantiates a new instance of class {@linkplain GraphDbConfig}.
     */
    public GraphDbConfig() {
    }

    /**
     * Gets the server url.
     *
     * @return the server url
     */
    @NotBlank
    public String getServerUrl() {
        return serverUrl;
    }

    /**
     * Sets the server url.
     *
     * @param serverUrl the server url
     */
    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    /**
     * Gets the repository id.
     *
     * @return the repository id
     */
    @NotBlank
    public String getRepositoryId() {
        return repositoryId;
    }

    /**
     * Sets the repository id.
     *
     * @param repositoryId the repository id
     */
    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }
}
