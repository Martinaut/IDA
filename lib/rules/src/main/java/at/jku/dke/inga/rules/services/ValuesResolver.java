package at.jku.dke.inga.rules.services;

import at.jku.dke.inga.data.repositories.CubeElementLabelRepository;
import at.jku.dke.inga.data.repositories.CubeLabelRepository;
import at.jku.dke.inga.rules.models.ResolveValuesDataModel;
import at.jku.dke.inga.shared.BeanUtil;
import at.jku.dke.inga.shared.display.Display;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * A service that determines the values to display depending on the analysis situation, operation and state.
 */
@Service
public class ValuesResolver extends DroolsService {

    private static final Logger LOG = LogManager.getLogger(ValuesResolver.class);

    /**
     * Instantiates a new instance of class {@linkplain ValuesResolver}.
     */
    public ValuesResolver() {
        super(new String[]{"value-determination"});
    }

    /**
     * Resolves the values that have to be displayed.
     *
     * @param dataModel The data model used by the rules engine.
     * @return The data to display.
     * @throws IllegalArgumentException If dataModel is {@code null}.
     */
    public Display resolveValues(ResolveValuesDataModel dataModel) {
        if (dataModel == null) throw new IllegalArgumentException("dataModel must not be null.");

        LOG.info("Resolving values");

        // Set data
        getSession().insert(dataModel);
        getSession().insert(dataModel.getAnalysisSituation());

        getSession().setGlobal("cubeRepository", BeanUtil.getBean(CubeLabelRepository.class));
        getSession().setGlobal("cubeElementRepository", BeanUtil.getBean(CubeElementLabelRepository.class));

        // Execute
        getSession().fireAllRules();

        // Get data
        Collection<?> objs = getSession().getObjects(obj -> obj instanceof Display);
        closeSession();

        return (Display) objs.iterator().next();
    }
}
