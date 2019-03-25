package at.jku.dke.inga.scxml.actions;

import at.jku.dke.inga.data.repositories.GranularityLevelRepository;
import at.jku.dke.inga.data.repositories.MeasureRepository;
import at.jku.dke.inga.rules.models.OperationDisplayDeterminationServiceModel;
import at.jku.dke.inga.rules.services.OperationDisplayDeterminationService;
import at.jku.dke.inga.scxml.context.ContextModel;
import at.jku.dke.inga.shared.BeanUtil;
import at.jku.dke.inga.shared.display.ListDisplay;
import at.jku.dke.inga.shared.models.NonComparativeAnalysisSituation;
import at.jku.dke.inga.shared.operations.Operation;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import com.google.common.graph.NetworkBuilder;
import org.apache.commons.lang3.tuple.Pair;
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
        MutableGraph<String> granularityLevels = GraphBuilder.directed().build();
        if (ctxModel.getAnalysisSituation() instanceof NonComparativeAnalysisSituation && ctxModel.getAnalysisSituation().isCubeDefined()) {
            String cube = ((NonComparativeAnalysisSituation) ctxModel.getAnalysisSituation()).getCube();
            measures = BeanUtil.getBean(MeasureRepository.class).findByCube(cube);
            buildGranularityLevelGraph(cube, granularityLevels);
        }

        // Build model
        var model = new OperationDisplayDeterminationServiceModel(
                getCurrentState(),
                ctxModel.getAnalysisSituation(),
                ctxModel.getLocale(),
                measures,
                granularityLevels
        );

        // Determine possible operations
        List<Operation> operations = new OperationDisplayDeterminationService().executeRules(model);

        // Send to display
        ctxModel.setDisplayData(new ListDisplay("selectOperation", ctxModel.getLocale(), operations));
    }

    private void buildGranularityLevelGraph(String cube, MutableGraph<String> graph) {
        Set<Pair<String, String>> gl = BeanUtil.getBean(GranularityLevelRepository.class).getAllGranularityLevelRelationships(cube);
        for (Pair<String, String> pair : gl) {
            graph.addNode(pair.getLeft());
            graph.addNode(pair.getRight());
            graph.putEdge(pair.getLeft(), pair.getRight());
        }
    }
}
