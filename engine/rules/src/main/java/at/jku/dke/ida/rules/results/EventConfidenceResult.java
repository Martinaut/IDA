package at.jku.dke.ida.rules.results;

import at.jku.dke.ida.shared.Event;

/**
 * A confidence result where the result type is {@linkplain Event}.
 */
public class EventConfidenceResult extends GenericConfidenceResult<Event> {

    /**
     * Instantiates a new instance of class {@link EventConfidenceResult} with confidence 1.
     *
     * @param evt The event.
     */
    public EventConfidenceResult(Event evt) {
        super(evt);
    }

    /**
     * Instantiates a new instance of class {@link EventConfidenceResult}.
     *
     * @param evt        The event.
     * @param confidence The confidence.
     */
    public EventConfidenceResult(Event evt, double confidence) {
        super(evt, confidence);
    }

    /**
     * Instantiates a new instance of class {@link EventConfidenceResult} with confidence 1.
     *
     * @param event The value.
     * @param term  The term.
     */
    public EventConfidenceResult(Event event, String term) {
        super(event, term);
    }

    /**
     * Instantiates a new instance of class {@link EventConfidenceResult}.
     *
     * @param event      The value.
     * @param confidence The confidence.
     * @param term       The term.
     */
    public EventConfidenceResult(Event event, double confidence, String term) {
        super(event, confidence, term);
    }
}
