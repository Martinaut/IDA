package at.jku.dke.ida.scxml.query;

import at.jku.dke.ida.data.IRIValidator;
import at.jku.dke.ida.data.QueryException;
import at.jku.dke.ida.data.models.Label;
import at.jku.dke.ida.data.repositories.SimpleRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A service that replaces all URIs within a CSV with their corresponding labels.
 */
@Service
public class CSVPrettifier {

    private static final Logger LOG = LogManager.getLogger(CSVPrettifier.class);
    private final SimpleRepository repo;

    /**
     * Instantiates a new instance of class {@linkplain CSVPrettifier}.
     *
     * @param repo The label repository.
     */
    @Autowired
    public CSVPrettifier(SimpleRepository repo) {
        this.repo = repo;
    }

    /**
     * Replaces all URIs with labels, if labels exist.
     *
     * @param language The language.
     * @param csv      The raw CSV.
     * @return Prettified CSV
     */
    public String prettify(String language, String csv) {
        LOG.info("Prettifying CSV");
        try {
            Iterable<CSVRecord> records = CSVFormat.RFC4180.withDelimiter(';').parse(new StringReader(csv));

            // Load labels for items
            Set<String> items = new HashSet<>();
            for (CSVRecord record : records) {
                for (int i = 0; i < record.size(); i++) {
                    if (IRIValidator.isValidAbsoluteIRI(record.get(i)))
                        items.add(record.get(i));
                }
            }
            Map<String, Label> labels = repo.getLabelsByLangAndIris(language, items);

            // Replace
            String tmp = csv;
            for (Label lbl : labels.values()) {
                tmp = tmp.replaceAll(lbl.getUri(), '"' + lbl.getLabel() + '"');
            }
            return tmp;
        } catch (IOException ex) {
            LOG.error("Could not parse CSV.", ex);
            return null;
        } catch (QueryException ex) {
            LOG.error("Could not load labels from GraphDB.", ex);
            return csv;
        }
    }
}
