import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

// Aisha Nicole L. Dones
// Section A122

public class AverageGrade {

    private static JPanel panel;
    private static JTextField nameField, programField, courseField,
            exam1Field, exam2Field, exam3Field, exam4Field;

    private static String[][] rowData = new String[1][9];
    private static final String[] columnNames = {"Name", "Program", "Course", "First", "Second", "Third", "Fourth", "Average", "Remarks"};

    public static void main(String[] args){ script(); }

    private static void script(){
        String name, program, course, remarks,
                strE1, strE2, strE3, strE4, strAve;
        int result;
        double exam1, exam2, exam3, exam4, ave;

        createPanel();

        result = JOptionPane.showConfirmDialog(null, panel,
                "Average Grade", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            // check for errors
            try {
                name = nameField.getText();
                program = programField.getText();
                course = courseField.getText();

                exam1 = Double.parseDouble(exam1Field.getText());
                exam2 = Double.parseDouble(exam2Field.getText());
                exam3 = Double.parseDouble(exam3Field.getText());
                exam4 = Double.parseDouble(exam4Field.getText());

                // check if grades are in percentage before computing
                if (exam1 < 0 || exam1 > 100 || exam2 < 0 || exam2 > 100 || exam3 < 0 || exam3 > 100 || exam4 < 0 || exam4 > 100) {
                    JOptionPane.showMessageDialog(null, "INVALID INPUT! Please enter grades in percentage (0-100).",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                    script(); // run script again
                }

                ave = getAverage(exam1, exam2, exam3, exam4);
                remarks = getRemarks(ave);

                strE1 = String.format("%.2f", exam1);
                strE2 = String.format("%.2f", exam2);
                strE3 = String.format("%.2f", exam3);
                strE4 = String.format("%.2f", exam4);
                strAve = String.format("%.2f", ave);

                rowData[0][0] = name;
                rowData[0][1] = program;
                rowData[0][2] = course;
                rowData[0][3] = strE1;
                rowData[0][4] = strE2;
                rowData[0][5] = strE3;
                rowData[0][6] = strE4;
                rowData[0][7] = strAve;
                rowData[0][8] = remarks;

                dataToFile();
            }
            catch (Exception e){
                JOptionPane.showMessageDialog(null, "INVALID INPUT! Please try again.", "ERROR", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                script(); // run script again
            }

        }
    }

    private static void dataToFile(){

        DefaultTableModel model = new DefaultTableModel(rowData, columnNames);
        JTable table = new JTable(model);

        // Set column widths based on maximum length of values in each column
        for (int i = 0; i < table.getColumnCount(); i++) {
            int maxWidth = 0;
            for (int j = 0; j < table.getRowCount(); j++) {
                String value = table.getValueAt(j, i).toString();
                int width = value.length() * 8; // adjust the multiplier as needed
                maxWidth = Math.max(maxWidth, width);
            }
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(maxWidth);
        }

        // Export table to file
        try {
            FileWriter writer = new FileWriter(new File("Student.txt"));

            writer.write("\n************************* Student Information ************************\n");

            // Calculate maximum length of values in each column
            int[] maxLengths = new int[columnNames.length];
            for (int i = 0; i < table.getRowCount(); i++) {
                for (int j = 0; j < table.getColumnCount(); j++) {
                    String value = table.getValueAt(i, j).toString();
                    maxLengths[j] = Math.max(maxLengths[j], value.length());
                }
            }

            // Write column names to file
            for (int i = 0; i < table.getColumnCount(); i++) {
                String columnName = table.getColumnName(i);
                int padding = maxLengths[i] - columnName.length() + 8; // add 5 extra spaces for padding
                writer.write(columnName + String.format("%" + padding + "s", "") + "\t");
            }
            writer.write("\n");

            // Write data to file
            for (int i = 0; i < table.getRowCount(); i++) {
                for (int j = 0; j < table.getColumnCount(); j++) {
                    String value = table.getValueAt(i, j).toString();
                    int padding = maxLengths[j] - value.length() + 3; // add 5 extra spaces for padding
                    writer.write(value + String.format("%" + padding + "s", "") + "\t\t");
                }
                writer.write("\n");
            }

            writer.write("\n\n(Submitted by: Aisha Nicole L. Dones)\n");

            writer.close();
            JOptionPane.showMessageDialog(null, "Sales information has been saved in file.");

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "An error occurred while writing to file.",
                    "ERROR", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    } // end of dataToFile method

    private static double getAverage(double e1, double e2, double e3, double e4){ return (e1+e2+e3+e4)/4; }
    private static String getRemarks(double ave){
        if (ave >= 75){ return "PASSED"; }
        else{ return "FAILED"; }
    }

    private static void createPanel(){

        JPanel namePanel, programPanel, coursePanel, examsPanel, exam1Panel, exam2Panel, exam3Panel, exam4Panel;

        panel = new JPanel();

        nameField = new JTextField();
        programField = new JTextField();
        courseField = new JTextField();
        exam1Field = new JTextField();
        exam2Field = new JTextField();
        exam3Field = new JTextField();
        exam4Field = new JTextField();

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        namePanel = new JPanel(new GridLayout(1, 2));
        programPanel = new JPanel(new GridLayout(1, 2));
        coursePanel = new JPanel(new GridLayout(1, 2));
        examsPanel = new JPanel(new GridLayout(1, 1));
        exam1Panel = new JPanel(new GridLayout(1, 2));
        exam2Panel = new JPanel(new GridLayout(1, 2));
        exam3Panel = new JPanel(new GridLayout(1, 2));
        exam4Panel = new JPanel(new GridLayout(1, 2));

        createPanel(namePanel, nameField, "Name: ", 5);
        createPanel(programPanel, programField, "Program: ", 5);
        createPanel(coursePanel, courseField, "Course: ", 20);
        createPanel(examsPanel, "Please input the grades for the following examinations: ", 10);
        createPanel(exam1Panel, exam1Field, "First: ", 5);
        createPanel(exam2Panel, exam2Field, "Second: ", 5);
        createPanel(exam3Panel, exam3Field, "Third: ", 5);
        createPanel(exam4Panel, exam4Field, "Fourth: ", 5);


    } // end of createPanel method

    private static void createPanel(JPanel p, String label, int space){
        p.add(new JLabel(label));
        panel.add(p);
        panel.add(Box.createVerticalStrut(space)); // a spacer
    }

    private static void createPanel(JPanel p, JTextField jt, String label, int space){
        p.add(new JLabel(label));
        p.add(jt);
        panel.add(p);
        panel.add(Box.createVerticalStrut(space)); // a spacer
    }

} // end of class