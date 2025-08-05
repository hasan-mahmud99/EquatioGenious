import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;

public class Linear {

    public static void solveLinearEquation(String equation, JTextArea outputArea) {
        
        String[] sides = equation.split("=");
        String leftSide = sides[0].trim();
        String rightSide = sides[1].trim();

      
        int[] leftCoefficients = extractCoefficients(leftSide);
        int[] rightCoefficients = extractCoefficients(rightSide);

 
        int combinedXCoefficient = leftCoefficients[0] - rightCoefficients[0];
        int combinedConstant = rightCoefficients[1] - leftCoefficients[1];

        
        StringBuilder output = new StringBuilder();
        output.append(equation).append("\n");

        output.append(formatTerm(leftCoefficients[0], "x"))
              .append(leftCoefficients[1] >= 0 ? "+" : "").append(leftCoefficients[1])
              .append("=")
              .append(formatTerm(rightCoefficients[0], "x"))
              .append(rightCoefficients[1] >= 0 ? "+" : "").append(rightCoefficients[1])
              .append("\n");

      
        int rightX = rightCoefficients[0] != 0 ? -rightCoefficients[0] : 0;
        int leftConst = leftCoefficients[1] != 0 ? -leftCoefficients[1] : 0;
        
        output.append(formatTerm(leftCoefficients[0], "x"))
              .append(rightX != 0 ? (rightX > 0 ? "+" : "") + formatTerm(rightX, "x") : "")
              .append("=")
              .append(rightCoefficients[1])
              .append(leftConst != 0 ? (leftConst > 0 ? "+" : "") + leftConst : "")
              .append("\n");

        output.append(formatTerm(combinedXCoefficient, "x"))
              .append("=")
              .append(combinedConstant)
              .append("\n");

        
        if (combinedXCoefficient != 0) {
            double x = (double) combinedConstant / combinedXCoefficient;
            output.append("x=").append(x).append("\n");
        } else {
            if (combinedConstant == 0) {
                output.append("Infinite solutions (all x values satisfy the equation).\n");
            } else {
                output.append("No solution (inconsistent equation).\n");
            }
        }

        // Display output in JTextArea
        SwingUtilities.invokeLater(() -> outputArea.append(output.toString()));


        
    }

    private static int[] extractCoefficients(String side) {
        int xCoefficient = 0;
        int constant = 0;

        Pattern pattern = Pattern.compile("([+-]?\\d*x)|([+-]?\\d+)");
        Matcher matcher = pattern.matcher(side);

        while (matcher.find()) {
            String term = matcher.group();
            if (term.contains("x")) {
                if (term.equals("x")) {
                    xCoefficient += 1;
                } else if (term.equals("-x")) {
                    xCoefficient -= 1;
                } else {
                    xCoefficient += Integer.parseInt(term.replace("x", ""));
                }
            } else {
                constant += Integer.parseInt(term);
            }
        }

        return new int[] { xCoefficient, constant };
    }

    private static String formatTerm(int coefficient, String variable) {
        if (coefficient == 1) {
            return variable;
        } else if (coefficient == -1) {
            return "-" + variable;
        } else {
            return coefficient + variable;
        }
    }
}
