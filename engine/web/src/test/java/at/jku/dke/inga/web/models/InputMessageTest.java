package at.jku.dke.inga.web.models;

import at.jku.dke.inga.web.PojoTestUtils;
import org.junit.jupiter.api.Test;

class InputMessageTest {

    @Test
    void testOperationAccessors() {
        PojoTestUtils.validateAccessors(InputMessage.class);
    }

}
