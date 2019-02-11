package at.jku.dke.inga.rules.services;

import at.jku.dke.inga.rules.models.ResolveActionModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * A service that resolves the action the user wants to execute.
 */
@Service
public class ActionResolver extends DroolsService {

    private static final Logger LOG = LogManager.getLogger(ActionResolver.class);

    /**
     * Instantiates a new instance of class {@linkplain ActionResolver}.
     */
    public ActionResolver() {
        super(new String[]{"resolve-action"});
    }

    /**
     * Resolves the users intent after ResolveUserInputs.
     *
     * @param dataModel The data model used by the rules engine.
     * @return The event to execute.
     * @throws IllegalArgumentException If dataModel is {@code null}.
     */
    public String resolveAction(ResolveActionModel dataModel) {
        if (dataModel == null) throw new IllegalArgumentException("dataModel must not be null.");

        LOG.info("Resolving action");

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
