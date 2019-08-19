package at.jku.dke.ida.app.ruleset.helpers;

import at.jku.dke.ida.data.QueryException;
import at.jku.dke.ida.data.models.DimensionLabel;
import at.jku.dke.ida.data.repositories.LevelRepository;
import at.jku.dke.ida.shared.models.DimensionQualification;
import at.jku.dke.ida.shared.models.NonComparativeAnalysisSituation;
import at.jku.dke.ida.shared.spring.BeanUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

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
            var baseLevels = BeanUtil.getBean(LevelRepository.class).getTopLevelLabelsByLangAndCube(lang, cube);
            for (DimensionLabel lvl : baseLevels) {
                var dq = new DimensionQualification(lvl.getDimensionUri());
                dq.setGranularityLevel(lvl.getUri());
                as.addDimensionQualification(dq);
            }
        } catch (QueryException ex) {
            LOGGER.fatal("Cannot set dimension qualifications for cube " + cube + " as there occurred an error while querying the levels.", ex);
        }
    }

    /**
     * Merges the dimension qualifications of two analysis situations following the principles of set-to-base comparison pattern.
     *
     * @param from The analysis situation from which to use the values.
     * @param to   The analysis situation to which set the values.
     */
    public static void setSetToBaseDimensions(NonComparativeAnalysisSituation from, NonComparativeAnalysisSituation to) {
        // Delete superfluous dimensions
        to.getDimensionQualifications().stream()
                .filter(x -> from.getDimensionQualification(x.getDimension()) == null)
                .forEach(to::removeDimensionQualification);

        // Merge existing
        from.getDimensionQualifications().stream()
                .filter(x -> to.getDimensionQualification(x.getDimension()) != null)
                .forEach(dq -> {
                    var toDq = to.getDimensionQualification(dq.getDimension());
                    toDq.setGranularityLevel(dq.getGranularityLevel());
                });

        // Add missing dimensions
        from.getDimensionQualifications().stream()
                .filter(x -> to.getDimensionQualification(x.getDimension()) == null)
                .forEach(dq -> {
                    try {
                        to.addDimensionQualification((DimensionQualification) dq.clone());
                    } catch (CloneNotSupportedException ex) {
                        LOGGER.fatal("Could not clone dimension qualification.", ex);
                    }
                });
    }

}
