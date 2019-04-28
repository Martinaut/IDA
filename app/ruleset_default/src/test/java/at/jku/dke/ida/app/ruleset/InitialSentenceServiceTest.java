package at.jku.dke.ida.app.ruleset;

import at.jku.dke.ida.app.ruleset.csp.ConstraintSatisfactionConfiguration;
import at.jku.dke.ida.data.configuration.DataSpringConfiguration;
import at.jku.dke.ida.shared.session.SessionModel;
import at.jku.dke.ida.shared.spring.SharedSpringConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@SpringBootTest(classes = {SharedSpringConfiguration.class, DataSpringConfiguration.class, ConstraintSatisfactionConfiguration.class})
@ExtendWith(SpringExtension.class)
class InitialSentenceServiceTest {
    @Test
    void test() {
        // Prepare
        SessionModel sessionModel = new SessionModel("test", "en");

        // Execute
        InitialSentenceService.fillAnalysisSituation(sessionModel, "Show me the total costs per insurant province and doctor district.");

        // Assert
        System.out.println(sessionModel.getAnalysisSituation());
        Assertions.assertTrue(sessionModel.getAnalysisSituation().isCubeDefined());
    }
}
