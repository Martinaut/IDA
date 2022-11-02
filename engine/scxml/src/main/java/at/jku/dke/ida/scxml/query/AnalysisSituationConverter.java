package at.jku.dke.ida.scxml.query;

import at.jku.dke.ida.shared.models.ComparativeAnalysisSituation;
import at.jku.dke.ida.shared.models.EngineAnalysisSituation;
import at.jku.dke.ida.shared.models.NonComparativeAnalysisSituation;
import org.apache.commons.io.output.WriterOutputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.UUID;

class AnalysisSituationConverter {

    private static final Logger LOG = LogManager.getLogger(AnalysisSituationConverter.class);

    private static final String NS_AG = "http://www.dke.jku.at.ac/ag#";
    private static final String NS_QBX = "http://dke.jku.at/inga/cubes#";
    private static final ValueFactory VALUE_FACTORY = SimpleValueFactory.getInstance();

    /**
     * Prevents creation of instances of this class.
     */
    private AnalysisSituationConverter() {
    }

    /**
     * Converts the given analysis situation to a json-ld representation.
     *
     * @param as            The analysis situation to be converted.
     * @return JSON-LD formatted analysis situation
     */
    static String convertAnalysisSituationToJsonLD(EngineAnalysisSituation as) {
        LOG.debug("Creating analysis situation json-ld representation.");

        ModelBuilder rdf = new ModelBuilder();
        if (as instanceof NonComparativeAnalysisSituation)
            convertNonComparativeAS("","urn:uuid:" + UUID.randomUUID(), rdf, (NonComparativeAnalysisSituation) as);
        else
            convertComparativeAS("","urn:uuid:" + UUID.randomUUID(), rdf, (ComparativeAnalysisSituation) as);
        return convertModelToJsonLD(rdf.build());
    }

    /**
     * Adds data of the non-comparative analysis situation to the model.
     *
     * @param situationName The situation name.
     * @param iri           The IRI of the analysis situation.
     * @param rdf           The RDF model.
     * @param as            The non-comparative analysis situation.
     */
    private static void convertNonComparativeAS(String situationName, String iri, ModelBuilder rdf, NonComparativeAnalysisSituation as) {
        rdf.subject(iri)
                .add(RDF.TYPE, VALUE_FACTORY.createIRI(NS_AG, "ElementaryAnalysisSituation"))
                .add(VALUE_FACTORY.createIRI(NS_QBX, "time"), VALUE_FACTORY.createLiteral(new Date()))
                .add(RDFS.LABEL, VALUE_FACTORY.createLiteral(situationName, "en"));

        // Cube
        rdf.add(VALUE_FACTORY.createIRI(NS_QBX, "hasCube"),
                VALUE_FACTORY.createIRI(as.getCube()));

        // Measures
        as.getMeasures().forEach(m ->
                rdf.add(VALUE_FACTORY.createIRI(NS_QBX, "hasAggregateMeasure"),
                        VALUE_FACTORY.createIRI(m)));

        // BaseMeasurePredicates
        as.getBaseMeasureConditions().forEach(bmc ->
                rdf.add(VALUE_FACTORY.createIRI(NS_QBX, "hasBaseMeasurePredicate"),
                        VALUE_FACTORY.createIRI(bmc)));

        // AggregateMeasurePredicates
        as.getFilterConditions().forEach(fc ->
                rdf.add(VALUE_FACTORY.createIRI(NS_QBX, "hasAggregateMeasurePredicate"),
                        VALUE_FACTORY.createIRI(fc)));

        // Dimension qualifications
        String uid = iri.substring(iri.lastIndexOf(':') + 1);
        as.getDimensionQualifications().forEach(dq -> {
            IRI dqIri = VALUE_FACTORY.createIRI(dq.getDimension() + "_spec_" + uid);

            rdf.subject(iri).add(VALUE_FACTORY.createIRI(NS_QBX, "hasDimSpec"), dqIri);
            rdf.subject(dqIri)
                    .add(VALUE_FACTORY.createIRI(NS_QBX, "granularity"), VALUE_FACTORY.createIRI(dq.getGranularityLevel()))
                    .add(VALUE_FACTORY.createIRI(NS_QBX, "dimension"), VALUE_FACTORY.createIRI(dq.getDimension()));
            if (!StringUtils.isBlank(dq.getDiceLevel()))
                rdf.add(VALUE_FACTORY.createIRI(NS_QBX, "diceLevel"),
                        VALUE_FACTORY.createIRI(dq.getDiceLevel()));
            if (!StringUtils.isBlank(dq.getDiceNode()))
                rdf.add(VALUE_FACTORY.createIRI(NS_QBX, "diceNode"),
                        VALUE_FACTORY.createIRI(dq.getDiceNode()));
            dq.getSliceConditions().forEach(sc ->
                    rdf.add(VALUE_FACTORY.createIRI(NS_QBX, "sliceCondition"),
                            VALUE_FACTORY.createIRI(sc)));
        });
    }

    /**
     * Adds data of the comparative analysis situation to the model.
     *
     * @param situationName The situation name.
     * @param iri           The IRI of the analysis situation.
     * @param rdf           The RDF model.
     * @param as            The comparative analysis situation.
     */
    private static void convertComparativeAS(String situationName, String iri, ModelBuilder rdf, ComparativeAnalysisSituation as) {
        rdf.subject(iri)
                .add(RDF.TYPE, VALUE_FACTORY.createIRI(NS_AG, "ComparisonAnalysisSituation"))
                .add(VALUE_FACTORY.createIRI(NS_QBX, "time"), VALUE_FACTORY.createLiteral(new Date()))
                .add(RDFS.LABEL, VALUE_FACTORY.createLiteral(situationName, "en"));

        // Scores
        as.getScores().forEach(s ->
                rdf.add(VALUE_FACTORY.createIRI(NS_QBX, "hasScore"),
                        VALUE_FACTORY.createIRI(s)));

        // ScoreFilters
        as.getScoreFilters().forEach(sc ->
                rdf.add(VALUE_FACTORY.createIRI(NS_QBX, "hasScoreFilter"),
                        VALUE_FACTORY.createIRI(sc)));

        // JoinConditions
        as.getJoinConditions().forEach(jc ->
                rdf.add(VALUE_FACTORY.createIRI(NS_QBX, "hasJoinCondition"),
                        VALUE_FACTORY.createIRI(jc)));

        // CoI
        String nasIri = "urn:uuid:" + UUID.randomUUID();
        convertNonComparativeAS(situationName + " CoI", nasIri, rdf, as.getContextOfInterest());
        rdf.subject(iri)
                .add(VALUE_FACTORY.createIRI(NS_QBX, "hasContextOfInterest"), VALUE_FACTORY.createIRI(nasIri));

        // CoC
        nasIri = "urn:uuid:" + UUID.randomUUID();
        convertNonComparativeAS(situationName + " CoC", nasIri, rdf, as.getContextOfComparison());
        rdf.subject(iri)
                .add(VALUE_FACTORY.createIRI(NS_QBX, "hasContextOfComparison"), VALUE_FACTORY.createIRI(nasIri));
    }

    /**
     * Converts the model into an json-ld representation.
     *
     * @param model The model.
     * @return the json-ld representation
     */
    private static String convertModelToJsonLD(Model model) {
        StringWriter writer = new StringWriter();
        OutputStream out = new WriterOutputStream(writer, Charset.defaultCharset());
        Rio.write(model, out, RDFFormat.JSONLD);

        String jsonld = writer.toString();
        try {
            writer.close();
        } catch (IOException ignored) {
        }

        LOG.debug(jsonld);
        return jsonld;
    }
}
