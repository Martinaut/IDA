package at.jku.dke.inga.scxml.actions;

import at.jku.dke.inga.data.models.Cube;
import at.jku.dke.inga.data.models.CubeElement;
import at.jku.dke.inga.rules.models.ResolveOperationsDataModel;
import at.jku.dke.inga.rules.services.OperationsResolver;
import at.jku.dke.inga.scxml.context.ContextModel;
import at.jku.dke.inga.shared.DefaultTypes;
import at.jku.dke.inga.shared.display.ListDisplay;
import at.jku.dke.inga.shared.models.NonComparativeAnalysisSituation;
import at.jku.dke.inga.shared.operations.Operation;
import org.apache.commons.scxml2.ActionExecutionContext;
import org.apache.commons.scxml2.SCXMLExpressionException;
import org.apache.commons.scxml2.model.ModelException;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Action that determines the possible operations and sends them to the "display".
 */
@SuppressWarnings("unused")
public class DisplayOperations extends BaseAction {

    /**
     * Executes the action.
     *
     * @param ctx      The action execution context.
     * @param ctxModel The context data.
     */
    @Override
    public void execute(final ActionExecutionContext ctx, final ContextModel ctxModel) throws ModelException, SCXMLExpressionException {
        logger.info("Executing action 'DisplayOperations'.");

        // Get Data
        NonComparativeAnalysisSituation as = (NonComparativeAnalysisSituation) ctxModel.getAnalysisSituation(); // TODO: make usable with comparative AS
        Cube cube = null;
        List<CubeElement> elements = new ArrayList<>();

        if (as.isCubeDefined()) {
            cube = new Cube(as.getCube());
            elements = Arrays.asList(new CubeElement("http://www.example.org/drugs#DrugPrescriptionCube", "http://www.example.org/drugs#QuantityMeasure", DefaultTypes.TYPE_MEASURE));
        }

        // Resolve Operations
        ResolveOperationsDataModel model = new ResolveOperationsDataModel(
                getCurrentState(),
                as,
                ctxModel.getLocale(),
                cube,
                elements);
        List<Operation> operations = new OperationsResolver().resolveOperations(model);

        // Send to display
        ctxModel.setOperation(null);
        ctxModel.setDisplayData(new ListDisplay("selectOperation", ctxModel.getLocale(), operations));
    }

}
