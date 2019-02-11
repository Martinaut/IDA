package at.jku.dke.inga.rules.services;

import at.jku.dke.inga.rules.models.ResolveOperationInputModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * A service that .
 */
@Service
public class OperationInputResolver extends DroolsService {

    private static final Logger LOG = LogManager.getLogger(OperationInputResolver.class);

    /**
     * Instantiates a new instance of class {@linkplain OperationInputResolver}.
     */
    public OperationInputResolver() {
        super(new String[]{"resolve-operation-input"});
    }

    /**
     * Resolves the users intent after DisplayOperations.
     *
     * @param dataModel The data model used by the rules engine.
     * @return The event to execute.
     * @throws IllegalArgumentException If dataModel is {@code null}.
     */
    public String resolveOperationInput(ResolveOperationInputModel dataModel) {
        if (dataModel == null) throw new IllegalArgumentException("dataModel must not be null.");

        LOG.info("Resolving operation input");

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
