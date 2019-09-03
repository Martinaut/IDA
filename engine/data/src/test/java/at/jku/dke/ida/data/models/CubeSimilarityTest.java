package at.jku.dke.ida.data.models;

import at.jku.dke.ida.data.PojoTestUtils;
import at.jku.dke.ida.data.models.similarity.CubeSimilarity;
import org.junit.jupiter.api.Test;

class CubeSimilarityTest {

    @Test
    void testOperationAccessors() {
        PojoTestUtils.validateAccessors(CubeSimilarity.class);
    }

}
