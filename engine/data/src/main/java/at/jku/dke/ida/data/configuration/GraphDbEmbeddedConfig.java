package at.jku.dke.ida.data.configuration;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Contains the configuration for embedded GraphDB.
 */
public class GraphDbEmbeddedConfig {

    private String directory;
    private String dataDirectory;
    private String indexDirectory;
    private String materializationDirectory;
    private String[] wordnetFiles;

    /**
     * Instantiates a new instance of class {@linkplain GraphDbEmbeddedConfig}.
     */
    public GraphDbEmbeddedConfig() {
    }

    /**
     * Gets the directory of the embedded database.
     *
     * @return the directory
     */
    @NotBlank
    public String getDirectory() {
        return directory;
    }

    /**
     * Sets the directory.
     *
     * @param directory the directory
     */
    public void setDirectory(String directory) {
        this.directory = directory;
    }

    /**
     * Gets the directory with the data files.
     *
     * @return the data directory
     */
    @NotBlank
    public String getDataDirectory() {
        return dataDirectory;
    }

    /**
     * Sets the directory with the data files.
     *
     * @param dataDirectory the data directory
     */
    public void setDataDirectory(String dataDirectory) {
        this.dataDirectory = dataDirectory;
    }

    /**
     * Gets the directory with the index files.
     *
     * @return the index directory
     */
    @NotBlank
    public String getIndexDirectory() {
        return indexDirectory;
    }

    /**
     * Sets the directory with the index files.
     *
     * @param indexDirectory the index directory
     */
    public void setIndexDirectory(String indexDirectory) {
        this.indexDirectory = indexDirectory;
    }

    /**
     * Gets the directory with the materialization files.
     *
     * @return the materialization directory
     */
    @NotBlank
    public String getMaterializationDirectory() {
        return materializationDirectory;
    }

    /**
     * Sets the directory with the materialization files.
     *
     * @param materializationDirectory the materialization directory
     */
    public void setMaterializationDirectory(String materializationDirectory) {
        this.materializationDirectory = materializationDirectory;
    }

    /**
     * Gets the wordnet files.
     *
     * @return the string [ ]
     */
    @NotNull
    @Size(min = 1)
    public String[] getWordnetFiles() {
        return wordnetFiles;
    }

    /**
     * Sets the wordnet files.
     *
     * @param wordnetFiles the wordnet files
     */
    public void setWordnetFiles(String[] wordnetFiles) {
        this.wordnetFiles = wordnetFiles;
    }
}
