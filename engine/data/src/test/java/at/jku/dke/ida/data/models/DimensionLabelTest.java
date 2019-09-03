package at.jku.dke.ida.data.models;

import at.jku.dke.ida.data.PojoTestUtils;
import at.jku.dke.ida.data.models.labels.DimensionLabel;
import org.junit.jupiter.api.Test;

class DimensionLabelTest {

    @Test
    void testOperationAccessors() {
        PojoTestUtils.validateAccessors(DimensionLabel.class);
    }

}
