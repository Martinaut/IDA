package at.jku.dke.ida.web.models;

import at.jku.dke.ida.web.PojoTestUtils;
import org.junit.jupiter.api.Test;

class InputMessageTest {

    @Test
    void testOperationAccessors() {
        PojoTestUtils.validateAccessors(InputMessage.class);
    }

}
