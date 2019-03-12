package at.jku.dke.inga.data.configuration;

import javax.validation.constraints.NotBlank;

/**
 * Configuration properties for GraphDB HTTP-Connection.
 */
public class GraphDbHttpConfig {

    private String serverUrl;
    private String repositoryId;

    /**
     * Instantiates a new instance of class {@linkplain GraphDbHttpConfig}.
     */
    public GraphDbHttpConfig() {
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
