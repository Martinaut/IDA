package at.jku.dke.inga.rules.services;

import at.jku.dke.inga.rules.models.ResolveParameterModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * A service that resolves the value to user wants to select.
 */
@Service
public class ParameterResolver extends DroolsService {

    private static final Logger LOG = LogManager.getLogger(ParameterResolver.class);

    /**
     * Instantiates a new instance of class {@linkplain ParameterResolver}.
     */
    public ParameterResolver() {
        super(new String[]{"resolve-parameter"});
    }

    public String resolveParameter(ResolveParameterModel dataModel) {
        if (dataModel == null) throw new IllegalArgumentException("dataModel must not be null.");

        LOG.info("Resolving parameter");

        // Set data
        getSession().insert(dataModel);
        getSession().setGlobal("LOGGER", LOG);

        // Execute
        getSession().fireAllRules();

        // Get data
        Collection<?> objs = getSession().getObjects(obj -> obj instanceof String);
        closeSession();

        return (String) objs.iterator().next();
    }
}
