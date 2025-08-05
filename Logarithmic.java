public class Logarithmic {

    public static String solveLogarithmicExpression(String expression) {
        StringBuilder result = new StringBuilder();
        result.append("Given expression: ").append(expression).append("\n");

        
        String[] terms = expression.split("(?=[+-])");
        double finalResult = 0.0;

        for (String term : terms) {
            term = term.trim();

            boolean isNegative = term.startsWith("-");
            if (isNegative) {
                term = term.substring(1).trim(); 
            } else if (term.startsWith("+")) {
                term = term.substring(1).trim(); 
            }

            if (term.startsWith("log")) {
                StringBuilder termResult = new StringBuilder();
                solveLog(term, termResult);
                result.append(termResult);

                try {
                    String[] lines = termResult.toString().split("\n");
                    String lastLine = lines[lines.length - 1];
                    double termValue = Double.parseDouble(lastLine.replace("=>", "").trim());

                    // Apply the sign
                    finalResult += isNegative ? -termValue : termValue;
                } catch (NumberFormatException e) {
                    result.append("Error processing term: ").append(term).append("\n");
                }
            } else {
                result.append("Unsupported term format: ").append(term).append("\n");
            }
        }

        result.append("\nFinal result:\n");
        result.append("=> ").append(finalResult).append("\n");

        return result.toString();
    }

   public static void solveLog(String expression, StringBuilder result) {
    int startIndex = expression.indexOf('(');
    String baseStr = expression.substring(3, startIndex).trim();
    double base;

    if (baseStr.isEmpty()) {
        base = 10; 
    } else if (baseStr.contains("_")) {
        baseStr = baseStr.substring(baseStr.indexOf('_') + 1);
        base = Double.parseDouble(baseStr);
    } else {
        base = 10; 
    }

   
    if (base <= 0) {
        result.append("Error: Logarithmic base must be positive.\n");
        return;
    }
    if (base == 1) {
        result.append("Error: Logarithmic base cannot be 1.\n");
        return;
    }

    String argument = expression.substring(startIndex + 1, expression.indexOf(')')).trim();
    result.append("Processing term: ").append(expression).append("\n");

    try {
        if (argument.contains("*")) {
            ProductRule(argument, base, result);
        } else if (argument.contains("/")) {
            DivisionRule(argument, base, result);
        } else {
            double argumentValue = Double.parseDouble(argument);

            if (argumentValue <= 0) {
                result.append("Error: Logarithmic argument must be positive.\n");
                return;
            }

            double exponent = Math.log(argumentValue) / Math.log(base);
            if (exponent == (int) exponent) {
                int integerExponent = (int) exponent;

                result.append("Step 1: Rewrite the argument as an exponential form\n");
                result.append("=> log_" + base + "(" + argumentValue + ") = log_" + base + "(" + base + "^" + integerExponent + ")\n");

                result.append("Step 2: Apply the power rule (log_b(M^r) = r * log_b(M))\n");
                result.append("=> " + integerExponent + " * log_" + base + "(" + base + ")\n");

                result.append("Step 3: Simplify using the identity log_b(b) = 1\n");
                result.append("=> " + integerExponent + " * 1\n");

                result.append("Step 4: Final answer\n");
                result.append("=> " + integerExponent + "\n");
            } else {
                double resultValue = Math.log(argumentValue) / Math.log(base);
                result.append("Direct result: log_" + base + "(" + argumentValue + ") = " + resultValue + "\n");
            }
        }
    } catch (NumberFormatException e) {
        result.append("Unsupported argument format.\n");
    }
}


    private static void ProductRule(String argument, double base, StringBuilder result) {
        result.append("Step 1: Identify the factors for the product rule\n");
        String[] factors = argument.split("\\*");
        double factor1 = Double.parseDouble(factors[0].trim());
        double factor2 = Double.parseDouble(factors[1].trim());
        result.append("=> log_" + base + "(" + argument + ") = log_" + base + "(" + factor1 + ") + log_" + base + "(" + factor2 + ")\n");

        double logFactor1 = calculateLog(factor1, base, result, "factor1");
        double logFactor2 = calculateLog(factor2, base, result, "factor2");

        result.append("\nStep 4: Sum the results\n");
        result.append(String.format("=> log_" + base + "(" + factor1 + ") + log_" + base + "(" + factor2 + ") = %.4f + %.4f%n", logFactor1, logFactor2));
        double finalResult = logFactor1 + logFactor2;

        result.append("\nStep 5: Final answer\n");
        result.append(String.format("=> %.4f%n", finalResult));
    }

    private static void DivisionRule(String argument, double base, StringBuilder result) {
        result.append("Step 1: Identify the numerator and denominator for the division rule\n");
        String[] terms = argument.split("/");
        double numerator = Double.parseDouble(terms[0].trim());
        double denominator = Double.parseDouble(terms[1].trim());

        if (denominator == 0) {
            result.append("Error: Denominator cannot be zero.\n");
            return;
        }

        result.append("=> log_" + base + "(" + argument + ") = log_" + base + "(" + numerator + ") - log_" + base + "(" + denominator + ")\n");

        double logNumerator = calculateLog(numerator, base, result, "numerator");
        double logDenominator = calculateLog(denominator, base, result, "denominator");

        result.append("\nStep 4: Subtract the results\n");
        result.append(String.format("=> log_" + base + "(" + numerator + ") - log_" + base + "(" + denominator + ") = %.4f - %.4f%n", logNumerator, logDenominator));
        double finalResult = logNumerator - logDenominator;

        result.append("\nStep 5: Final answer\n");
        result.append(String.format("=> %.4f%n", finalResult));
    }

    private static double calculateLog(double value, double base, StringBuilder result, String termName) {
        result.append("\nStep 2: Calculate log_" + base + "(" + value + ")\n");
        if (isPowerOfBase(value, base)) {
            int exponent = (int) (Math.log(value) / Math.log(base));
            result.append("=> Rewrite as log_" + base + "(" + base + "^" + exponent + ")\n");
            result.append("=> Apply power rule: " + exponent + " * log_" + base + "(" + base + ")\n");
            result.append("=> Simplify using log_" + base + "(" + base + ") = 1\n");
            result.append("=> " + exponent + "\n");
            return exponent;
        } else {
            double logValue = Math.log(value) / Math.log(base);
            result.append(String.format("=> Direct calculation: log_" + base + "(" + value + ") = %.4f%n", logValue));
            return logValue;
        }
    }

    private static boolean isPowerOfBase(double number, double base) {
        double exponent = Math.log(number) / Math.log(base);
        return exponent == (int) exponent;
    }

}
