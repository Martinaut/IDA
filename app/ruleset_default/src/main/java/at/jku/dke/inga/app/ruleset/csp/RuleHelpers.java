package at.jku.dke.inga.app.ruleset.csp;

import at.jku.dke.inga.app.ruleset.csp.domain.AnalysisSituationElement;

public final class RuleHelpers {
    /**
     * Prevents creation of instances of this class.
     */
    private RuleHelpers() {
    }

    public static boolean allInSameCube(String cube, AnalysisSituationElement elem) {
        if (elem == null) return true;
        if (elem.getElements().isEmpty()) return true;

        return elem.getElements().stream()
                .allMatch(x -> x.getCube().equals(cube));
    }
}
