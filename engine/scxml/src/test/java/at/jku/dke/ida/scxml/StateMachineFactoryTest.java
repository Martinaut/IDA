package at.jku.dke.ida.scxml;

import at.jku.dke.ida.scxml.exceptions.StateMachineInstantiationException;
import org.apache.commons.scxml2.SCXMLExecutor;
import org.apache.commons.scxml2.model.Data;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StateMachineFactoryTest {
    @Test
    void testCreateWithNullSessionId() {
        Throwable ex = assertThrows(IllegalArgumentException.class, () -> StateMachineFactory.create(null));
        assertTrue(ex.getMessage().contains("sessionId"));
    }

    @Test
    void testCreateWithEmptySessionId() {
        Throwable ex = assertThrows(IllegalArgumentException.class, () -> StateMachineFactory.create(""));
        assertTrue(ex.getMessage().contains("sessionId"));
    }

    @Test
    void testCreateWithWhitespaceSessionId() {
        Throwable ex = assertThrows(IllegalArgumentException.class, () -> StateMachineFactory.create("  "));
        assertTrue(ex.getMessage().contains("sessionId"));
    }

    @Test
    void testCreate() throws StateMachineInstantiationException {
        // Prepare
        final String sessionId = "asdfjklö";

        // Execute
        SCXMLExecutor executor = StateMachineFactory.create(sessionId);
        Data data = executor.getStateMachine().getDatamodel().getData().stream()
                .filter(x -> x.getId().equals("sessionId"))
                .findFirst()
                .orElse(null);

        // Assert
        assertNotNull(executor);
        assertNotNull(data);
        assertEquals("'" + sessionId + "'", data.getExpr());
    }
}