package at.jku.dke.inga.rules.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kie.api.KieServices;
import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.event.rule.DefaultAgendaEventListener;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.Agenda;

/**
 * Base-class for all drools-services. This class provides a KieSession.
 */
public abstract class DroolsService<TModel, TResult> implements AutoCloseable {

    protected final Logger logger;
    private final String[] agendaGroups;
    private KieSession session;

    /**
     * Instantiates a new instance of class {@linkplain DroolsService} which returns a session which executes all drools files.
     */
    protected DroolsService() {
        this.agendaGroups = null;
        this.logger = LogManager.getLogger(getClass());
    }

    /**
     * Instantiates a new instance of class {@linkplain DroolsService}.
     * Only the specified agenda groups are executed in the order they occur in the array.
     *
     * @param agendaGroups The agenda groups to execute.
     */
    protected DroolsService(String[] agendaGroups) {
        this.agendaGroups = agendaGroups;
        this.logger = LogManager.getLogger(getClass());
    }

    /**
     * Executes the rules using the given model.
     *
     * @param model The model required by the rules.
     * @return Result of the query execution
     * @throws IllegalArgumentException If the {@code model} is {@code null}.
     */
    public abstract TResult executeRules(TModel model);

    /**
     * Closes the current session.
     */
    public final void closeSession() {
        if (session == null) return;
        session.dispose();
        session = null;
    }

    /**
     * Returns the session.
     *
     * @return The kie session.
     */
    protected final KieSession getSession() {
        if (session == null)
            session = createNewSession();
        return session;
    }

    /**
     * Creates a new session.
     *
     * @return The new kie session.
     */
    protected final KieSession createNewSession() {
        logger.debug("Creating new Kie Session.");
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.getKieClasspathContainer();
        KieSession kieSession = kieContainer.newKieSession();

        if (agendaGroups != null && agendaGroups.length > 0) {
            logger.debug("Configuring agenda groups for kie session.");
            Agenda agenda = kieSession.getAgenda();
            for (String group : agendaGroups) {
                agenda.getAgendaGroup(group).setFocus();
            }
        }

        kieSession.addEventListener(new DefaultAgendaEventListener() {
            @Override
            public void afterMatchFired(AfterMatchFiredEvent event) {
                super.afterMatchFired(event);
                logger.info(event);
            }
        });

        return kieSession;
    }


    /**
     * Closes this resource, relinquishing any underlying resources.
     * This method is invoked automatically on objects managed by the
     * {@code try}-with-resources statement.
     */
    @Override
    public void close() {
        closeSession();
    }
}
