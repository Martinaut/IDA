package at.jku.dke.inga.scxml;

import at.jku.dke.inga.scxml.exceptions.StateMachineInstantiationException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.scxml2.SCInstance;
import org.apache.commons.scxml2.SCXMLExecutor;
import org.apache.commons.scxml2.env.SimpleDispatcher;
import org.apache.commons.scxml2.env.Tracer;
import org.apache.commons.scxml2.env.jexl.JexlEvaluator;
import org.apache.commons.scxml2.io.SCXMLReader;
import org.apache.commons.scxml2.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The state-machine factory for the guided instantiation dialogue.
 */
public class DialogueStateMachineFactory {

    private static final Logger LOG = LogManager.getLogger(DialogueStateMachineFactory.class);
    private static SCXML scxml;

    /**
     * Create a new SCXML executor.
     *
     * @param sessionId The session id.
     * @return The created SCXML executor.
     * @throws IllegalArgumentException           If {@code sessionId} is {@code null} or empty or blank.
     * @throws StateMachineInstantiationException If an error occurred while instantiating a new state machine.
     */
    public static SCXMLExecutor create(String sessionId) throws StateMachineInstantiationException {
        if (StringUtils.isBlank(sessionId)) throw new IllegalArgumentException("sessionId must not be empty");

        LOG.info("Creating a new SCXML-Executor with session id {}.", sessionId);
        SCXMLExecutor executor = createExecutor();

        Data data = executor.getStateMachine().getDatamodel().getData().stream()
                .filter(x -> x.getId().equals("contextId"))
                .findFirst()
                .orElse(null);
        if (data == null) {
            data = new Data();
            data.setId("contextId");
            executor.getStateMachine().getDatamodel().addData(data);
        }
        data.setExpr("'" + sessionId + "'");

        return executor;
    }

    /**
     * Creates a new SCXML executor and attaches the existing state chart instance.
     *
     * @param stateChartInstance The instance.
     * @return The SCXML executor
     * @throws IllegalArgumentException           If {@code stateChartInstance} is {@code null}.
     * @throws StateMachineInstantiationException If an error occurred while instantiating a new state machine.
     */
    public static SCXMLExecutor attach(SCInstance stateChartInstance) throws StateMachineInstantiationException {
        if (stateChartInstance == null) throw new IllegalArgumentException("stateChartInstance must not be null");

        LOG.info("Creating a new SCXML-Executor for existing instance.");
        SCXMLExecutor executor = createExecutor();
        executor.attachInstance(stateChartInstance);
        return executor;
    }

    /**
     * Creates a new SCXML-executor.
     *
     * @return The executor.
     * @throws StateMachineInstantiationException If an error occurred while instantiating a new state machine executor.
     */
    private static SCXMLExecutor createExecutor() throws StateMachineInstantiationException {
        try {
            if (scxml == null)
                loadSCXML();

            Tracer tracer = new Tracer();
            SCXMLExecutor executor = new SCXMLExecutor(new JexlEvaluator(), new SimpleDispatcher(), tracer);
            executor.setStateMachine(scxml); // TODO: scxml klonen?? wegen datenmodell
            executor.addListener(scxml, tracer);
            return executor;
        } catch (FileNotFoundException ex) {
            LOG.fatal("Could not find SCXML-file.", ex);
            throw new StateMachineInstantiationException(ex.getMessage(), ex);
        } catch (IOException ex) {
            LOG.fatal("An IO error during parsing the SCXML-file.", ex);
            throw new StateMachineInstantiationException("An IO error during parsing the SCXML-file.", ex);
        } catch (XMLStreamException ex) {
            LOG.fatal("An exception processing the underlying XMLStreamReader.", ex);
            throw new StateMachineInstantiationException("An exception processing the underlying XMLStreamReader.", ex);
        } catch (ModelException ex) {
            LOG.fatal("The Commons SCXML object model is incomplete or inconsistent.", ex);
            throw new StateMachineInstantiationException("The Commons SCXML object model is incomplete or inconsistent.", ex);
        }
    }

    /**
     * Loads the SCXML-File and creates a new SCXML-Instance.
     *
     * @throws FileNotFoundException If the SCXML-file does not exist.
     * @throws IOException           If the SCXML-File could not be loaded.
     * @throws ModelException        If an error occurred while loading the state chart model.
     */
    private static void loadSCXML() throws FileNotFoundException, IOException, XMLStreamException, ModelException {
        LOG.info("Loading resource analysisgraph.scxml from classpath");
        URL url = DialogueStateMachineFactory.class.getClassLoader().getResource("analysisgraph.scxml");
        if (url == null) {
            scxml = null;
            throw new FileNotFoundException("SCXML-file 'analysisgraph.scxml' not found");
        }
        scxml = SCXMLReader.read(url, new SCXMLReader.Configuration(null, null, getCustomActions()));
    }

    /**
     * Returns all custom actions determined by reflection.
     *
     * @return List with all custom actions.
     */
    private static List<CustomAction> getCustomActions() {
        LOG.debug("Building custom actions");
        // Get classes
        Reflections reflections = new Reflections("at.jku.dke.inga.scxml.actions", new SubTypesScanner());
        Set<Class<? extends Action>> actions = reflections.getSubTypesOf(Action.class);

        // Create list and return
        return actions.stream()
                .filter(x -> !Modifier.isAbstract(x.getModifiers()) && Modifier.isPublic(x.getModifiers()))
                .map(x -> new CustomAction("http://dke.jku.at/2019/inga", x.getSimpleName(), x))
                .collect(Collectors.toList());
    }

    /**
     * Prevents creation of instances of this class.
     */
    private DialogueStateMachineFactory() {
    }
}
