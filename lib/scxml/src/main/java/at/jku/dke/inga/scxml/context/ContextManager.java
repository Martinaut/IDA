package at.jku.dke.inga.scxml.context;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * This class holds all current active contexts.
 * A context is identified by an ID.
 */
public final class ContextManager {

    private static Map<String, ContextModel> contextMap = new HashMap<>();

    /**
     * Creates a new context model.
     *
     * @param id     The context identifier (should be equal to the session id).
     * @param locale The locale.
     * @throws IllegalArgumentException If the locale is invalid or if the session id is {@code null} or empty or blank.
     */
    public static void createNewContext(String id, String locale) {
        if (StringUtils.isBlank(id))
            throw new IllegalArgumentException("sessionId must not be empty");
        if (locale == null)
            throw new IllegalArgumentException("locale must not be null");
        if (!locale.equals("en") && !locale.equals("de"))
            throw new IllegalArgumentException("locale must be 'en' or 'de'");

        contextMap.put(id, new ContextModel(locale));
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
