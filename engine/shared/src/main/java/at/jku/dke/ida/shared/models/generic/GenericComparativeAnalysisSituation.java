package at.jku.dke.ida.shared.models.generic;

import at.jku.dke.ida.shared.models.AnalysisSituation;

import java.util.*;

/**
 * A comparative analysis situation allows to model comparison. It joins two analysis situations and relates
 * both by a score definition.
 */
public class GenericComparativeAnalysisSituation<TValue extends Comparable<? super TValue>, TDimQual extends GenericDimensionQualification<TValue>, TNonComp extends GenericNonComparativeAnalysisSituation<TValue, TDimQual>> extends AnalysisSituation {

    private TNonComp contextOfInterest; // 1. a non-comparative analysis situation as context of interest
    private TNonComp contextOfComparison;  // 2. a non-comparative analysis situation as context of comparison
    private Set<TValue> joinConditions; // 3. a non-empty set of join conditions
    private Set<TValue> scores; // 4. a non-empty set of scores
    private Set<TValue> scoreFilters; // 5. a possibly empty set of score filters

    /**
     * Instantiates a new instance of class {@linkplain GenericComparativeAnalysisSituation}.
     */
    public GenericComparativeAnalysisSituation() {
        this.contextOfInterest = null;
        this.contextOfComparison = null;
        this.joinConditions = new HashSet<>();
        this.scores = new HashSet<>();
        this.scoreFilters = new HashSet<>();
    }

    /**
     * Instantiates a new instance of class {@linkplain GenericComparativeAnalysisSituation}.
     *
     * @param contextOfInterest   The context of interest.
     * @param contextOfComparison The context of comparison.
     */
    public GenericComparativeAnalysisSituation(TNonComp contextOfInterest, TNonComp contextOfComparison) {
        this();
        this.contextOfInterest = contextOfInterest;
        this.contextOfComparison = contextOfComparison;
    }

    /**
     * Gets the context of interest.
     *
     * @return the context of interest
     */
    public TNonComp getContextOfInterest() {
        return contextOfInterest;
    }

    /**
     * Sets the context of interest.
     *
     * @param contextOfInterest the context of interest
     */
    public void setContextOfInterest(TNonComp contextOfInterest) {
        this.contextOfInterest = contextOfInterest;
    }

    /**
     * Gets the context of comparison.
     *
     * @return the context of comparison
     */
    public TNonComp getContextOfComparison() {
        return contextOfComparison;
    }

    /**
     * Sets the context of comparison.
     *
     * @param contextOfComparison the context of comparison
     */
    public void setContextOfComparison(TNonComp contextOfComparison) {
        this.contextOfComparison = contextOfComparison;
    }

    // region --- JoinConditions ---

    /**
     * Gets the join conditions as unmodifiable set.
     *
     * @return the join conditions
     */
    public Set<TValue> getJoinConditions() {
        return Collections.unmodifiableSet(joinConditions);
    }

    /**
     * Sets the join conditions.
     *
     * @param joinConditions the join conditions
     */
    public void setJoinConditions(Set<TValue> joinConditions) {
        this.joinConditions = Objects.requireNonNullElseGet(joinConditions, HashSet::new);
    }

    /**
     * Adds the specified join condition if it is not already present.
     *
     * @param cond The join condition to be added to the set.
     * @return {@code true} if this set did not already contain the specified condition
     * @see Set#add(Object)
     */
    public boolean addJoinCondition(TValue cond) {
        return joinConditions.add(cond);
    }

    /**
     * Removes the specified join condition if it is present.
     *
     * @param cond The join condition to be removed from the set, if present.
     * @return {@code true} if the set contained the specified condition
     * @see Set#remove(Object)
     */
    public boolean removeJoinCondition(TValue cond) {
        return joinConditions.remove(cond);
    }
    // endregion

    // region --- Scores ---

    /**
     * Gets the scores as unmodifiable set.
     *
     * @return the scores
     */
    public Set<TValue> getScores() {
        return Collections.unmodifiableSet(scores);
    }

    /**
     * Sets the scores.
     *
     * @param scores the scores
     */
    public void setScores(Set<TValue> scores) {
        this.scores = Objects.requireNonNullElseGet(scores, HashSet::new);
    }

    /**
     * Adds the specified score if it is not already present.
     *
     * @param score The score to be added to the set.
     * @return {@code true} if this set did not already contain the specified score
     * @see Set#add(Object)
     */
    public boolean addScore(TValue score) {
        return scores.add(score);
    }

    /**
     * Removes the specified score if it is present.
     *
     * @param score The score to be removed from the set, if present.
     * @return {@code true} if the set contained the specified score
     * @see Set#remove(Object)
     */
    public boolean removeScore(TValue score) {
        return scores.remove(score);
    }
    // endregion

    // region --- ScoreFilters ---

    /**
     * Gets score filters as unmodifiable set.
     *
     * @return the score filters
     */
    public Set<TValue> getScoreFilters() {
        return Collections.unmodifiableSet(scoreFilters);
    }

    /**
     * Sets score filters (may be empty).
     *
     * @param scoreFilters the score filters
     */
    public void setScoreFilters(Set<TValue> scoreFilters) {
        this.scoreFilters = Objects.requireNonNullElseGet(scoreFilters, HashSet::new);
    }

    /**
     * Adds the specified score filter if it is not already present.
     *
     * @param scoref The score filter to be added to the set.
     * @return {@code true} if this set did not already contain the specified score filter
     * @see Set#add(Object)
     */
    public boolean addScoreFilter(TValue scoref) {
        return scoreFilters.add(scoref);
    }

    /**
     * Removes the specified score filter if it is present.
     *
     * @param scoref The score filter to be removed from the set, if present.
     * @return {@code true} if the set contained the specified score filter
     * @see Set#remove(Object)
     */
    public boolean removeScoreFilter(TValue scoref) {
        return scoreFilters.remove(scoref);
    }
    // endregion

    @Override
    public boolean isExecutable() {
        return contextOfInterest != null && contextOfInterest.isExecutable() &&
                contextOfComparison != null && contextOfComparison.isExecutable() &&
                !joinConditions.isEmpty() &&
                !scores.isEmpty();
    }

    @Override
    public boolean isCubeDefined() {
        return contextOfInterest != null && contextOfInterest.isCubeDefined() &&
                contextOfComparison != null && contextOfComparison.isCubeDefined();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", GenericComparativeAnalysisSituation.class.getSimpleName() + "[", "]")
                .add("contextOfInterest=" + contextOfInterest)
                .add("contextOfComparison=" + contextOfComparison)
                .add("joinConditions=" + joinConditions)
                .add("scores=" + scores)
                .add("scoreFilters=" + scoreFilters)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GenericComparativeAnalysisSituation that = (GenericComparativeAnalysisSituation) o;
        return Objects.equals(contextOfInterest, that.contextOfInterest) &&
                Objects.equals(contextOfComparison, that.contextOfComparison) &&
                Objects.equals(joinConditions, that.joinConditions) &&
                Objects.equals(scores, that.scores) &&
                Objects.equals(scoreFilters, that.scoreFilters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), contextOfInterest, contextOfComparison, joinConditions, scores, scoreFilters);
    }
}
