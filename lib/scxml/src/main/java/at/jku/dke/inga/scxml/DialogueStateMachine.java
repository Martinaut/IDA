package at.jku.dke.inga.scxml;

import org.apache.commons.scxml2.SCXMLExecutor;
import org.apache.commons.scxml2.env.SimpleDispatcher;
import org.apache.commons.scxml2.env.Tracer;
import org.apache.commons.scxml2.env.jexl.JexlEvaluator;
import org.apache.commons.scxml2.io.SCXMLReader;
import org.apache.commons.scxml2.model.Action;
import org.apache.commons.scxml2.model.CustomAction;
import org.apache.commons.scxml2.model.ModelException;
import org.apache.commons.scxml2.model.SCXML;
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
 * The state-machine for the guided instantiation dialogue.
 */
public class DialogueStateMachine {

    private static final Logger LOG = LogManager.getLogger(DialogueStateMachine.class);
    private SCXML scxml;
    private SCXMLExecutor executor;

    /**
     * Instantiates a new instance of class {@linkplain DialogueStateMachine}.
     *
     * @throws IOException        If the SCXML-File could not be loaded.
     * @throws ModelException     If an error occurred while instantiating the state chart.
     * @throws XMLStreamException If an error occurred while instantiating the state chart.
     */
    public DialogueStateMachine() throws IOException, ModelException, XMLStreamException {
        loadSCXML();
        initExecutor();
    }

    /**
     * Loads the SCXML-File and creates a new SCXML-Instance.
     *
     * @throws FileNotFoundException If the SCXML-file does not exist.
     * @throws IOException If the SCXML-File could not be loaded.
     * @throws ModelException If an error occurred while loading the state chart model.
     */
    private void loadSCXML() throws IOException, XMLStreamException, ModelException {
        LOG.info("Loading resource analysisgraph.scxml from classpath");
        URL url = DialogueStateMachine.class.getClassLoader().getResource("analysisgraph.scxml");
        if (url == null) {
            scxml = null;
            throw new FileNotFoundException("SCXML-file 'analysisgraph.scxml' not found");
        }
        scxml = SCXMLReader.read(url, new SCXMLReader.Configuration(null, null, getCustomActions()));
    }

    /**
     * Initializes the Executor.
     *
     * @throws ModelException If an error occurred while instantiating the state chart model.
     */
    private void initExecutor() throws ModelException {
        LOG.info("Initializing executor");
        Tracer tcr = new Tracer();
        executor = new SCXMLExecutor(new JexlEvaluator(), new SimpleDispatcher(), tcr);
        executor.setStateMachine(scxml);
        executor.addListener(scxml, tcr);
    }


    /**
     * Returns all custom actions determined by reflection.
     *
     * @return List with all custom actions.
     */
    private List<CustomAction> getCustomActions() {
        LOG.info("Building custom actions");
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
     * Returns the SCXML-Executor.
     *
     * @return The executor.
     */
    public SCXMLExecutor getExecutor() {
        return executor;
    }
}
