package at.jku.dke.ida.data.repositories;

import at.jku.dke.ida.data.GraphDbConnection;
import at.jku.dke.ida.data.IRIValidator;
import at.jku.dke.ida.data.QueryException;
import at.jku.dke.ida.data.models.labels.ComparativeLabel;
import at.jku.dke.ida.data.repositories.base.SimpleCubeElementRepository;
import at.jku.dke.ida.shared.IRIConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.eclipse.rdf4j.query.BindingSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Repository for querying join condition predicates.
 */
@Service
public class JoinConditionPredicateRepository extends SimpleCubeElementRepository {

    /**
     * Instantiates a new instance of class {@linkplain JoinConditionPredicateRepository}.
     *
     * @param connection The GraphDB connection service class.
     */
    @Autowired
    public JoinConditionPredicateRepository(GraphDbConnection connection) {
        super(connection, IRIConstants.TYPE_JOIN_CONDITION_PREDICATE, "repo_joinpred", "join condition predicates");
    }

    private List<ComparativeLabel> mapAllResultToLabel(String lang, Stream<BindingSet> stream) {
        return stream
                .map(x -> RepositoryHelpers.convertToComparativeLabel(lang, typeIri, x))
                .collect(Collectors.toList());
    }

    /**
     * Returns the labels of all elements of the type of this repository.
     *
     * @param lang The requested language.
     * @return List with elements labels in the requested language
     * @throws IllegalArgumentException If {@code lang} is {@code null} or blank.
     * @throws QueryException           If an exception occurred while executing the query.
     */
    @Override
    public List<ComparativeLabel> getAllLabelsByLang(String lang) throws QueryException {
        if (StringUtils.isBlank(lang)) throw new IllegalArgumentException("lang must not be null or empty");

        logger.debug("Querying labels of language {} for levels.", lang);

        return mapAllResultToLabel(lang, connection.getQueryResult(
                "/" + queryFolder + "/getAllLabelsByLang.sparql",
                s -> s.replaceAll("###LANG###", lang)
        ).stream());
    }

    /**
     * Returns the labels of all elements of the type of this repository,
     * except those specified in the exclusions collection.
     *
     * @param lang      The requested language.
     * @param exclusion The collection with IRIs to exclude from the result.
     * @return List with elements labels of the cube in the requested language except the specified ones
     * @throws IllegalArgumentException If {@code lang} or {@code exclusion} is {@code null} or blank.
     * @throws QueryException           If an exception occurred while executing the query.
     */
    @Override
    public List<ComparativeLabel> getAllLabelsByLang(String lang, Collection<String> exclusion) throws QueryException {
        if (StringUtils.isBlank(lang)) throw new IllegalArgumentException("lang must not be null or empty");
        if (exclusion == null) throw new IllegalArgumentException("excluded must not be null");
        if (exclusion.stream().map(IRIValidator::isValidAbsoluteIRI).anyMatch(x -> !x))
            throw new IllegalArgumentException("exclusion contains at least one invalid IRI");

        logger.debug("Querying labels of language {} for levels with exclusions.", lang);

        return mapAllResultToLabel(lang, connection.getQueryResult(
                "/" + queryFolder + "/getAllLabelsByLangExcept.sparql",
                s -> s.replaceAll("###LANG###", lang).replace("###NOTIN###", convertToFullIriString(exclusion))
        ).stream());
    }

    /**
     * Returns the level, dimension and pattern part of the join condition predicate.
     *
     * @param jcIRI The IRI of the join condition.
     * @return Triple-Set with dimension, level and part
     * @throws IllegalArgumentException If {@code cubeIri} is {@code null}, blank or an invalid IRI.
     * @throws QueryException           If an exception occurred while executing the query.
     */
    public Set<Triple<String, String, String>> getLevelAndDimensionForPredicate(String jcIRI) throws QueryException {
        if (StringUtils.isBlank(jcIRI)) throw new IllegalArgumentException("cubeIri must not be null nor empty");
        if (!IRIValidator.isValidAbsoluteIRI(jcIRI))
            throw new IllegalArgumentException("cubeIri must be an absolute IRI");

        logger.debug("Querying dimension and level of predicate {}.", jcIRI);
        return connection.getQueryResult(
                "/" + queryFolder + "/getDimAndLevelForPredicate.sparql",
                s -> s.replace("###PREDICATE###", '(' + convertToFullIriString(jcIRI) + ')'))
                .stream()
                .map(x -> new ImmutableTriple<String, String, String>(
                        x.getBinding("dimension").getValue().stringValue(),
                        x.getBinding("level").getValue().stringValue(),
                        x.getBinding("part").getValue().stringValue()
                )).collect(Collectors.toSet());
    }
}
