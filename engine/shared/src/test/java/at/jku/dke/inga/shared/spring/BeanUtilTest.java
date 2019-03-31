package at.jku.dke.inga.shared.spring;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {SharedSpringConfiguration.class})
@ExtendWith(SpringExtension.class)
class BeanUtilTest {
    @Test
    void testGetBean() {
        // Execute
        var result = BeanUtil.getBean(BeanUtil.class);

        // Assert
        assertNotNull(result);
    }
}
