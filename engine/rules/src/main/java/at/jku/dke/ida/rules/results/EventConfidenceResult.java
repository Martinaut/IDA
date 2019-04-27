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

}
