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

    private final String[] agendaGroups;
    private KieSession session;

    /**
     * The logger of the current drools service.
     */
    protected final Logger logger;

    /**
     * Instantiates a new instance of class {@linkplain DroolsService}.
     * Only the specified agenda groups are executed in the order they occur in the array.
     *
     * @param agendaGroups The agenda groups to execute.
     */
    public DroolsService(String[] agendaGroups) {
        this.agendaGroups = agendaGroups;
        this.logger = LogManager.getLogger(getClass());
    }

    // region --- Execute ---

    /**
     * Executes the rules using the given model.
     *
     * @param model The model required by the rules.
     * @return Result of the query execution
     * @throws IllegalArgumentException If the {@code model} is {@code null}.
     */
    public TResult executeRules(TModel model) {
        if (model == null) throw new IllegalArgumentException("model must not be null");
        return execute(createNewSession(), model);
    }

    /**
     * Executes the rules using the given model.
     *
     * @param session The new kie session.
     * @param model   The model required by the rules. It is guaranteed, that the model is not {@code null}.
     * @return Result of the query execution
     */
    protected abstract TResult execute(KieSession session, TModel model);
    // endregion

    // region --- Session ---

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
     * Closes the current session.
     */
    public final void closeSession() {
        if (session == null) return;
        logger.debug("Closing session");
        session.dispose();
        session = null;
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
    // endregion

    @Override
    public void close() {
        closeSession();
    }
}
