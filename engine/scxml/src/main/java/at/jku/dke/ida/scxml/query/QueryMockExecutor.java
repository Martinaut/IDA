package at.jku.dke.ida.scxml.query;

import at.jku.dke.ida.data.QueryException;
import at.jku.dke.ida.data.repositories.LevelMemberRepository;
import at.jku.dke.ida.shared.models.NonComparativeAnalysisSituation;
import at.jku.dke.ida.shared.models.generic.GenericDimensionQualification;
import at.jku.dke.ida.shared.session.SessionModel;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * A service that generates a random result based on the current analysis situation.
 */
@Service
public class QueryMockExecutor {
    /**
     * The url to use when the mock executor should be used.
     */
    public static final String MOCK_URL = "http://ida-mock-executor";

    private final LevelMemberRepository repo;

    /**
     * Instantiates a new instance of class {@linkplain QueryMockExecutor}.
     *
     * @param repo The level member repository.
     */
    @Autowired
    public QueryMockExecutor(LevelMemberRepository repo) {
        this.repo = repo;
    }

    /**
     * Performs a mock-request.
     *
     * @param model The session model.
     * @return The CSV with random data.
     * @throws IOException If an error occurred.
     */
    public String sendRequest(SessionModel model) throws IOException {
        if (!(model.getAnalysisSituation() instanceof NonComparativeAnalysisSituation)) return "";
        NonComparativeAnalysisSituation as = (NonComparativeAnalysisSituation) model.getAnalysisSituation();

        // Headers
        List<String> measures = new ArrayList<>(as.getMeasures());
        List<String> granularityLevels = as.getDimensionQualifications().stream().map(GenericDimensionQualification::getGranularityLevel).collect(Collectors.toList());
        List<String> headers = new ArrayList<>(granularityLevels);
        headers.addAll(measures);

        // Build csv
        Random random = new Random(System.currentTimeMillis());
        StringBuilder builder = new StringBuilder();
        builder.append(String.join(";", headers)).append(System.lineSeparator());
        for (int i = 0; i < random.nextInt(100) + 10; i++) {
            try {
                StringBuilder inner = new StringBuilder();
                for (String gl : granularityLevels) {
                    List<Triple<String, String, String>> list = new ArrayList<>(repo.getAllByCubeAndLevel(as.getCube(), gl));
                    if (!list.isEmpty())
                        inner.append(list.get(random.nextInt(list.size())).getRight()).append(';');
                    else
                        inner.append(';');
                }
                for (int j = 0; j < measures.size(); j++) {
                    inner.append(random.nextInt()).append(';');
                }
                inner.deleteCharAt(inner.lastIndexOf(";"));
                builder.append(inner.toString()).append(System.lineSeparator());
            } catch (QueryException | IllegalArgumentException ex) {
                throw new IOException(ex);
            }
        }
        builder.deleteCharAt(builder.lastIndexOf(System.lineSeparator()));
        return builder.toString();
    }
}
