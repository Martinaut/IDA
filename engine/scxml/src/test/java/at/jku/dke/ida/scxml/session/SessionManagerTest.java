package at.jku.dke.ida.scxml.session;

import at.jku.dke.ida.data.configuration.DataSpringConfiguration;
import at.jku.dke.ida.scxml.exceptions.SessionExpiredException;
import at.jku.dke.ida.scxml.exceptions.StateMachineInstantiationException;
import at.jku.dke.ida.shared.spring.SharedSpringConfiguration;
import org.apache.commons.scxml2.model.ModelException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {SharedSpringConfiguration.class, DataSpringConfiguration.class})
@ExtendWith(SpringExtension.class)
class SessionManagerTest {

    // region --- CREATE SESSION ---
    @Test
    void testCreateSessionWithNullSessionId() {
        Throwable ex = assertThrows(IllegalArgumentException.class, () -> SessionManager.getInstance().createSession(null, "en"));
        assertTrue(ex.getMessage().contains("sessionId"));
    }

    @Test
    void testCreateSessionWithEmptySessionId() {
        Throwable ex = assertThrows(IllegalArgumentException.class, () -> SessionManager.getInstance().createSession("", "en"));
        assertTrue(ex.getMessage().contains("sessionId"));
    }

    @Test
    void testCreateSessionWithWhitespaceSessionId() {
        Throwable ex = assertThrows(IllegalArgumentException.class, () -> SessionManager.getInstance().createSession("  ", "en"));
        assertTrue(ex.getMessage().contains("sessionId"));
    }

    @Test
    void testCreateSessionWithNullLocale() {
        Throwable ex = assertThrows(IllegalArgumentException.class, () -> SessionManager.getInstance().createSession("ABC", null));
        assertTrue(ex.getMessage().contains("locale"));
    }

    @Test
    void testCreateSessionWithEmptyLocale() {
        Throwable ex = assertThrows(IllegalArgumentException.class, () -> SessionManager.getInstance().createSession("ABC", ""));
        assertTrue(ex.getMessage().contains("locale"));
    }

    @Test
    void testCreateSessionWithWhitespaceLocale() {
        Throwable ex = assertThrows(IllegalArgumentException.class, () -> SessionManager.getInstance().createSession("ABC", "  "));
        assertTrue(ex.getMessage().contains("locale"));
    }

    @Test
    void testCreateSession() throws StateMachineInstantiationException {
        // Prepare
        final String sessionId = "testCreateSession";
        final String locale = "en";

        // Execute
        SessionManager.getInstance().createSession(sessionId, locale);
        Session s = SessionManager.getInstance().getSession(sessionId);

        // Assert
        assertNotNull(s);
        assertEquals(sessionId, s.getSessionId());
        assertEquals(locale, s.getSessionContextModel().getLocale().getLanguage());
    }
    // endregion

    // region --- GET SESSION ---
    @Test
    void testGetSession() {
        assertNull(SessionManager.getInstance().getSession("testGetSession"));
    }

    @Test
    void testGetSessionWithNullSessionId() throws StateMachineInstantiationException {
        SessionManager.getInstance().createSession("testGetSessionWithNullSessionId", "en");
        Throwable ex = assertThrows(IllegalArgumentException.class, () -> SessionManager.getInstance().getSession(null));
        assertTrue(ex.getMessage().contains("sessionId"));
    }

    @Test
    void testGetSessionWithEmptySessionId() throws StateMachineInstantiationException {
        SessionManager.getInstance().createSession("testGetSessionWithEmptySessionId", "en");
        Throwable ex = assertThrows(IllegalArgumentException.class, () -> SessionManager.getInstance().getSession(""));
        assertTrue(ex.getMessage().contains("sessionId"));
    }

    @Test
    void testGetSessionWithWhitespaceSessionId() throws StateMachineInstantiationException {
        SessionManager.getInstance().createSession("testGetSessionWithWhitespaceSessionId", "en");
        Throwable ex = assertThrows(IllegalArgumentException.class, () -> SessionManager.getInstance().getSession("  "));
        assertTrue(ex.getMessage().contains("sessionId"));
    }
    // endregion

    // region --- INITIATE ---

    @Test
    void testInitiateStateMachine() throws StateMachineInstantiationException {
        // Prepare
        final String sessionId = "testInitiateStateMachine";
        SessionManager.getInstance().createSession(sessionId, "en");

        // Execute
        SessionManager.getInstance().initiateStateMachine(sessionId);

        // Assert
    }

    @Test
    void testInitiateStateMachineWithNullSessionId() throws StateMachineInstantiationException {
        SessionManager.getInstance().createSession("testInitiateStateMachineWithNullSessionId", "en");
        Throwable ex = assertThrows(IllegalArgumentException.class, () -> SessionManager.getInstance().initiateStateMachine(null));
        assertTrue(ex.getMessage().contains("sessionId"));
    }

    @Test
    void testInitiateStateMachineWithEmptySessionId() throws StateMachineInstantiationException {
        SessionManager.getInstance().createSession("testInitiateStateMachineWithEmptySessionId", "en");
        Throwable ex = assertThrows(IllegalArgumentException.class, () -> SessionManager.getInstance().initiateStateMachine(""));
        assertTrue(ex.getMessage().contains("sessionId"));
    }

    @Test
    void testInitiateStateMachineWithWhitespaceSessionId() throws StateMachineInstantiationException {
        SessionManager.getInstance().createSession("testInitiateStateMachineWithWhitespaceSessionId", "en");
        Throwable ex = assertThrows(IllegalArgumentException.class, () -> SessionManager.getInstance().initiateStateMachine("  "));
        assertTrue(ex.getMessage().contains("sessionId"));
    }
    // endregion

    // region --- DELETE SESSION ---

    @Test
    void testDeleteSession() throws StateMachineInstantiationException {
        // Prepare
        final String sessionId = "testDeleteSession";
        SessionManager.getInstance().createSession(sessionId, "en");

        // Execute and Assert
        assertNotNull(SessionManager.getInstance().getSession(sessionId));
        SessionManager.getInstance().deleteSession(sessionId);
        assertNull(SessionManager.getInstance().getSession(sessionId));
    }

    @Test
    void testDeleteSessionWithNullSessionId() throws StateMachineInstantiationException {
        SessionManager.getInstance().createSession("testDeleteSessionWithNullSessionId", "en");
        Throwable ex = assertThrows(IllegalArgumentException.class, () -> SessionManager.getInstance().deleteSession(null));
        assertTrue(ex.getMessage().contains("sessionId"));
    }

    @Test
    void testDeleteSessionWithEmptySessionId() throws StateMachineInstantiationException {
        SessionManager.getInstance().createSession("testDeleteSessionWithEmptySessionId", "en");
        Throwable ex = assertThrows(IllegalArgumentException.class, () -> SessionManager.getInstance().deleteSession(""));
        assertTrue(ex.getMessage().contains("sessionId"));
    }

    @Test
    void testDeleteSessionWithWhitespaceSessionId() throws StateMachineInstantiationException {
        SessionManager.getInstance().createSession("testDeleteSessionWithWhitespaceSessionId", "en");
        Throwable ex = assertThrows(IllegalArgumentException.class, () -> SessionManager.getInstance().deleteSession("  "));
        assertTrue(ex.getMessage().contains("sessionId"));
    }
    // endregion

    // region --- TRIGGER ---

//    @Test
//    void testTriggerUserInput() throws StateMachineInstantiationException, SessionExpiredException, StateMachineExecutionException, InterruptedException {
//        // Prepare
//        final String sessionId = "testTriggerUserInput";
//        SessionManager.getInstance().createSession(sessionId, "en");
//        SessionManager.getInstance().initiateStateMachine(sessionId);
//
//        // Execute and Assert
//        Thread.sleep(5000);
//        SessionManager.getInstance().triggerUserInput(sessionId, "1");
//    }

    @Test
    void testTriggerUserInputWithInvalidSession() {
        // Prepare
        final String sessionId = "testTriggerUserInputWithInvalidSession";

        // Execute and Assert
        assertThrows(SessionExpiredException.class, () -> SessionManager.getInstance().triggerUserInput(sessionId, "Hello"));
    }

    @Test
    void testTriggerUserInputWithFinishedState() throws StateMachineInstantiationException, ModelException {
        // Prepare
        final String sessionId = "testTriggerUserInputWithFinishedState";
        SessionManager.getInstance().createSession(sessionId, "en");
        SessionManager.getInstance().initiateStateMachine(sessionId);
        SessionManager.getInstance().getSession(sessionId).triggerExitEvent();

        // Execute and Assert
        assertThrows(SessionExpiredException.class, () -> SessionManager.getInstance().triggerUserInput(sessionId, "1"));
    }

    @Test
    void testTriggerUserInputWithNullSessionId() throws StateMachineInstantiationException {
        SessionManager.getInstance().createSession("testTriggerUserInputWithNullSessionId", "en");
        Throwable ex = assertThrows(IllegalArgumentException.class, () -> SessionManager.getInstance().triggerUserInput(null, "Hello"));
        assertTrue(ex.getMessage().contains("sessionId"));
    }

    @Test
    void testTriggerUserInputWithEmptySessionId() throws StateMachineInstantiationException {
        SessionManager.getInstance().createSession("testTriggerUserInputWithEmptySessionId", "en");
        Throwable ex = assertThrows(IllegalArgumentException.class, () -> SessionManager.getInstance().triggerUserInput("", "Hello"));
        assertTrue(ex.getMessage().contains("sessionId"));
    }

    @Test
    void testTriggerUserInputWithWhitespaceSessionId() throws StateMachineInstantiationException {
        SessionManager.getInstance().createSession("testTriggerUserInputWithWhitespaceSessionId", "en");
        Throwable ex = assertThrows(IllegalArgumentException.class, () -> SessionManager.getInstance().triggerUserInput("  ", "Hello"));
        assertTrue(ex.getMessage().contains("sessionId"));
    }

    @Test
    void testTriggerUserInputWithNullUserInput() throws StateMachineInstantiationException {
        SessionManager.getInstance().createSession("testTriggerUserInputWithNullUserInput", "en");
        Throwable ex = assertThrows(IllegalArgumentException.class, () -> SessionManager.getInstance().triggerUserInput("testTriggerUserInputWithNullUserInput", null));
        assertTrue(ex.getMessage().contains("userInput"));
    }
    // endregion
}
