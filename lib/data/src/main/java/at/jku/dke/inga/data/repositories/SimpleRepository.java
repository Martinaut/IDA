package at.jku.dke.inga.data.repositories;

import at.jku.dke.inga.data.models.Label;
import com.github.jsonldjava.core.RDFDataset;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.query.BindingSet;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.*;
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
     * @param graphDbHelper A helper class for accessing the graph db.
     */
    protected SimpleRepository(GraphDbHelper graphDbHelper) {
        super(graphDbHelper);
    }

    /**
     * Returns the label for the uri in the specified language.
     *
     * @param uri  The URI.
     * @param lang The language.
     * @return The label or {@code null}.
     */
    public Label findLabelByUriAndLang(String uri, String lang) {
        try {
            List<BindingSet> result = graphDbHelper.getQueryResult(
                    "/queries/getLabel.sparql",
                    s -> s.replaceAll("###LANG###", lang).replaceAll("###URI###", uri));
            Optional<BindingSet> single = result.stream().findFirst();
            return single.map(bindings -> new Label(
                    uri,
                    lang,
                    bindings.getValue("label").stringValue(),
                    bindings.hasBinding("description") ?
                            bindings.getValue("description").stringValue() :
                            null
            )).orElse(null);
        } catch (QueryException ex) {
            return null;
        }
    }

    /**
     * Returns the labels for the uris in the specified language.
     *
     * @param uris The set of URIs.
     * @param lang The language.
     * @return The labels (the key is the URI).
     */
    public Map<String, Label> findLabelsByUriAndLang(Set<String> uris, String lang) {
        try {
            List<BindingSet> result = graphDbHelper.getQueryResult(
                    "/queries/getLabels.sparql",
                    s -> s.replaceAll("###LANG###", lang)
                            .replaceAll("###IN###", uris.stream()
                                    .filter(x -> x.contains("://")) // TODO: improve IRI validation
                                    .map(x -> '<' + x + '>')
                                    .collect(Collectors.joining(", "))));
            return result.stream().map(bindings -> new Label(
                    bindings.getValue("element").stringValue(),
                    lang,
                    bindings.getValue("label").stringValue(),
                    bindings.hasBinding("description") ?
                            bindings.getValue("description").stringValue() :
                            null
            )).collect(Collectors.toMap(Label::getUri, Function.identity()));
        } catch (QueryException ex) {
            return null;
        }
    }
}
