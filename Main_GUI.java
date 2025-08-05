import java.awt.*;
import javax.swing.*;

public class Main_GUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main_GUI().createAndShowGUI());
    }

    private JTextArea equationInputField;
    private JTextArea outputArea;

    private void createAndShowGUI() {
        JFrame frame = new JFrame("EquatioGenius");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 600);

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(70, 130, 180)); 
        JLabel titleLabel = new JLabel("EquatioGenius", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        frame.add(titlePanel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel equationInputLabel = new JLabel("Enter Equation/Expression:");
        equationInputLabel.setFont(new Font("Serif", Font.PLAIN, 16));

        equationInputField = new JTextArea(3, 20); 
        equationInputField.setFont(new Font("Monospaced", Font.PLAIN, 14));
        equationInputField.setLineWrap(true);
        equationInputField.setWrapStyleWord(true);
        JScrollPane inputScrollPane = new JScrollPane(equationInputField);

        // gbc.gridx = 0;
        // gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0.1; 
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(equationInputLabel, gbc);

        gbc.gridy = 1;
        mainPanel.add(inputScrollPane, gbc);

        
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        outputArea.setBackground(new Color(245, 245, 245));
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        JScrollPane outputScrollPane = new JScrollPane(outputArea);

        gbc.gridy = 2;
        gbc.weighty = 0.7; 
        mainPanel.add(outputScrollPane, gbc);

       
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints buttonGbc = new GridBagConstraints();


        JButton solveButton = new JButton("Solve Equation");
        styleButton(solveButton);

        JButton resetButton = new JButton("Reset");
        styleButton(resetButton);

        buttonGbc.gridx = 0;
        buttonGbc.gridy = 0;
        buttonGbc.weightx = 0.5;
        buttonGbc.insets = new Insets(0, 80, 0, 0); 
        buttonGbc.anchor = GridBagConstraints.WEST; 
        buttonPanel.add(solveButton, buttonGbc);

        buttonGbc.gridx = 1;
        buttonGbc.weightx = 0.5;
        buttonGbc.insets = new Insets(0, 0, 0, 100);
        buttonGbc.anchor = GridBagConstraints.EAST; 
        buttonPanel.add(resetButton, buttonGbc);

        gbc.gridy = 3;
        gbc.weighty = 0.1; 
        mainPanel.add(buttonPanel, gbc);

        frame.add(mainPanel, BorderLayout.CENTER);

        solveButton.addActionListener(e -> solveEquation());
        resetButton.addActionListener(e -> resetFields());

        frame.setVisible(true);
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Serif", Font.BOLD, 16));
        button.setBackground(new Color(70, 130, 180)); 
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        button.setPreferredSize(new Dimension(150, 40)); 
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        
    }

    public void solveEquation() {
        String input = equationInputField.getText().trim();
        String[] equations = input.split("\n"); 

        if (equations.length == 0 || equations[0].isEmpty()) {
            outputArea.setText("Please enter at least one equation.");
            return;
        }

        String equation1 = equations[0].trim(); 
        String equation2 = equations.length > 1 ? equations[1].trim() : ""; 

        String equationType1 = detectEquationType(equation1);
        String equationType2 = detectEquationType(equation2);

        StringBuilder output = new StringBuilder();

        if (equationType1.equals("linear") && equationType2.equals("linear")) {
            output.append("Solving system of linear equations...\n");

            String methodChoice = JOptionPane.showInputDialog(
                    "Choose method to solve system of linear equations:\n1. Elimination\n2. Substitution");

            if (methodChoice != null) {
                try {
                    int method = Integer.parseInt(methodChoice.trim());
                    output.append(SystemOfLinear.solveSystemOfLinearEquations(equation1, equation2, method));
                } catch (NumberFormatException e) {
                    output.append("Invalid method choice. Please enter 1 or 2.");
                }
            } else {
                output.append("Solving method selection canceled.\n");
            }
        } else if (equationType1.equals("quadratic") && equation2.isEmpty()) {
            output.append("Solving quadratic equation...\n");
            Quadratic q1 = new Quadratic();
            output.append(q1.solveQuadraticEquation(equation1));
        } else if (equationType1.equals("linear") && equation2.isEmpty()) {
            output.append("Solving linear equation...\n");
            Linear.solveLinearEquation(equation1, outputArea);
        } else if (equationType1.equals("cubic") && equation2.isEmpty()) {
            output.append("Solving cubic equation...\n");
            output.append(Cubic.solveCubicEquation(equation1));
        } else if (equationType1.equals("logarithmic") && equation2.isEmpty()) {
            output.append("Solving logarithmic expression...\n");
            output.append(Logarithmic.solveLogarithmicExpression(equation1));
        } else if (equationType1.equals("rational") && equation2.isEmpty()) {
            output.append("Solving rational equation...\n");
            output.append(Rational.solveRationalEquation(equation1, outputArea));
        } else {
            output.append("Unsupported equation type.\n");
        }

        
        outputArea.setText(output.toString());
    }

    private void resetFields() {
        equationInputField.setText("");
        outputArea.setText(""); 
    }

    private static String detectEquationType(String equation) {
        if (equation.contains("log")) {
            return "logarithmic";
        } else if (equation.contains("/")) {
            return "rational";
        } else if (equation.contains("x^3")) {
            return "cubic";
        } else if (equation.contains("x^2")) {
            return "quadratic";
        } else if (equation.contains("=")) {
            return "linear";
        } else {
            return "";
        }
    }
}
