package at.jku.dke.inga.scxml.actions;

import at.jku.dke.inga.data.models.Cube;
import at.jku.dke.inga.data.models.CubeElement;
import at.jku.dke.inga.data.repositories.CubeElementRepository;
import at.jku.dke.inga.data.repositories.CubeRepository;
import at.jku.dke.inga.rules.models.ResolveOperationsDataModel;
import at.jku.dke.inga.rules.services.OperationsResolver;
import at.jku.dke.inga.shared.models.NonComparativeAnalysisSituation;
import at.jku.dke.inga.shared.operations.Operation;
import org.apache.commons.scxml2.ActionExecutionContext;
import org.apache.commons.scxml2.SCXMLExpressionException;
import org.apache.commons.scxml2.model.ModelException;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Action that determines the possible operations and sends them to the "display".
 */
public class DisplayOperations extends BaseAction {

    /**
     * Executes the action.
     *
     * @param ctx The action execution context.
     */
    @Override
    public void execute(ActionExecutionContext ctx) throws ModelException, SCXMLExpressionException {
        logger.info("Executing action 'DisplayOperations'.");

        // Get Data
        NonComparativeAnalysisSituation as = (NonComparativeAnalysisSituation) getContext(ctx).getAnalysisSituation(); // TODO: make usable with comparative AS
        Cube cube = null;
        List<CubeElement> elements = new ArrayList<>();

        if (as.isCubeDefined()) {
            cube = BeanUtils.instantiateClass(CubeRepository.class).findById(as.getCube()).orElse(new Cube(""));
            elements = BeanUtils.instantiateClass(CubeElementRepository.class).findByCubeUri(as.getCube());
        }

        // Resolve Operations
        ResolveOperationsDataModel model = new ResolveOperationsDataModel(
                getCurrentState(),
                as,
                getContext(ctx).getLocale(),
                cube,
                elements);
        List<Operation> operations = new OperationsResolver().resolveOperations(model);

        // Send to display
        // TODO
    }

}
