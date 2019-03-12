package at.jku.dke.inga.data.configuration;

import javax.validation.constraints.NotBlank;

/**
 * Configuration properties for embedded GraphDB connection.
 */
public class GraphDbEmbeddedConfig {

    private String directory;

    /**
     * Instantiates a new instance of class {@linkplain GraphDbEmbeddedConfig}.
     */
    public GraphDbEmbeddedConfig() {
    }

    /**
     * Gets directory.
     *
     * @return the directory
     */
    @NotBlank
    public String getDirectory() {
        return directory;
    }

    /**
     * Sets directory.
     *
     * @param directory the directory
     */
    public void setDirectory(String directory) {
        this.directory = directory;
    }
}
