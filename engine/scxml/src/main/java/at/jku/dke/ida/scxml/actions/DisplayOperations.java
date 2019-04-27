package at.jku.dke.ida.scxml.actions;

import at.jku.dke.ida.data.QueryException;
import at.jku.dke.ida.data.repositories.*;
import at.jku.dke.ida.rules.models.OperationDisplayServiceModel;
import at.jku.dke.ida.rules.services.OperationDisplayService;
import at.jku.dke.ida.scxml.interceptors.DisplayOperationsInterceptor;
import at.jku.dke.ida.scxml.session.SessionContextModel;
import at.jku.dke.ida.shared.display.ErrorDisplay;
import at.jku.dke.ida.shared.display.ListDisplay;
import at.jku.dke.ida.shared.models.NonComparativeAnalysisSituation;
import at.jku.dke.ida.shared.operations.Operation;
import at.jku.dke.ida.shared.spring.BeanUtil;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.commons.scxml2.ActionExecutionContext;
import org.apache.commons.scxml2.model.ModelException;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * This action identifies operations from which the user can select one.
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
    protected void execute(ActionExecutionContext ctx, SessionContextModel ctxModel) throws ModelException {
        // Get data
        Set<String> measures = Collections.emptySet();
        MutableGraph<String> granularityLevels = GraphBuilder.directed().build();
        Set<Pair<String, String>> sliceConditions = Collections.emptySet();
        Set<String> filters = Collections.emptySet();
        Set<String> bmcs = Collections.emptySet();
        try {
            if (ctxModel.getAnalysisSituation() instanceof NonComparativeAnalysisSituation && ctxModel.getAnalysisSituation().isCubeDefined()) {
                String cube = ((NonComparativeAnalysisSituation) ctxModel.getAnalysisSituation()).getCube();
                measures = BeanUtil.getBean(AggregateMeasureRepository.class).getAllByCube(cube);
                sliceConditions = BeanUtil.getBean(LevelPredicateRepository.class).getAllByCube(cube);
                filters = BeanUtil.getBean(AggregateMeasurePredicateRepository.class).getAllByCube(cube);
                bmcs = BeanUtil.getBean(BaseMeasurePredicateRepository.class).getAllByCube(cube);
                buildGranularityLevelGraph(cube, granularityLevels);
            }
        } catch (QueryException ex) {
            logger.fatal("Could not load required data for OperationDisplayServiceModel.", ex);
            ctxModel.setDisplayData(new ErrorDisplay("errorLoadData", ctxModel.getLocale()));
            return;
        }

        // Build model
        var model = new OperationDisplayServiceModel(
                getCurrentState(),
                ctxModel.getLocale(),
                ctxModel.getAnalysisSituation(),
                ctxModel.getOperation(),
                ctxModel.getAdditionalData(),
                measures,
                granularityLevels,
                sliceConditions,
                bmcs,
                filters
        );

        var interceptor = BeanUtil.getOptionalBean(DisplayOperationsInterceptor.class);
        if (interceptor != null)
            model = interceptor.modifyModel(model);

        // Determine possible operations
        Collection<Operation> operations = new OperationDisplayService().executeRules(model);
        if (interceptor != null)
            operations = interceptor.modifyResult(operations);

        // Send to display
        ctxModel.setDisplayData(new ListDisplay("selectOperation", ctxModel.getLocale(), List.copyOf(operations)));
    }

    private void buildGranularityLevelGraph(String cube, MutableGraph<String> graph) throws QueryException {
        Set<Triple<String, String, String>> gl = BeanUtil.getBean(GranularityLevelRepository.class).getAllRelationshipsByCube(cube);
        for (Triple<String, String, String> pair : gl) {
            graph.addNode(pair.getMiddle());
            graph.addNode(pair.getRight());
            graph.putEdge(pair.getMiddle(), pair.getRight());
        }
    }
}
