package at.jku.dke.inga.app.ruleset;

import at.jku.dke.inga.data.configuration.DataSpringConfiguration;
import at.jku.dke.inga.shared.session.SessionModel;
import at.jku.dke.inga.shared.spring.SharedSpringConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@SpringBootTest(classes = {SharedSpringConfiguration.class, DataSpringConfiguration.class})
@ExtendWith(SpringExtension.class)
class InitialSentenceServiceTest {
    @Test
    void test() {
        // Prepare
        SessionModel sessionModel = new SessionModel("test", "en");

        // Execute
        InitialSentenceService.fillAnalysisSituation(sessionModel, "Show me the total costs per insurant and doctor district.");

        // Assert
        System.out.println(sessionModel.getAnalysisSituation());
        Assertions.assertTrue(sessionModel.getAnalysisSituation().isCubeDefined());
    }
}
