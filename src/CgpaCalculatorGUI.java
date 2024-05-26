import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

public class CgpaCalculatorGUI extends JFrame {
    private JTextField courseCountField;
    private JPanel inputPanel;
    private JButton calculateButton;
    private JTextArea resultArea;

    public CgpaCalculatorGUI() {
        setTitle("CGPA Calculator");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // Top panel for number of courses input
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Enter the number of courses: "));
        courseCountField = new JTextField(5);
        topPanel.add(courseCountField);

        JButton enterButton = new JButton("Enter");
        topPanel.add(enterButton);
        add(topPanel, BorderLayout.NORTH);

        // Center panel for dynamic course inputs
        inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(0, 2));
        add(new JScrollPane(inputPanel), BorderLayout.CENTER);

        // Bottom panel for results
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());

        calculateButton = new JButton("Calculate CGPA");
        calculateButton.setEnabled(false);  // Disable until courses are entered
        bottomPanel.add(calculateButton, BorderLayout.NORTH);

        resultArea = new JTextArea(10, 40);
        resultArea.setEditable(false);
        bottomPanel.add(new JScrollPane(resultArea), BorderLayout.CENTER);

        add(bottomPanel, BorderLayout.SOUTH);

        // Action listener for the enter button
        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int courseCount;
                try {
                    courseCount = Integer.parseInt(courseCountField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid number of courses.");
                    return;
                }
                inputPanel.removeAll();
                for (int i = 1; i <= courseCount; i++) {
                    inputPanel.add(new JLabel("Course " + i + " credit hours:"));
                    inputPanel.add(new JTextField());
                    inputPanel.add(new JLabel("Course " + i + " marks:"));
                    inputPanel.add(new JTextField());
                }
                inputPanel.revalidate();
                inputPanel.repaint();
                calculateButton.setEnabled(true);
            }
        });

        // Action listener for the calculate button
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateCGPA();
            }
        });
    }

    private void calculateCGPA() {
        Component[] components = inputPanel.getComponents();
        double cred = 0;
        double gpa = 0;
        double totalMarks = 0;
        int courseCount = components.length / 4;

        StringBuilder result = new StringBuilder("Course Grades:\n");

        try {
            for (int i = 0; i < components.length; i += 4) {
                JTextField creditField = (JTextField) components[i + 1];
                JTextField marksField = (JTextField) components[i + 3];

                float crhrs = Float.parseFloat(creditField.getText());
                double nm = Double.parseDouble(marksField.getText());

                totalMarks += nm;

                double gp;
                if (nm >= 80) {
                    gp = 4.0;
                } else if (nm >= 70) {
                    gp = 3.0;
                } else if (nm >= 60) {
                    gp = 2.0;
                } else if (nm >= 50) {
                    gp = 1.0;
                } else {
                    gp = 0.0;
                }

                double subjectGPA = gp * crhrs;
                gpa += subjectGPA;
                cred += crhrs;

                result.append("Course ").append((i / 4) + 1).append(": Marks = ").append(nm).append(", GPA = ").append(gp).append("\n");
            }

            double cgpa = gpa / cred;
            double averageMarks = totalMarks / courseCount;
            DecimalFormat df = new DecimalFormat("#.00");

            result.append("\nTotal Marks: ").append(totalMarks);
            result.append("\nAverage Marks: ").append(df.format(averageMarks));
            result.append("\nTotal CGPA: ").append(df.format(cgpa));

            resultArea.setText(result.toString());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Please enter valid numbers for credit hours and marks.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CgpaCalculatorGUI().setVisible(true);
            }
        });
    }
}
