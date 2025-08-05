import java.util.*;
import java.util.regex.*;
import javax.swing.JTextArea;

public class Rational {

    public static int lcm(int a, int b) {
        return (a * b) / gcd(a, b);
    }

    public static int gcd(int a, int b) {
        if (b == 0) {
            return a;
        }
        return gcd(b, a % b);
    }

    public static List<String> extractFractions(String equation) {
        Pattern fractionPattern = Pattern.compile("([\\+\\-]?[^=\\+\\-]+)/(\\d+)");
        Matcher matcher = fractionPattern.matcher(equation);

        List<String> fractions = new ArrayList<>();
        while (matcher.find()) {
            fractions.add(matcher.group());
        }
        return fractions;
    }

    public static int[] extractDenominators(List<String> fractions) {
    int[] denominators = new int[fractions.size()];
    for (int i = 0; i < fractions.size(); i++) {
        String fraction = fractions.get(i);
        String[] parts = fraction.split("/");
        int denominator = Integer.parseInt(parts[1].trim());
        if (denominator == 0) { 
            throw new ArithmeticException("Denominator cannot be zero in a rational equation: ");
        }
        denominators[i] = denominator;
    }
    return denominators;
}


    public static String multiplyByLCD(String equation, int lcd) {
        Pattern fractionPattern = Pattern.compile("([\\+\\-]?[^=\\+\\-]+)/(\\d+)");
        Matcher matcher = fractionPattern.matcher(equation);

        StringBuffer modifiedEquation = new StringBuffer();
        while (matcher.find()) {
            String fraction = matcher.group();
            String[] parts = fraction.split("/");
            String numerator = parts[0].trim();
            int denominator = Integer.parseInt(parts[1].trim());

            int factor = lcd / denominator;
            String multipliedTerm = numerator + "*" + factor; 
            matcher.appendReplacement(modifiedEquation, multipliedTerm);
        }
        matcher.appendTail(modifiedEquation);

        String expandedEquation = evaluateMultiplications(modifiedEquation.toString());
        return expandedEquation;
    }

    public static String evaluateMultiplications(String equation) {
        Pattern multiplicationPattern = Pattern.compile("([\\+\\-]?[0-9]*x?)\\*([0-9]+)");
        Matcher matcher = multiplicationPattern.matcher(equation);

        StringBuffer simplifiedEquation = new StringBuffer();
        while (matcher.find()) {
            String term = matcher.group(1); 
            String factorStr = matcher.group(2); 

            int factor = Integer.parseInt(factorStr);

            if (term.contains("x")) {
                int coefficient = term.equals("x") || term.equals("+x") ? 1
                                  : term.equals("-x") ? -1
                                  : Integer.parseInt(term.replace("x", ""));
                String simplifiedTerm = (coefficient * factor) + "x"; 
                matcher.appendReplacement(simplifiedEquation, simplifiedTerm);
            } else if (!term.isEmpty()) {
                int constant = Integer.parseInt(term);
                String result = String.valueOf(constant * factor);
                matcher.appendReplacement(simplifiedEquation, result);
            }
        }
        matcher.appendTail(simplifiedEquation);

        return simplifiedEquation.toString();
    }

    public static String simplifyExpression(String expression) {

    expression = expression.replaceAll("\\s+", ""); 

    expression = expression.replaceAll("--", "+");
    expression = expression.replaceAll("\\+\\-", "-");
    expression = expression.replaceAll("\\-\\+", "-");
   

    expression = expression.replaceAll("\\*1", "");

    Pattern pattern = Pattern.compile("\\(([^)]+)\\)\\*(\\d+)");  // Match (expression)*number
    Matcher matcher = pattern.matcher(expression);
    StringBuffer result = new StringBuffer();

    while (matcher.find()) {
        String innerExpression = matcher.group(1);  
        int multiplier = Integer.parseInt(matcher.group(2));

        StringBuilder expandedExpression = new StringBuilder();
        String[] terms = innerExpression.split("(?=[+-])"); // Split, preserving signs
        for (String term : terms) {
            term = term.trim(); 
            if (term.isEmpty()) continue;

            if (term.matches("[+-]?[a-zA-Z]+")) { // Variable term like x or +x
                expandedExpression.append(multiplier).append(term.replaceAll("[+-]?$", ""));
            } else if (term.matches("[+-]?\\d+")) { // Constant term like +2 or -2
                int constant = Integer.parseInt(term) * multiplier;
                expandedExpression.append((constant >= 0 ? "+" : "")).append(constant);
            }
        }


        matcher.appendReplacement(result, expandedExpression.toString());
    }
    matcher.appendTail(result);  

    String finalResult = result.toString();
    

    return finalResult;
}
public static String printRationalEquation(String equation) {
    Pattern fractionPattern = Pattern.compile("([^=]+)=([^=]+)"); 
    Matcher matcher = fractionPattern.matcher(equation);

    if (matcher.find()) {
        String leftSide = matcher.group(1).trim();  
        String rightSide = matcher.group(2).trim(); 

        // Extract numerator and denominator from each side
        String[] leftFraction = leftSide.split("/");
        String[] rightFraction = rightSide.split("/");

        String leftNumerator = leftFraction[0].trim();
        String leftDenominator = leftFraction.length > 1 ? leftFraction[1].trim() : "1"; // Default to 1 if no denominator

        String rightNumerator = rightFraction[0].trim();
        String rightDenominator = rightFraction.length > 1 ? rightFraction[1].trim() : "1"; // Default to 1 if no denominator

        // Find the maximum width of the numerators and denominators for alignment
        int maxLeftWidth = Math.max(leftNumerator.length(), leftDenominator.length());
        int maxRightWidth = Math.max(rightNumerator.length(), rightDenominator.length());

        // Adjust spacing dynamically for proper center alignment
        String leftNumeratorAligned = centerAlign(leftNumerator, maxLeftWidth);
        String leftDenominatorAligned = centerAlign(leftDenominator, maxLeftWidth);

        String rightNumeratorAligned = centerAlign(rightNumerator, maxRightWidth);
        String rightDenominatorAligned = centerAlign(rightDenominator, maxRightWidth);

        // Format the fractions with proper alignment
        return String.format(
            " %s       %s\n" +
            "-------   =  ------\n" +
            " %s       %s",
            leftNumeratorAligned, rightNumeratorAligned,
            leftDenominatorAligned, rightDenominatorAligned
        );
    } else {
        return "Invalid rational equation format.";
    }
}


private static String centerAlign(String text, int width) {
    int padding = (width - text.length()) / 2;
    String paddingSpaces = " ".repeat(padding);
    return paddingSpaces + text + paddingSpaces + (text.length() % 2 != width % 2 ? " " : "");
}



   public static String solveRationalEquation(String equation, JTextArea outputArea) {
    StringBuilder output = new StringBuilder();

    try {
        // Extract fractions
        List<String> fractions = extractFractions(equation);

        // Extract denominators and calculate LCM
        int[] denominators = extractDenominators(fractions);
        int lcm = denominators[0];
        for (int denom : denominators) {
            lcm = lcm(lcm, denom);
        }

        // Append formatted rational equation
        output.append(printRationalEquation(equation)).append("\n");
        output.append("LCM of " + Arrays.toString(denominators) + " is " + lcm + "\n");

        // Simplify equation by multiplying with LCM
        output.append("Multiplying with LCM..\n");
        String simplifiedEquation = multiplyByLCD(equation, lcm);
        output.append(simplifiedEquation).append("\n");

        output.append("Simplifying..\n");
        String expandedEquation = simplifyExpression(simplifiedEquation);
        output.append("=>" + expandedEquation).append("\n");

   
        Linear.solveLinearEquation(expandedEquation, outputArea);
        output.append(outputArea.getText()); 

    } catch (ArithmeticException e) {
        // Handle zero denominator case
        output.append("Error: ").append(e.getMessage()).append("\n");
    } catch (Exception e) {
        // Handle other unexpected errors
        output.append("An unexpected error occurred: ").append(e.getMessage()).append("\n");
    }

    return output.toString();
}
}