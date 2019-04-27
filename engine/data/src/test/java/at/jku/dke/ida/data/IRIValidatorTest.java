package at.jku.dke.ida.data;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

// https://www.ietf.org/rfc/rfc3987.txt
class IRIValidatorTest {

    @Test
    void testValidIRIs(){
        assertTrue(IRIValidator.isValidAbsoluteIRI("https://www.jku.at/"));
        assertTrue(IRIValidator.isValidAbsoluteIRI("http://DC.BA.ef/gh/LK/JI.html"));
        assertTrue(IRIValidator.isValidAbsoluteIRI("eXAMPLE://a/./b/../b/%63/%7bfoo%7d/ros%C3%A9"));
    }

    @Test
    void testInvalidIRIs(){
        assertFalse(IRIValidator.isValidAbsoluteIRI("all"));
        assertFalse(IRIValidator.isValidAbsoluteIRI("www.jku.at/"));
        assertFalse(IRIValidator.isValidAbsoluteIRI("www.jku.at/"));
        assertFalse(IRIValidator.isValidAbsoluteIRI(""));
        assertFalse(IRIValidator.isValidAbsoluteIRI(null));
    }
}
