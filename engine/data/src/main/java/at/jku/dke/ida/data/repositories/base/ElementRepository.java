package at.jku.dke.ida.data.repositories.base;

import at.jku.dke.ida.data.GraphDbConnection;
import at.jku.dke.ida.data.IRIValidator;
import at.jku.dke.ida.data.QueryException;
import at.jku.dke.ida.data.models.Label;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.rdf4j.query.BindingSet;

import java.util.*;
import java.util.stream.Stream;

/**
 * Base repository for all repositories querying elements.
 */
public abstract class ElementRepository extends BaseRepository {

    /**
     * The IRI of the type of the elements this repository returns.
     */
    protected final String typeIri;

    /**
     * Instantiates a new instance of class {@linkplain ElementRepository}.
     *
     * @param connection The GraphDB connection service class.
     * @param typeIri    The IRI of the type.
     * @throws IllegalArgumentException If {@code typeIri} is {@code null} or empty.
     */
    protected ElementRepository(GraphDbConnection connection, String typeIri) {
        super(connection);
        if (StringUtils.isBlank(typeIri))
            throw new IllegalArgumentException("typeIri must not be null or empty");

        this.typeIri = typeIri;
    }

    // region --- ALL ---

    /**
     * Returns all elements of the type of this repository.
     *
     * @return Set with all element IRIse.
     * @throws QueryException If an exception occurred while executing the query.
     */
    public Set<String> getAll() throws QueryException {
        return getAll("/repo_base/getAll.sparql", typeIri);
    }

    /**
     * Returns all elements of the type of this repository, except those specified in the exclusions collection.
     *
     * @param exclusion The collection with IRIs to exclude from the result.
     * @return Set with all element IRIs except the specified ones
     * @throws IllegalArgumentException If {@code exclusions} is {@code null} or invalid.
     * @throws QueryException           If an exception occurred while executing the query.
     */
    public Set<String> getAll(Collection<String> exclusion) throws QueryException {
        final Collection<String> exclusions = Objects.requireNonNullElseGet(exclusion, Collections::emptyList);
        if (exclusions != null && exclusions.stream().map(IRIValidator::isValidAbsoluteIRI).anyMatch(x -> !x))
            throw new IllegalArgumentException("exclusion contains at least one invalid IRI");

        return getAll("/repo_base/getAllExcept.sparql", s -> s.replace("###TYPE###", typeIri).replace("###NOTIN###", convertToFullIriString(exclusions)));
    }
    // endregion

    // region --- LABELS BY IRI ---

    /**
     * Returns the labels of the specified elements the type of this repository.
     *
     * @param lang        The requested language.
     * @param elementIris Collection with element IRIs
     * @return List with element labels in the requested language
     * @throws IllegalArgumentException If {@code lang} is {@code null} or blank or if {@code elementIris} is {@code null} or if the IRIs are not valid.
     * @throws QueryException           If an exception occurred while executing the query.
     */
    public List<Label> getLabelsByLangAndElementIris(String lang, Collection<String> elementIris) throws QueryException {
        if (StringUtils.isBlank(lang)) throw new IllegalArgumentException("lang must not be null or empty");
        if (elementIris == null) throw new IllegalArgumentException("elementIris must not be null");
        if (elementIris.stream().map(IRIValidator::isValidAbsoluteIRI).anyMatch(x -> !x))
            throw new IllegalArgumentException("elementIris contains at least one invalid IRI");

        return getLabelsByLang("/repo_base/getLabelsByLangAndIris.sparql",
                lang,
                s -> s.replace("###IN###", convertToFullIriString(elementIris))
                        .replace("###TYPE###", typeIri));
    }

    /**
     * Returns the label for the IRI in the specified language.
     *
     * @param lang The requested language.
     * @param iri  The absolute IRI of the resource.
     * @return Label of the specified resource or {@code null} if no label for the specified language exists.
     * @throws IllegalArgumentException If {@code lang} is {@code null} or blank or if {@code iri} is an invalid IRI.
     * @throws QueryException           If an exception occurred while executing the query.
     */
    public Label getLabelByLangAndIri(String lang, String iri) throws QueryException {
        if (!IRIValidator.isValidAbsoluteIRI(iri)) throw new IllegalArgumentException("iri must be a valid IRI");
        var list = getLabelsByLang(
                "/repo_base/getLabelByLangAndIri.sparql",
                lang,
                s -> s.replace("###IRI###", iri).replace("###TYPE###", typeIri));
        if (list == null || list.isEmpty()) return null;
        return list.get(0);
    }
    // endregion

    // region --- LABELS BY CUBE ---

    /**
     * Returns the labels of all elements of the type of this repository.
     *
     * @param lang The requested language.
     * @return List with elements labels in the requested language
     * @throws IllegalArgumentException If {@code lang} is {@code null} or blank.
     * @throws QueryException           If an exception occurred while executing the query.
     */
    public List<Label> getAllLabelsByLang(String lang) throws QueryException {
        return super.getLabelsByLang("/repo_base/getLabelsByLang.sparql", lang);
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
    public List<Label> getAllLabelsByLang(String lang, Collection<String> exclusion) throws QueryException {
        if (StringUtils.isBlank(lang)) throw new IllegalArgumentException("lang must not be null or empty");
        if (exclusion == null) throw new IllegalArgumentException("excluded must not be null");
        if (exclusion.stream().map(IRIValidator::isValidAbsoluteIRI).anyMatch(x -> !x))
            throw new IllegalArgumentException("exclusion contains at least one invalid IRI");

        return getLabelsByLang("/repo_base/getLabelsByLangExcept.sparql", lang,
                s -> s.replace("###TYPE###", typeIri)
                        .replace("###LANG###", lang)
                        .replace("###NOTIN###", convertToFullIriString(exclusion)));
    }
    // endregion
}


