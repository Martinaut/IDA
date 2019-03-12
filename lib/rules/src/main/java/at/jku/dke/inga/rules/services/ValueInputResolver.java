package at.jku.dke.inga.rules.services;

import at.jku.dke.inga.rules.models.ResolveValueInputModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * A service that ...
 */
@Service
public class ValueInputResolver extends DroolsService {

    private static final Logger LOG = LogManager.getLogger(ValueInputResolver.class);

    /**
     * Instantiates a new instance of class {@linkplain ValueInputResolver}.
     */
    public ValueInputResolver() {
        super(new String[]{"resolve-value-input"});
    }

    /**
     * Resolves the users intent after DisplayValues.
     *
     * @param dataModel The data model used by the rules engine.
     * @return The event to execute.
     * @throws IllegalArgumentException If dataModel is {@code null}.
     */
    public String resolveValueInput(ResolveValueInputModel dataModel) {
        if (dataModel == null) throw new IllegalArgumentException("dataModel must not be null.");

        LOG.info("Resolving value input");

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
