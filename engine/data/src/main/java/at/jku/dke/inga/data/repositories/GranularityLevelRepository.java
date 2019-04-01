package at.jku.dke.inga.data.repositories;

import at.jku.dke.inga.data.IRIValidator;
import at.jku.dke.inga.data.QueryException;
import at.jku.dke.inga.data.configuration.GraphDbConnection;
import at.jku.dke.inga.data.models.DimensionLabel;
import at.jku.dke.inga.data.models.Label;
import at.jku.dke.inga.shared.models.DimensionQualification;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.eclipse.rdf4j.query.BindingSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Repository for querying granularity levels.
 */
@Service
public class GranularityLevelRepository extends BaseRepository {

    /**
     * Instantiates a new instance of class {@linkplain GranularityLevelRepository}.
     *
     * @param connection The GraphDB connection service class.
     */
    @Autowired
    public GranularityLevelRepository(GraphDbConnection connection) {
        super(connection);
    }

    /**
     * Returns all granularity levels for the specified cube.
     *
     * @param cubeIri The absolute IRI of the cube.
     * @return Set with all granularity level IRIs of the specified cube. The key of the pair represents the dimension, the value is the level.
     * @throws IllegalArgumentException If {@code cubeIri} is {@code null}, blank or an invalid IRI.
     * @throws QueryException           If an exception occurred while executing the query.
     */
    public Set<Pair<String, String>> getAllByCube(String cubeIri) throws QueryException {
        if (StringUtils.isBlank(cubeIri)) throw new IllegalArgumentException("cubeIri must not be null nor empty");
        if (!IRIValidator.isValidAbsoluteIRI(cubeIri))
            throw new IllegalArgumentException("cubeIri must be an absolute IRI");

        logger.debug("Querying all granularity levels of cube {}.", cubeIri);
        return connection.getQueryResult("/repo_gl/getAllByCube.sparql", s -> s.replaceAll("###CUBE###", cubeIri))
                .stream()
                .map(x -> new ImmutablePair<>(
                        x.getValue("dimension").stringValue(),
                        x.getValue("element").stringValue()
                )).collect(Collectors.toSet());
    }

    /**
     * Returns all granularity level relationships for the specified cube.
     * <p>
     * The first entry of the triple is the dimension, the second one the child and the third one the parent.
     *
     * @param cubeIri The absolute IRI of the cube.
     * @return Set with all granularity level relationships of the specified cube.
     * @throws IllegalArgumentException If {@code cubeIri} is {@code null}, blank or an invalid IRI.
     * @throws QueryException           If an exception occurred while executing the query.
     */
    public Set<Triple<String, String, String>> getAllRelationshipsByCube(String cubeIri) throws QueryException {
        if (StringUtils.isBlank(cubeIri)) throw new IllegalArgumentException("cubeIri must not be null nor empty");
        if (!IRIValidator.isValidAbsoluteIRI(cubeIri))
            throw new IllegalArgumentException("cubeIri must be an absolute IRI");

        logger.debug("Querying all granularity level relationships of cube {}.", cubeIri);
        return connection.getQueryResult("/repo_gl/getAllRelationshipsByCube.sparql", s -> s.replaceAll("###CUBE###", cubeIri))
                .stream()
                .map(x -> new ImmutableTriple<>(
                        x.getValue("dimension").stringValue(),
                        x.getValue("child").stringValue(),
                        x.getValue("parent").stringValue()
                )).collect(Collectors.toSet());
    }

    /**
     * Returns the labels of all base granularity levels of the specified cube.
     *
     * @param lang    The requested language.
     * @param cubeIri The absolute IRI of the cube.
     * @return List with base aggregate measure labels of the cube in the requested language
     * @throws IllegalArgumentException If {@code lang} or {@code cubeIri} is {@code null} or blank.
     * @throws QueryException           If an exception occurred while executing the query.
     */
    public List<DimensionLabel> getBaseLevelLabelsByLangAndCube(String lang, String cubeIri) throws QueryException {
        if (StringUtils.isBlank(lang)) throw new IllegalArgumentException("lang must not be null or empty");
        if (StringUtils.isBlank(cubeIri)) throw new IllegalArgumentException("cubeIri must not be null or empty");
        if (!IRIValidator.isValidAbsoluteIRI(cubeIri))
            throw new IllegalArgumentException("cubeIri must be an absolute IRI");

        logger.debug("Querying labels of base granularity levels of cube {} in language {}.", cubeIri, lang);

        return connection.getQueryResult(
                "/repo_gl/getBaseLabelsByLangAndCube.sparql",
                s -> s.replaceAll("###LANG###", lang).replaceAll("###CUBE###", cubeIri)
        )
                .stream()
                .map(x -> convert(lang, x))
                .collect(Collectors.toList());
    }

    /**
     * Returns the labels of all direct and transitive parents for the selected granularity level of the specified dimension.
     * <p>
     * If the dimension qualification has an invalid granularity level IRI or dimension IRI, an empty list will be returned.
     *
     * @param lang      The requested language.
     * @param dimension The dimension qualification.
     * @return List with parent level labels of the dimension in the requested language
     * @throws IllegalArgumentException If {@code lang} or {@code dimension} is {@code null} or empty.
     * @throws QueryException           If an exception occurred while executing the query.
     */
    public List<DimensionLabel> getParentLevelLabelsByLangAndDimension(String lang, DimensionQualification dimension) throws QueryException {
        if (StringUtils.isBlank(lang)) throw new IllegalArgumentException("lang must not be null or empty");
        if (dimension == null) throw new IllegalArgumentException("dimensionQualification must not be null");
        logger.debug("Querying labels of parent granularity levels in language {} for dimension {}.", lang, dimension.getDimension());

        if (!IRIValidator.isValidAbsoluteIRI(dimension.getGranularityLevel())) return Collections.emptyList();
        if (!IRIValidator.isValidAbsoluteIRI(dimension.getDimension())) return Collections.emptyList();

        return connection.getQueryResult(
                "/repo_gl/getParentLevelLabelsByLangAndDimension.sparql",
                s -> s.replaceAll("###LANG###", lang)
                        .replaceAll("###DIMENSION###", dimension.getDimension())
                        .replaceAll("###LEVEL###", dimension.getGranularityLevel())
        )
                .stream()
                .map(x -> convert(lang, x))
                .collect(Collectors.toList());
    }

    /**
     * Returns the labels of all direct and transitive children for the selected granularity level of the specified dimension.
     * <p>
     * If the dimension qualification has an invalid granularity level IRI or dimension IRI, an empty list will be returned.
     *
     * @param lang      The requested language.
     * @param dimension The dimension qualification.
     * @return List with child level labels of the dimension in the requested language
     * @throws IllegalArgumentException If {@code lang} or {@code dimension} is {@code null} or empty.
     * @throws QueryException           If an exception occurred while executing the query.
     */
    public List<DimensionLabel> getChildLevelLabelsByLangAndDimension(String lang, DimensionQualification dimension) throws QueryException {
        if (StringUtils.isBlank(lang)) throw new IllegalArgumentException("lang must not be null or empty");
        if (dimension == null) throw new IllegalArgumentException("dimensionQualification must not be null");
        logger.debug("Querying labels of child granularity levels in language {} for dimension {}.", lang, dimension.getDimension());

        if (!IRIValidator.isValidAbsoluteIRI(dimension.getGranularityLevel())) return Collections.emptyList();
        if (!IRIValidator.isValidAbsoluteIRI(dimension.getDimension())) return Collections.emptyList();

        return connection.getQueryResult(
                "/repo_gl/getChildLevelLabelsByLangAndDimension.sparql",
                s -> s.replaceAll("###LANG###", lang)
                        .replaceAll("###DIMENSION###", dimension.getDimension())
                        .replaceAll("###LEVEL###", dimension.getGranularityLevel())
        )
                .stream()
                .map(x -> convert(lang, x))
                .collect(Collectors.toList());
    }

    /**
     * Returns the labels of the dimensions of the specified cube where a roll up operation can be performed.
     * <p>
     * This is the case when the granularity level is not set to the top most granularity level.
     *
     * @param lang                    The requested language.
     * @param cubeIri                 The absolute IRI of the cube.
     * @param dimensionQualifications The dimensions qualifications of the analysis situation.
     * @return List with dimension labels in the requested language
     * @throws IllegalArgumentException If {@code lang} or {@code cubeIri} is {@code null} or blank or {@code dimensionQualifications} is {@code null}.
     * @throws QueryException           If an exception occurred while executing the query.
     */
    public List<Label> getDimensionsWhereRollUpPossible(String lang, String cubeIri, Collection<DimensionQualification> dimensionQualifications) throws QueryException {
        if (StringUtils.isBlank(lang)) throw new IllegalArgumentException("lang must not be null or empty");
        if (StringUtils.isBlank(cubeIri)) throw new IllegalArgumentException("cubeIri must not be null or empty");
        if (!IRIValidator.isValidAbsoluteIRI(cubeIri))
            throw new IllegalArgumentException("cubeIri must be an absolute IRI");
        if (dimensionQualifications == null)
            throw new IllegalArgumentException("dimensionQualifications must not be null");

        logger.debug("Querying labels of dimension of cube {} in language {} for dimensions {} where rollup is possible.", cubeIri, lang, dimensionQualifications);
        return getLabelsByLang(
                "/repo_gl/getDimensionsWhereRollUpPossible.sparql",
                lang,
                s -> s
                        .replaceAll("###CUBE###", cubeIri)
                        .replace("###LEVELS###", dimensionQualifications.stream()
                                .map(x -> '(' + convertToFullIriString(x.getGranularityLevel()) + ')')
                                .collect(Collectors.joining(" "))
                        ));
    }

    /**
     * Returns the labels of the dimensions of the specified cube where a drill down operation can be performed.
     * <p>
     * This is the case when the granularity level is not set to the base granularity level.
     *
     * @param lang                    The requested language.
     * @param cubeIri                 The absolute IRI of the cube.
     * @param dimensionQualifications The dimensions qualifications of the analysis situation.
     * @return List with dimension labels in the requested language
     * @throws IllegalArgumentException If {@code lang} or {@code cubeIri} is {@code null} or blank or {@code dimensionQualifications} is {@code null}.
     * @throws QueryException           If an exception occurred while executing the query.
     */
    public List<Label> getDimensionsWhereDrillDownPossible(String lang, String cubeIri, Collection<DimensionQualification> dimensionQualifications) throws QueryException {
        if (StringUtils.isBlank(lang)) throw new IllegalArgumentException("lang must not be null or empty");
        if (StringUtils.isBlank(cubeIri)) throw new IllegalArgumentException("cubeIri must not be null or empty");
        if (!IRIValidator.isValidAbsoluteIRI(cubeIri))
            throw new IllegalArgumentException("cubeIri must be an absolute IRI");
        if (dimensionQualifications == null)
            throw new IllegalArgumentException("dimensionQualifications must not be null");

        logger.debug("Querying labels of dimension of cube {} in language {} for dimensions {} where drill-down is possible.", cubeIri, lang, dimensionQualifications);
        return getLabelsByLang(
                "/repo_gl/getDimensionsWhereDrillDownPossible.sparql",
                lang,
                s -> s
                        .replaceAll("###CUBE###", cubeIri)
                        .replace("###LEVELS###", dimensionQualifications.stream()
                                .map(x -> '(' + convertToFullIriString(x.getGranularityLevel()) + ')')
                                .collect(Collectors.joining(" "))
                        ));
    }

    private static DimensionLabel convert(String lang, BindingSet bindingSet) {
        return new DimensionLabel(
                lang,
                bindingSet.getValue("dimension").stringValue(),
                bindingSet.getValue("dimensionLabel").stringValue(),
                bindingSet.getValue("element").stringValue(),
                bindingSet.getValue("label").stringValue(),
                bindingSet.hasBinding("description") ? bindingSet.getValue("description").stringValue() : null
        );
    }
}
