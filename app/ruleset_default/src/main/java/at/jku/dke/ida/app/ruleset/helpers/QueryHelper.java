package at.jku.dke.ida.app.ruleset.helpers;

import at.jku.dke.ida.data.QueryException;
import at.jku.dke.ida.data.models.DimensionLabel;
import at.jku.dke.ida.data.models.DimensionLevelLabel;
import at.jku.dke.ida.data.models.Label;
import at.jku.dke.ida.data.repositories.GranularityLevelRepository;
import at.jku.dke.ida.rules.interfaces.ValueDisplayServiceModel;
import at.jku.dke.ida.shared.models.DimensionQualification;
import at.jku.dke.ida.shared.models.NonComparativeAnalysisSituation;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.util.*;
import java.util.stream.Collectors;

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

    // region --- VALUE DISPLAY: DICE NODE ---

    /**
     * Returns all dimensions where a dice node can be added, because there are not already all possible selected.
     *
     * @param model The model.
     * @return The dimensions.
     * @throws QueryException If an error occurred while executing the query.
     */
    public static Collection<Label> getDimensionsWhereAddDicePossible(ValueDisplayServiceModel model) throws QueryException {
        if (!(model.getAnalysisSituation() instanceof NonComparativeAnalysisSituation))
            return Collections.emptyList();
        NonComparativeAnalysisSituation as = (NonComparativeAnalysisSituation) model.getAnalysisSituation();

        // Get all level predicates
        Set<Triple<String, String, String>> all = model.getLevelMemberRepository().getAllByCube(as.getCube());

        // Remove the already selected ones and where already a dice level is set
        Set<Triple<String, String, String>> selected = all.stream()
                .filter(x -> as.getDimensionQualification(x.getLeft()) != null)
                .filter(x -> as.getDimensionQualification(x.getLeft()).getDiceNode() != null)
                .filter(x -> as.getDimensionQualification(x.getLeft()).getDiceNode().equals(x.getRight()))
                .collect(Collectors.toSet());
        all.removeAll(selected);

        // Get labels of dimensions
        return model.getSimpleRepository()
                .getLabelsByLangAndIris(model.getLanguage(), all.stream().map(Triple::getLeft).collect(Collectors.toSet()))
                .values();
    }

    /**
     * Returns all levels where a dice node can be added, because there are not already all possible selected.
     *
     * @param model The model.
     * @return The dimensions.
     * @throws QueryException If an error occurred while executing the query.
     */
    public static Collection<Label> getLevelsWhereAddDicePossible(ValueDisplayServiceModel model) throws QueryException {
        if (!(model.getAnalysisSituation() instanceof NonComparativeAnalysisSituation))
            return Collections.emptyList();
        NonComparativeAnalysisSituation as = (NonComparativeAnalysisSituation) model.getAnalysisSituation();

        // Get all level predicates
        Set<Triple<String, String, String>> all = model.getLevelMemberRepository().getAllByCubeAndDimension(as.getCube(), model.getAdditionalData(Constants.ADD_DATA_DIMENSION, DimensionQualification.class).getDimension());

        // Remove the already selected ones and where already a dice level is set
        Set<Triple<String, String, String>> selected = all.stream()
                .filter(x -> as.getDimensionQualification(x.getLeft()) != null)
                .filter(x -> as.getDimensionQualification(x.getLeft()).getDiceNode() != null)
                .filter(x -> as.getDimensionQualification(x.getLeft()).getDiceLevel().equals(x.getMiddle()))
                .collect(Collectors.toSet());
        all.removeAll(selected);

        // Get labels of dimensions
        return model.getSimpleRepository()
                .getLabelsByLangAndIris(model.getLanguage(), all.stream().map(Triple::getMiddle).collect(Collectors.toSet()))
                .values();
    }

    /**
     * Calls {@link at.jku.dke.ida.data.repositories.LevelPredicateRepository#getLabelsByLangAndDimension(String, String, Collection)}
     * with the dimension set in additional data and with the already selected ones excluded.
     *
     * @param model The model.
     * @return The level predicates.
     * @throws QueryException If an error occurred while executing the query.
     */
    public static List<DimensionLevelLabel> getLevelMembersToAdd(ValueDisplayServiceModel model) throws QueryException {
        return model.getLevelMemberRepository()
                .getLabelsByLangAndLevel(
                        model.getLanguage(),
                        model.getAdditionalData(Constants.ADD_DATA_LEVEL, String.class),
                        Collections.emptySet());
    }

    /**
     * Returns all dimensions where a slice condition can be removed, because there are already slice conditions selected.
     *
     * @param model The model.
     * @return The dimensions.
     * @throws QueryException If an error occurred while executing the query.
     */
    public static Collection<Label> getDimensionsWhereDropDicePossible(ValueDisplayServiceModel model) throws QueryException {
        if (!(model.getAnalysisSituation() instanceof NonComparativeAnalysisSituation))
            return Collections.emptyList();
        NonComparativeAnalysisSituation as = (NonComparativeAnalysisSituation) model.getAnalysisSituation();

        return model.getSimpleRepository()
                .getLabelsByLangAndIris(
                        model.getLanguage(),
                        as.getDimensionQualifications().stream()
                                .filter(x -> x.getDiceNode() != null)
                                .map(DimensionQualification::getDimension)
                                .collect(Collectors.toSet())
                )
                .values();
    }
    // endregion

    // region --- VALUE DISPLAY: SLICE CONDITION ---

    /**
     * Returns all dimensions where a slice condition can be added, because there are not already all possible selected.
     *
     * @param model The model.
     * @return The dimensions.
     * @throws QueryException If an error occurred while executing the query.
     */
    public static Collection<Label> getDimensionsWhereAddPossible(ValueDisplayServiceModel model) throws QueryException {
        if (!(model.getAnalysisSituation() instanceof NonComparativeAnalysisSituation))
            return Collections.emptyList();
        NonComparativeAnalysisSituation as = (NonComparativeAnalysisSituation) model.getAnalysisSituation();

        // Get all level predicates
        Set<Pair<String, String>> all = model.getLevelPredicateRepository().getAllByCube(as.getCube());

        // Remove the already selected ones
        Set<Pair<String, String>> selected = all.stream()
                .filter(x -> as.getDimensionQualification(x.getLeft()).getSliceConditions().contains(x.getRight()))
                .collect(Collectors.toSet());
        all.removeAll(selected);

        // Get labels of dimensions
        return model.getSimpleRepository()
                .getLabelsByLangAndIris(model.getLanguage(), all.stream().map(Pair::getLeft).collect(Collectors.toSet()))
                .values();
    }

    /**
     * Returns all dimensions where a slice condition can be removed, because there are already slice conditions selected.
     *
     * @param model The model.
     * @return The dimensions.
     * @throws QueryException If an error occurred while executing the query.
     */
    public static Collection<Label> getDimensionsWhereDropPossible(ValueDisplayServiceModel model) throws QueryException {
        if (!(model.getAnalysisSituation() instanceof NonComparativeAnalysisSituation))
            return Collections.emptyList();
        NonComparativeAnalysisSituation as = (NonComparativeAnalysisSituation) model.getAnalysisSituation();

        return model.getSimpleRepository()
                .getLabelsByLangAndIris(
                        model.getLanguage(),
                        as.getDimensionQualifications().stream()
                                .filter(x -> !x.getSliceConditions().isEmpty())
                                .map(DimensionQualification::getDimension)
                                .collect(Collectors.toSet())
                )
                .values();
    }

    /**
     * Returns all dimensions where a refocus operation on slice conditions is possible.
     *
     * @param model The model.
     * @return The dimensions.
     * @throws QueryException If an error occurred while executing the query.
     */
    public static Collection<Label> getDimensionsWhereRefocusPossible(ValueDisplayServiceModel model) throws QueryException {
        Collection<Label> add = getDimensionsWhereAddPossible(model);
        Collection<Label> drop = getDimensionsWhereDropPossible(model);
        add.retainAll(drop);
        return add;
    }

    /**
     * Calls {@link at.jku.dke.ida.data.repositories.LevelPredicateRepository#getLabelsByLangAndDimension(String, String, Collection)}
     * with the dimension set in additional data and with the already selected ones excluded.
     *
     * @param model The model.
     * @return The level predicates.
     * @throws QueryException If an error occurred while executing the query.
     */
    public static List<DimensionLabel> getLevelPredicatesToAddByLangAndDimension(ValueDisplayServiceModel model) throws QueryException {
        List<String> exclusions = new ArrayList<>();
        if (model.getAnalysisSituation() instanceof NonComparativeAnalysisSituation) {
            NonComparativeAnalysisSituation as = (NonComparativeAnalysisSituation) model.getAnalysisSituation();
            exclusions.addAll(as.getDimensionQualification(model.getAdditionalData(Constants.ADD_DATA_DIMENSION, DimensionQualification.class).getDimension()).getSliceConditions());
        }

        return model.getLevelPredicateRepository()
                .getLabelsByLangAndDimension(
                        model.getLanguage(),
                        model.getAdditionalData(Constants.ADD_DATA_DIMENSION, DimensionQualification.class).getDimension(),
                        exclusions);
    }

    /**
     * Calls {@link at.jku.dke.ida.data.repositories.LevelPredicateRepository#getLabelsByLangAndIris(String, Collection)}
     * for the selected slice conditions.
     *
     * @param model The model.
     * @return The level predicates.
     * @throws QueryException If an error occurred while executing the query.
     */
    public static List<DimensionLabel> getLevelPredicatesToDropByLangAndDimension(ValueDisplayServiceModel model) throws QueryException {
        if (!(model.getAnalysisSituation() instanceof NonComparativeAnalysisSituation)) return Collections.emptyList();
        NonComparativeAnalysisSituation as = (NonComparativeAnalysisSituation) model.getAnalysisSituation();

        return model.getLevelPredicateRepository().getLabelsByLangAndIris(model.getLanguage(), as.getDimensionQualifications().stream()
                .filter(x -> !x.getSliceConditions().isEmpty())
                .flatMap(x -> x.getSliceConditions().stream())
                .collect(Collectors.toSet()));
    }
    // endregion
}
