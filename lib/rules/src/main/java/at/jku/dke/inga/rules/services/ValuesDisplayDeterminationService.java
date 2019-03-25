package at.jku.dke.inga.rules.services;

import at.jku.dke.inga.data.repositories.CubeRepository;
import at.jku.dke.inga.data.repositories.GranularityLevelRepository;
import at.jku.dke.inga.data.repositories.MeasureRepository;
import at.jku.dke.inga.rules.models.ValuesDisplayDeterminationServiceModel;
import at.jku.dke.inga.shared.BeanUtil;
import at.jku.dke.inga.shared.display.Display;

import java.util.Collection;

/**
 * This service provides a method that returns the values from which the user can select one,
 * based on the current analysis situation and operation.
 */
public class ValuesDisplayDeterminationService extends DroolsService<ValuesDisplayDeterminationServiceModel, Display> {

    /**
     * Instantiates a new instance of class {@linkplain ValuesDisplayDeterminationService}.
     */
    public ValuesDisplayDeterminationService() {
        super(new String[]{"values-display-determination"});
    }

    /**
     * Executes the rules using the given model.
     *
     * @param model The model required by the rules.
     * @return Result of the query execution
     * @throws IllegalArgumentException If the {@code model} is {@code null}.
     */
    @Override
    public Display executeRules(ValuesDisplayDeterminationServiceModel model) {
        if (model == null) throw new IllegalArgumentException("dataModel must not be null.");

        logger.info("Determining the values to display");
        var session = getSession();

        // Add data
        session.insert(model);
        session.insert(model.getAnalysisSituation());
        session.setGlobal("cubeRepository", BeanUtil.getBean(CubeRepository.class));
        session.setGlobal("measureRepository", BeanUtil.getBean(MeasureRepository.class));
        session.setGlobal("granularityLevelRepository", BeanUtil.getBean(GranularityLevelRepository.class));

        // Execute rules
        session.fireAllRules();

        // Get result and close session
        Collection<?> objs = getSession().getObjects(obj -> obj instanceof Display);
        closeSession();

        return (Display) objs.stream().findFirst().orElse(null);
    }
}
