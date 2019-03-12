package at.jku.dke.inga.scxml.actions;

import at.jku.dke.inga.data.repositories.MeasureRepository;
import at.jku.dke.inga.rules.models.OperationDisplayDeterminationServiceModel;
import at.jku.dke.inga.rules.services.OperationDisplayDeterminationService;
import at.jku.dke.inga.scxml.context.ContextModel;
import at.jku.dke.inga.shared.BeanUtil;
import at.jku.dke.inga.shared.display.ListDisplay;
import at.jku.dke.inga.shared.models.NonComparativeAnalysisSituation;
import at.jku.dke.inga.shared.operations.Operation;
import org.apache.commons.scxml2.ActionExecutionContext;
import org.apache.commons.scxml2.model.ModelException;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * This action identifies operation from which the user can select a value.
 * Operations can be select cube, drop measure, ...
 */
public class DisplayOperations extends BaseAction {

    /**
     * Executes the action operations.
     *
     * @param ctx      The state chart application execution context.
     * @param ctxModel The context data.
     */
    @Override
    protected void execute(ActionExecutionContext ctx, ContextModel ctxModel) throws ModelException {
        // Get data
        Set<String> measures = Collections.emptySet();
        if (ctxModel.getAnalysisSituation() instanceof NonComparativeAnalysisSituation && ctxModel.getAnalysisSituation().isCubeDefined()) {
            measures = BeanUtil.getBean(MeasureRepository.class).findByCube(((NonComparativeAnalysisSituation) ctxModel.getAnalysisSituation()).getCube());
        }

        var model = new OperationDisplayDeterminationServiceModel(
                getCurrentState(),
                ctxModel.getAnalysisSituation(),
                ctxModel.getLocale(),
                measures
        );

        // Determine possible operations
        List<Operation> operations = new OperationDisplayDeterminationService().executeRules(model);

        // Send to display
        ctxModel.setDisplayData(new ListDisplay("selectOperation", ctxModel.getLocale(), operations));
    }

}
