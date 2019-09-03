package at.jku.dke.ida.data.models;

import at.jku.dke.ida.data.PojoTestUtils;
import at.jku.dke.ida.data.models.labels.Label;
import org.junit.jupiter.api.Test;

class LabelTest {

    @Test
    void testOperationAccessors() {
        PojoTestUtils.validateAccessors(Label.class);
    }

}
