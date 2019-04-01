package at.jku.dke.inga.app.ruleset.helpers;

import at.jku.dke.inga.data.QueryException;
import at.jku.dke.inga.data.models.DimensionLabel;
import at.jku.dke.inga.data.repositories.GranularityLevelRepository;
import at.jku.dke.inga.rules.models.ValueDisplayServiceModel;
import at.jku.dke.inga.shared.models.DimensionQualification;

import java.util.List;

/**
 * Contains some helper methods to execute queries.
 */
public final class QueryHelper {
    /**
     * Prevents creation of instances of this class.
     */
    private QueryHelper() {
    }

    // region --- VALUE DISPLAY: GRANULARITY LEVEL ---
    /**
     * Calls {@link GranularityLevelRepository#getParentLevelLabelsByLangAndDimension(String, DimensionQualification)}
     * with the dimension set in additional data.
     *
     * @param model The model.
     * @return The levels.
     * @throws QueryException If an error occurred while executing the query.
     */
    public static List<DimensionLabel> getParentLevelLabelsByLangAndDimension(ValueDisplayServiceModel model) throws QueryException {
        return model.getGranularityLevelRepository()
                .getParentLevelLabelsByLangAndDimension(model.getLanguage(), model.getAdditionalData(Constants.ADD_DATA_DIMENSION, DimensionQualification.class));
    }

    /**
     * Calls {@link GranularityLevelRepository#getChildLevelLabelsByLangAndDimension(String, DimensionQualification)}
     * with the dimension set in additional data.
     *
     * @param model The model.
     * @return The levels.
     * @throws QueryException If an error occurred while executing the query.
     */
    public static List<DimensionLabel> getChildLevelLabelsByLangAndDimension(ValueDisplayServiceModel model) throws QueryException {
        return model.getGranularityLevelRepository()
                .getChildLevelLabelsByLangAndDimension(model.getLanguage(), model.getAdditionalData(Constants.ADD_DATA_DIMENSION, DimensionQualification.class));
    }
    // endregion
}
