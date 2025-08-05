import java.util.ArrayList;
import java.util.List;
public class Quadratic {

    public static String solveQuadraticEquation(String equation) {
        StringBuilder result = new StringBuilder();

        
        int[] coefficients = extractCoefficients(equation);
        int a = coefficients[0];
        int b = coefficients[1];
        int c = coefficients[2];

        result.append((a == 1 ? "x^2" : a + "x^2") + formatTerm(b, "x") + formatTerm(c, "") + "=0\n");

        result.append(showSteps(a, b, c));

        return result.toString(); 
    }

    private static int[] extractCoefficients(String equation) {
        int a = 0, b = 0, c = 0;
        equation = equation.replace("=0", "");
        equation = equation.replace("-", "+-");
        String[] terms = equation.split("\\+");

        for (String term : terms) {
            if (term.contains("x^2")) {
                a = term.equals("x^2") ? 1 : (term.equals("-x^2") ? -1 : Integer.parseInt(term.replace("x^2", "")));
            } else if (term.contains("x")) {
                b = term.equals("x") ? 1 : (term.equals("-x") ? -1 : Integer.parseInt(term.replace("x", "")));
            } else if (!term.isEmpty()) {
                c = Integer.parseInt(term);
            }
        }

        return new int[]{a, b, c};
    }

    private static String formatTerm(int coefficient, String variable) {
        if (coefficient == 0) {
            return "";
        } else if (coefficient == 1) {
            return "+" + variable;
        } else if (coefficient == -1) {
            return "-" + variable;
        } else if (coefficient > 0) {
            return "+" + coefficient + variable;
        } else {
            return coefficient + variable;
        }
    }

    private static String showSteps(int a, int b, int c) {
        StringBuilder result = new StringBuilder();
        int ac = a * c;
        boolean found = false;
        int midTerm1 = 0, midTerm2 = 0;

        for (int i = -Math.abs(ac); i <= Math.abs(ac); i++) {
            if (i == 0) continue;
            if (ac % i == 0) {
                int j = ac / i;
                if (i + j == b) {
                    midTerm1 = i;
                    midTerm2 = j;
                    found = true;
                    break;
                }
            }
        }

        if (a == 1) {
            if (found) {
                result.append("=> x^2" + formatTerm(midTerm1, "x") + formatTerm(midTerm2, "x") + formatTerm(c, "") + "=0\n");
                result.append("=> x(x" + formatTerm(midTerm1, "") + ") + " + midTerm2 + "(x" + formatTerm(c / midTerm2, "") + ")=0\n");
                result.append("=> (x" + formatTerm(midTerm1, "") + ")(x" + formatTerm(midTerm2, "") + ")=0\n");
                result.append("if\nx" + formatTerm(midTerm1, "") + "=0\n=> x=" + (-midTerm1) + "\n");
                result.append("again\nx" + formatTerm(midTerm2, "") + "=0\n=> x=" + (-midTerm2) + "\n");
                result.append("So x=" + (-midTerm1) + ", x=" + (-midTerm2) + "\n");
            } else {
                result.append("=> Factorization not found. Solving using the quadratic formula.\n");
                result.append(solveUsingQuadraticFormula(a, b, c));
            }
        } else {
            result.append("=> Factorization not supported for a != 1. Solving using the quadratic formula.\n");
            result.append(solveUsingQuadraticFormula(a, b, c));
        }

        return result.toString();
    }

    private static String solveUsingQuadraticFormula(int a, int b, int c) {
        double d = Math.pow(b, 2) - 4 * a * c;
        StringBuilder result = new StringBuilder();

        if (d < 0) {
            result.append("The equation has no real roots\n");
            return result.toString();
        }

        double sqrtd = Math.sqrt(d);
        double x1 = (-b + sqrtd) / (2 * a);
        double x2 = (-b - sqrtd) / (2 * a);

        result.append("=> x = (-b +/- sqrt(b^2-4ac)) / 2a\n");
        result.append("=> x = (" + (-b) + " +/- sqrt(" + d + ")) / " + (2 * a) + "\n");
        result.append("=> x1 = " + x1 + "\n");
        result.append("=> x2 = " + x2 + "\n");

        return result.toString();
    }

    public static List<Double> getRoots(int a, int b, int c) {
        List<Double> roots = new ArrayList<>();
        double discriminant = Math.pow(b, 2) - 4 * a * c;

        if (discriminant >= 0) {
            double sqrtD = Math.sqrt(discriminant);
            double x1 = (-b + sqrtD) / (2 * a);
            double x2 = (-b - sqrtD) / (2 * a);

            roots.add(x1); 
            if (discriminant > 0) {
                roots.add(x2); 
            }
        }

        return roots; 
    }
}
