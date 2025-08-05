import java.util.ArrayList;
import java.util.List;

public class Cubic {

   
    public static String solveCubicEquation(String equation) {
        StringBuilder result = new StringBuilder();

       
        int[] coefficients = extractCoefficients(equation);
        int a = coefficients[0];
        int b = coefficients[1];
        int c = coefficients[2];
        int d = coefficients[3];

        result.append((a == 1 ? "x^3" : a + "x^3") + formatTerm(b, "x^2") + formatTerm(c, "x") + formatTerm(d, "") + "=0\n");

 
        result.append(showSteps(a, b, c, d));

        return result.toString(); 
    }

    private static int[] extractCoefficients(String equation) {
        int a = 0, b = 0, c = 0, d = 0;
        equation = equation.replace("=0", "");
        equation = equation.replace("-", "+-");
        String[] terms = equation.split("\\+");

        for (String term : terms) {
            if (term.contains("x^3")) {
                a = term.equals("x^3") ? 1 : (term.equals("-x^3") ? -1 : Integer.parseInt(term.replace("x^3", "")));
            } else if (term.contains("x^2")) {
                b = term.equals("x^2") ? 1 : (term.equals("-x^2") ? -1 : Integer.parseInt(term.replace("x^2", "")));
            } else if (term.contains("x")) {
                c = term.equals("x") ? 1 : (term.equals("-x") ? -1 : Integer.parseInt(term.replace("x", "")));
            } else if (!term.isEmpty()) {
                d = Integer.parseInt(term);
            }
        }

        return new int[]{a, b, c, d};
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

    private static String showSteps(int a, int b, int c, int d) {
        StringBuilder result = new StringBuilder();

     
        int[] possibleRoots = getPossibleRoots(d, a);
        int root = 0;
        boolean found = false;

        for (int possibleRoot : possibleRoots) {
            if (evaluatePolynomial(a, b, c, d, possibleRoot) == 0) {
                root = possibleRoot;
                found = true;
                break;
            }
        }

        if (found) {
            if (root < 0) {
                result.append("=> f(" + root + ") = 0, so (x + " + Math.abs(root) + ") is a factor.\n");
            } else {
                result.append("=> f(" + root + ") = 0, so (x - " + root + ") is a factor.\n");
            }

            int[] quotient = polynomialLongDivision(a, b, c, d, root);
            if (root < 0) {
                result.append("=> The remaining polynomial after factoring out (x + " + Math.abs(root) + ") is:\n");
            } else {
                result.append("=> The remaining polynomial after factoring out (x - " + root + ") is:\n");
            }

            result.append(quotient[0] + "x^2" + formatTerm(quotient[1], "x") + formatTerm(quotient[2], "") + "=0\n");

            String quadraticEquation = quotient[0] + "x^2" + formatTerm(quotient[1], "x") + formatTerm(quotient[2], "") + "=0";
            result.append(Quadratic.solveQuadraticEquation(quadraticEquation));
        List<Double> quadraticRoots = Quadratic.getRoots(quotient[0], quotient[1], quotient[2]);

        
        result.append("All roots of the cubic equation are:\n");
        result.append("Root 1: ").append(root).append("\n");
        for (int i = 0; i < quadraticRoots.size(); i++) {
            result.append("Root ").append(i + 2).append(": ").append(quadraticRoots.get(i)).append("\n");
        }
    } else {
        result.append("=> No rational roots found using factor theorem.\n");
    }

    return result.toString();
    }

    private static int evaluatePolynomial(int a, int b, int c, int d, int x) {
        return a * x * x * x + b * x * x + c * x + d;
    }

    private static int[] polynomialLongDivision(int a, int b, int c, int d, int root) {
        int[] quotient = new int[3];
        quotient[0] = a;
        quotient[1] = b + root * quotient[0];
        quotient[2] = c + root * quotient[1];
        return quotient;
    }

    private static int[] getFactors(int number) {
        List<Integer> factors = new ArrayList<>();
        number = Math.abs(number);
        for (int i = 1; i <= number; i++) {
            if (number % i == 0) {
                factors.add(i);
            }
        }
        return factors.stream().mapToInt(i -> i).toArray();
    }

    private static int[] getPossibleRoots(int constant, int leadingCoefficient) {
        int[] factorsOfConstant = getFactors(constant);
        int[] factorsOfLeadingCoefficient = getFactors(leadingCoefficient);

        List<Integer> possibleRootsList = new ArrayList<>();
        for (int constantFactor : factorsOfConstant) {
            for (int leadingFactor : factorsOfLeadingCoefficient) {
                possibleRootsList.add(constantFactor / leadingFactor);
                possibleRootsList.add(-constantFactor / leadingFactor);
            }
        }

        return possibleRootsList.stream().mapToInt(i -> i).toArray();
    }
}
