
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SystemOfLinear {

    public static String solveSystemOfLinearEquations(String equation1, String equation2, int method) {
        // Extract coefficients and constants
        double[] coeffs1 = extractCoefficients(equation1);
        double[] coeffs2 = extractCoefficients(equation2);

        double a1 = coeffs1[0];
        double b1 = coeffs1[1];
        double c1 = coeffs1[2];

        double a2 = coeffs2[0];
        double b2 = coeffs2[1];
        double c2 = coeffs2[2];

        StringBuilder result = new StringBuilder();

        result.append("Original equations:\n");
        result.append(formatEquation(a1, b1, c1, 1));
        result.append(formatEquation(a2, b2, c2, 2));

        if (a1 / a2 == b1 / b2) {
            if (c1 / c2 == a1 / a2) {
                result.append("The equations have infinite solutions (they are the same equation).\n");
            } else {
                result.append("There is no solution (the equations are parallel).\n");
            }
            return result.toString();
        }

        if (method == 1) {
            return solveByElimination(a1, b1, c1, a2, b2, c2, result);
        } else if (method == 2) {
            return solveBySubstitution(a1, b1, c1, a2, b2, c2, result);
        } else {
            result.append("Invalid method choice.\n");
            return result.toString();
        }
    }

    public static double[] extractCoefficients(String equation) {
        Pattern pattern = Pattern.compile("([+-]?\\d*\\.?\\d*)x([+-]?\\d*\\.?\\d*)y=([+-]?\\d*\\.?\\d*)");
        Matcher matcher = pattern.matcher(equation);

        if (matcher.matches()) {
            double a = parseCoefficient(matcher.group(1));
            double b = parseCoefficient(matcher.group(2));
            double c = Double.parseDouble(matcher.group(3));
            return new double[]{a, b, c};
        } else {
            throw new IllegalArgumentException("Invalid equation format");
        }
    }

    public static double parseCoefficient(String coefficient) {
        if (coefficient.equals("") || coefficient.equals("+")) {
            return 1;
        } else if (coefficient.equals("-")) {
            return -1;
        } else {
            return Double.parseDouble(coefficient);
        }
    }

    public static String solveByElimination(double a1, double b1, double c1, double a2, double b2, double c2, StringBuilder result) {
        result.append("\nMultiplying equations to align coefficients of x:\n");

        double multiplier1 = a2;
        double multiplier2 = a1;

        double newA1 = a1 * multiplier1;
        double newB1 = b1 * multiplier1;
        double newC1 = c1 * multiplier1;

        double newA2 = a2 * multiplier2;
        double newB2 = b2 * multiplier2;
        double newC2 = c2 * multiplier2;

        result.append(formatEquation(newA1, newB1, newC1, 3));
        result.append(formatEquation(newA2, newB2, newC2, 4));

        // Eliminate x by subtracting the equations
        double elimB = newB1 - newB2;
        double elimC = newC1 - newC2;

        result.append("\nSubtracting equation (4) from equation (3):\n");
        result.append(formatEquation(newA1, newB1, newC1, 3));
        result.append(formatEquation(newA2, newB2, newC2, 4));
        result.append("----------------------------\n");
        result.append(formatNumber(elimB) + "y = " + formatNumber(elimC) + "\n");

        // Solve for y
        double y = elimC / elimB;
        result.append("y = " + formatNumber(y) + "\n");

        // Solve for x
        double x = (c1 - b1 * y) / a1;
        result.append("\nSubstituting y back into the first equation:\n");
        result.append(formatNumber(a1) + "x + " + formatNumber(b1) + "*(" + formatNumber(y) + ") = " + formatNumber(c1) + "\n");
        result.append("x = " + formatNumber(x) + "\n");

        result.append("\nSolution: (x, y) = (" + formatNumber(x) + ", " + formatNumber(y) + ")\n");
        return result.toString();
    }

    public static String solveBySubstitution(double a1, double b1, double c1, double a2, double b2, double c2, StringBuilder result) {
        result.append("\nSolving the first equation for x:\n");
        result.append(formatNumber(a1) + "x + " + formatVariable(b1, "y") + " = " + formatNumber(c1) + "\n");
        result.append("x = (" + formatNumber(c1) + " - " + formatVariable(b1, "y") + ") / " + formatNumber(a1) + "\n");

        // Substitute x into the second equation and solve for y
        double y = (c2 - a2 * (c1 / a1)) / (b2 - a2 * (b1 / a1));
        result.append("\nSubstituting x into the second equation:\n");
        result.append(formatNumber(a2) + "*(" + formatNumber(c1) + " - " + formatVariable(b1, "y") + ") / " + formatNumber(a1) + " + " + formatVariable(b2, "y") + " = " + formatNumber(c2) + "\n");
        result.append("y = " + formatNumber(y) + "\n");

        double x = (c1 - b1 * y) / a1;
        result.append("\nSubstituting y back into the expression for x:\n");
        result.append("x = (" + formatNumber(c1) + " - " + formatVariable(b1, "y") + ") / " + formatNumber(a1) + "\n");
        result.append("x = " + formatNumber(x) + "\n");

        result.append("\nSolution: (x, y) = (" + formatNumber(x) + ", " + formatNumber(y) + ")\n");
        return result.toString();
    }

    private static String formatNumber(double number) {
        if (number == (long) number) {
            return String.format("%d", (long) number);
        } else {
            return String.format("%.2f", number);
        }
    }

    private static String formatVariable(double coefficient, String variable) {
        if (coefficient == 1) {
            return variable;
        } else if (coefficient == -1) {
            return "-" + variable;
        } else {
            return formatNumber(coefficient) + variable;
        }
    }

    private static String formatEquation(double a, double b, double c, int eqNumber) {
        String aStr = (a == 1) ? "x" : (a == -1) ? "-x" : formatNumber(a) + "x";
        String bStr = (b == 1) ? "+ y" : (b == -1) ? "- y" : (b > 0) ? " + " + formatNumber(b) + "y" : " - " + formatNumber(-b) + "y";
        return aStr + bStr + " = " + formatNumber(c) + " ...(" + eqNumber + ")\n";
    }
}
