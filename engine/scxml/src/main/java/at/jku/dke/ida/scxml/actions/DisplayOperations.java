package at.jku.dke.ida.scxml.actions;

import at.jku.dke.ida.data.QueryException;
import at.jku.dke.ida.data.repositories.*;
import at.jku.dke.ida.rules.interfaces.OperationDisplayServiceModel;
import at.jku.dke.ida.rules.models.DefaultOperationDisplayServiceModel;
import at.jku.dke.ida.rules.services.OperationDisplayService;
import at.jku.dke.ida.scxml.interceptors.DisplayOperationsInterceptor;
import at.jku.dke.ida.scxml.session.SessionContextModel;
import at.jku.dke.ida.shared.IRIConstants;
import at.jku.dke.ida.shared.display.ErrorDisplay;
import at.jku.dke.ida.shared.display.ListDisplay;
import at.jku.dke.ida.shared.models.NonComparativeAnalysisSituation;
import at.jku.dke.ida.shared.operations.Operation;
import at.jku.dke.ida.shared.spring.BeanUtil;
import com.google.common.graph.Graph;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.ImmutableGraph;
import com.google.common.graph.MutableGraph;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.commons.scxml2.ActionExecutionContext;
import org.apache.commons.scxml2.model.ModelException;

import java.util.*;
import java.util.stream.Collectors;

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
        // Build model
        OperationDisplayServiceModel model = null;
        try {
            if (ctxModel.getAnalysisSituation() instanceof NonComparativeAnalysisSituation && ctxModel.getAnalysisSituation().isCubeDefined()) {
                String cube = ((NonComparativeAnalysisSituation) ctxModel.getAnalysisSituation()).getCube();
                model = buildNonComparativeModel(ctxModel, cube);
            }
            // TODO: Non Comparative
        } catch (QueryException ex) {
            logger.fatal("Could not load required data for OperationDisplayServiceModel.", ex);
            ctxModel.setDisplayData(new ErrorDisplay("errorLoadData", ctxModel.getLocale()));
            return;
        }

        // Intercept model
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

    private OperationDisplayServiceModel buildNonComparativeModel(SessionContextModel ctxModel, String cube) throws QueryException, ModelException {
        NonComparativeAnalysisSituation as = (NonComparativeAnalysisSituation) ctxModel.getAnalysisSituation();

        // Load data
        Set<String> measures = BeanUtil.getBean(AggregateMeasureRepository.class).getAllByCube(cube, as.getMeasures());
        Set<String> bmcs = BeanUtil.getBean(BaseMeasurePredicateRepository.class).getAllByCube(cube, as.getBaseMeasureConditions());
        Set<String> filters = BeanUtil.getBean(AggregateMeasurePredicateRepository.class).getAllByCube(cube, as.getFilterConditions());
        Graph<String> granularityLevels = buildDependencyGraph(BeanUtil.getBean(LevelRepository.class).getAllRelationshipsByCube(cube));
        Set<Triple<String, String, String>> diceNodes = BeanUtil.getBean(LevelMemberRepository.class).getAllByCube(cube); // TODO

        var repo = BeanUtil.getBean(LevelPredicateRepository.class);
        Graph<String> sliceConditions = repo.getDependencyGraph(cube);
        Set<Pair<String, String>> allSCs = repo.getAllByCube(cube);

        // Build model
        return new DefaultOperationDisplayServiceModel(
                getCurrentState(),
                ctxModel,
                measures,
                filters,
                bmcs,
                granularityLevels,
                sliceConditions,
                allSCs,
                diceNodes
        );
    }

    private Graph<String> buildDependencyGraph(Set<Triple<String, String, String>> relationships) throws QueryException {
        MutableGraph<String> graph = GraphBuilder.directed().build();

        for (Triple<String, String, String> pair : relationships) {
            graph.addNode(pair.getMiddle());
            if (pair.getRight() != null) {
                graph.addNode(pair.getRight());
                graph.putEdge(pair.getMiddle(), pair.getRight());
            }
        }

        // Add top level
        graph.addNode(IRIConstants.RESOURCE_TOP_LEVEL);
        Set<String> nodes = new HashSet<>(graph.nodes());
        for (String node : nodes) {
            if (!node.equals(IRIConstants.RESOURCE_TOP_LEVEL) && graph.inDegree(node) == 0) {
                graph.putEdge(IRIConstants.RESOURCE_TOP_LEVEL, node);
            }
        }

        return ImmutableGraph.copyOf(graph);
    }
}
