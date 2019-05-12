package at.jku.dke.ida.data.repositories;

import at.jku.dke.ida.data.IRIValidator;
import at.jku.dke.ida.data.QueryException;
import at.jku.dke.ida.data.GraphDbConnection;
import at.jku.dke.ida.data.models.Label;
import at.jku.dke.ida.data.repositories.base.BaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Repository which provides methods for queries independent of type.
 */
@Service
public class SimpleRepository extends BaseRepository {

    /**
     * Instantiates a new instance of class {@linkplain SimpleRepository}.
     *
     * @param connection The GraphDB connection service class.
     */
    @Autowired
    public SimpleRepository(GraphDbConnection connection) {
        super(connection);
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
                "/repo_simple/getLabelByLangAndIri.sparql",
                lang,
                s -> s.replaceAll("###URI###", iri));
        if (list == null || list.isEmpty()) return null;
        return list.get(0);
    }

    /**
     * Returns the labels for the uris in the specified language.
     *
     * @param lang The requested language.
     * @param iris Collection with the IRIs of which the labels are requested.
     * @return Map with labels in the requested language. The key is the IRI, the value is the Label.
     * @throws IllegalArgumentException If {@code lang} is {@code null} or blank or if {@code iris} is {@code null}.
     * @throws QueryException           If an exception occurred while executing the query (e.g. query file not found).
     */
    public Map<String, Label> getLabelsByLangAndIris(String lang, Collection<String> iris) throws QueryException {
        if (iris == null) throw new IllegalArgumentException("iris must not be null");
        return getLabelsByLang("/repo_simple/getLabelsByLangAndIris.sparql",
                lang,
                s -> s.replaceAll("###IN###", convertToFullIriString(iris)))
                .stream()
                .distinct()
                .collect(Collectors.toMap(Label::getUri, Function.identity()));
    }

    /**
     * Returns the labels for all elements in the specified language.
     *
     * @param lang The requested language.
     * @return Map with labels in the requested language.
     * @throws IllegalArgumentException If {@code lang} is {@code null} or blank.
     * @throws QueryException           If an exception occurred while executing the query (e.g. query file not found).
     */
    public List<Label> getLabelsByLang(String lang) throws QueryException {
        return getLabelsByLang("/repo_simple/getLabelsByLang.sparql", lang);
    }
}
