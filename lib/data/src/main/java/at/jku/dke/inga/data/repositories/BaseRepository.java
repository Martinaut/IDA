package at.jku.dke.inga.data.repositories;

import at.jku.dke.inga.data.models.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Base class for repositories with some helper methods.
 */
public abstract class BaseRepository {

    /**
     * A helper class for accessing the graph db.
     */
    protected final GraphDbHelper graphDbHelper;

    /**
     * Instantiates a new instance of class {@linkplain BaseRepository}.
     *
     * @param graphDbHelper A helper class for accessing the graph db.
     */
    protected BaseRepository(GraphDbHelper graphDbHelper) {
        this.graphDbHelper = graphDbHelper;
    }

    /**
     * Returns all labels returned by the query.
     *
     * @param lang      The requested language.
     * @param queryFile The path to the query file.
     * @return List with found labels of the language
     * @see #findLabelsByLang(String, String, Function)
     */
    protected List<Label> findLabelsByLang(String lang, String queryFile) {
        return findLabelsByLang(lang, queryFile, Function.identity());
    }

    /**
     * Returns all labels returned by the query.
     * <p>
     * Following bindings have to be returned by the query:
     * <ul>
     * <li>element</li>
     * <li>label</li>
     * <li>description (optional)</li>
     * </ul>
     * </p>
     *
     * @param lang                             The requested language.
     * @param queryFile                        The path to the query file.
     * @param additionalQueryStringManipulator Additional query string manipulator to manipulate the query text before executing
     * @return List with found labels of the language
     */
    protected List<Label> findLabelsByLang(String lang, String queryFile, Function<String, String> additionalQueryStringManipulator) {
        try {
            var result = graphDbHelper.getQueryResult(
                    queryFile,
                    s -> additionalQueryStringManipulator.apply(s.replaceAll("###LANG###", lang)));
            return result.stream().map(x ->
                    new Label(
                            x.getValue("element").stringValue(),
                            lang,
                            x.getValue("label").stringValue(),
                            x.hasBinding("description") ? x.getValue("description").stringValue() : null
                    )
            ).collect(Collectors.toList());
        } catch (QueryException ex) {
            return Collections.emptyList();
        }
    }

    /**
     * Returns all resources of the specified type.
     * <p>
     * Following binding has to be returned by the query: element
     * </p>
     *
     * @param type The requested type (full uri).
     * @param queryFile                        The path to the query file.
     * @param additionalQueryStringManipulator Additional query string manipulator to manipulate the query text before executing
     * @return A set with all resource-URIs of the specified type.
     */
    protected Set<String> getAll(String type, String queryFile, Function<String, String> additionalQueryStringManipulator) {
        try {
            var result = graphDbHelper.getQueryResult(
                    queryFile,
                    s -> additionalQueryStringManipulator.apply(s.replace("###TYPE###", type)));
            return result.stream().map(x -> x.getValue("element").stringValue()).collect(Collectors.toSet());
        } catch (QueryException ex) {
            return Collections.emptySet();
        }
    }

    /**
     * Returns all resources of the specified type.
     *
     * @param type The requested type (full uri).
     * @return A set with all resource-URIs of the specified type.
     */
    protected Set<String> getAll(String type) {
        return getAll(type, "/queries/getAll.sparql", Function.identity());
    }
}
