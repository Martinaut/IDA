package at.jku.dke.inga.data.repositories;

import at.jku.dke.inga.data.models.Label;
import org.eclipse.rdf4j.query.BindingSet;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
}
