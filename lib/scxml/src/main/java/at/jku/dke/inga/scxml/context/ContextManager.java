package at.jku.dke.inga.scxml.context;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * This class holds all current active contexts.
 * A context is identified by an ID.
 */
public final class ContextManager {

    private static Map<String, ContextModel> contextMap = new HashMap<>();

    /**
     * Creates a new context model and returns the context ID.
     *
     * @return The ID of the new context.
     */
    public static String createNewContext() {
        String id = UUID.randomUUID().toString();
        contextMap.put(id, new ContextModel());
        return id;
    }

    /**
     * Returns the context model for the given id.
     *
     * @param id The context id.
     * @return The context model or {@code null} if there exists no content with the specified id.
     */
    public static ContextModel getContext(String id) {
        if (id == null) return null;
        if (!contextMap.containsKey(id)) return null;
        return contextMap.get(id);
    }

    /**
     * Deletes the context with the given id.
     *
     * @param id The context id.
     */
    public static void deleteContext(String id) {
        if (id == null) return;
        if (!contextMap.containsKey(id)) return;
        contextMap.remove(id);
    }

    /**
     * Prevents generating a instance of this class.
     */
    private ContextManager() {
    }
}
