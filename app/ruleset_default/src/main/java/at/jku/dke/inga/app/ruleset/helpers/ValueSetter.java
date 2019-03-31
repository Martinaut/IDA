package at.jku.dke.inga.app.ruleset.helpers;

import at.jku.dke.inga.data.QueryException;
import at.jku.dke.inga.data.models.DimensionLabel;
import at.jku.dke.inga.data.repositories.GranularityLevelRepository;
import at.jku.dke.inga.shared.models.DimensionQualification;
import at.jku.dke.inga.shared.models.NonComparativeAnalysisSituation;
import at.jku.dke.inga.shared.spring.BeanUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Contains some helper methods used in the rules for setting values.
 */
public final class ValueSetter {

    private static final Logger LOGGER = LogManager.getLogger(ValueSetter.class);

    /**
     * Prevents creation of instances of this class.
     */
    private ValueSetter() {
    }

    /**
     * Sets the cube of the analysis situation to the given value.
     *
     * @param lang The requested language.
     * @param as   The analysis situation.
     * @param cube The cube.
     */
    public static void setCube(String lang, NonComparativeAnalysisSituation as, String cube) {
        as.setCube(cube);

        try {
            var baseLevels = BeanUtil.getBean(GranularityLevelRepository.class).getBaseLevelLabelsByLangAndCube(lang, cube);
            for (DimensionLabel lvl : baseLevels) {
                var dq = new DimensionQualification(lvl.getDimensionUri());
                dq.setGranularityLevel(lvl.getUri());
                as.addDimensionQualification(dq);
            }
        } catch (QueryException ex) {
            LOGGER.fatal("Cannot set dimension qualifications for cube " + cube + " as there occurred an error while querying the levels.", ex);
        }
    }
}
