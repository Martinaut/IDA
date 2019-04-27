package at.jku.dke.ida.scxml.actions;

import at.jku.dke.ida.data.repositories.*;
import at.jku.dke.ida.rules.models.ValueDisplayServiceModel;
import at.jku.dke.ida.rules.services.ValueDisplayService;
import at.jku.dke.ida.scxml.interceptors.DisplayValuesInterceptor;
import at.jku.dke.ida.scxml.session.SessionContextModel;
import at.jku.dke.ida.shared.display.Display;
import at.jku.dke.ida.shared.spring.BeanUtil;
import org.apache.commons.scxml2.ActionExecutionContext;
import org.apache.commons.scxml2.model.ModelException;

/**
 * This action identifies values from which the user can select one.
 * Values can be cubes, measures, ...
 */
public class DisplayValues extends BaseAction {
    /**
     * Executes the action operations.
     *
     * @param ctx      The state chart application execution context.
     * @param ctxModel The context data.
     */
    @Override
    protected void execute(ActionExecutionContext ctx, SessionContextModel ctxModel) throws ModelException {
        // Get data
        var model = new ValueDisplayServiceModel(
                getCurrentState(),
                ctxModel.getLocale(),
                ctxModel.getAnalysisSituation(),
                ctxModel.getOperation(),
                ctxModel.getAdditionalData(),
                BeanUtil.getBean(SimpleRepository.class),
                BeanUtil.getBean(CubeRepository.class),
                BeanUtil.getBean(AggregateMeasureRepository.class),
                BeanUtil.getBean(GranularityLevelRepository.class),
                BeanUtil.getBean(LevelPredicateRepository.class),
                BeanUtil.getBean(BaseMeasurePredicateRepository.class),
                BeanUtil.getBean(AggregateMeasurePredicateRepository.class)
        );
        var interceptor = BeanUtil.getOptionalBean(DisplayValuesInterceptor.class);
        if (interceptor != null)
            model = interceptor.modifyModel(model);

        // Determine display data
        Display display = new ValueDisplayService().executeRules(model);
        if (interceptor != null)
            display = interceptor.modifyResult(display);

        // Send to display
        ctxModel.setDisplayData(display);
    }
}
