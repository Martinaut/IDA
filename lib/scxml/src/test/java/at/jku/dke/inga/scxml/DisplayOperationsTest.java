package at.jku.dke.inga.scxml;

import org.apache.commons.scxml2.model.ModelException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
public class DisplayOperationsTest {

    @Test
    void testResolveOperations() throws ModelException, XMLStreamException, IOException {
        DialogueStateMachine dsm = new DialogueStateMachine();
        dsm.getExecutor().go();
    }
}
