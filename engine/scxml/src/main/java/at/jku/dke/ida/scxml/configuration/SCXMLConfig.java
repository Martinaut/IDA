package at.jku.dke.ida.scxml.configuration;

import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;

/**
 * Contains the connection configuration for GraphDB.
 */
@Component
@Primary
@ConfigurationProperties(prefix = "scxml")
public class SCXMLConfig {

    private String queryEndpoint;

    /**
     * Instantiates a new instance of class {@linkplain SCXMLConfig}.
     */
    public SCXMLConfig() {
    }

    /**
     * Gets the query endpoint.
     *
     * @return the query endpoint
     */
    @NotBlank
    @URL
    public String getQueryEndpoint() {
        return queryEndpoint;
    }

    /**
     * Sets the query endpoint.
     *
     * @param queryEndpoint the query endpoint
     */
    public void setQueryEndpoint(String queryEndpoint) {
        this.queryEndpoint = queryEndpoint;
    }

}
