package at.jku.dke.inga.rules.helpers;

import at.jku.dke.inga.data.models.DimensionLabel;
import at.jku.dke.inga.data.repositories.GranularityLevelRepository;
import at.jku.dke.inga.shared.BeanUtil;
import at.jku.dke.inga.shared.models.DimensionQualification;
import at.jku.dke.inga.shared.models.NonComparativeAnalysisSituation;

/**
 * Contains some helper methods used in the rules for setting values.
 */
public final class ValueSetter {
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

        var baseLevels = BeanUtil.getBean(GranularityLevelRepository.class).findBaseLevelsByCubeAndLang(cube, lang);
        for (DimensionLabel lvl : baseLevels){
            var dq = new DimensionQualification(lvl.getDimensionUri());
            dq.setGranularityLevel(lvl.getUri());
            as.addDimensionQualification(dq);
        }
    }
}
