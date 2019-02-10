package at.jku.dke.inga.rules.services;

import at.jku.dke.inga.rules.models.ResolveOperationsDataModel;
import at.jku.dke.inga.shared.operations.Operation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A service that determines all possible operations that can be executed on the current analysis situation and context.
 */
@Service
public class OperationsResolver extends DroolsService {

    private static final Logger LOG = LogManager.getLogger(OperationsResolver.class);

    /**
     * Instantiates a new instance of class {@linkplain OperationsResolver}.
     */
    public OperationsResolver() {
        super(new String[]{"operation-determination"});
    }

    /**
     * Resolves the operations that may be executed in the current state and context.
     *
     * @param dataModel The data model used by the rules engine.
     * @return All possible operations.
     * @throws IllegalArgumentException If dataModel is {@code null}.
     */
    public List<Operation> resolveOperations(ResolveOperationsDataModel dataModel) {
        if (dataModel == null) throw new IllegalArgumentException("dataModel must not be null.");

        LOG.info("Resolving operations");
        getSession().insert(dataModel);
        getSession().insert(dataModel.getAnalysisSituation());

        getSession().fireAllRules();
        Collection<?> objs = getSession().getObjects(object -> object instanceof Operation);
        closeSession();

        return objs.stream()
                .map(x -> (Operation) x)
                .sorted()
                .collect(Collectors.toList());
    }
}
