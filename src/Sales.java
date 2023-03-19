import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

// Aisha Nicole L. Dones
// Section A122

public class Sales {

    private static JPanel panel;
    private static JTextField grossField, expensesField;

    private static String[][] rowData = new String[12][4];
    private static final String[] months = {"January", "February", "March", "April", "May", "June", "July",
            "August", "September", "October", "November", "December"};
    private static final String[] columnNames = {"Month", "Gross Income", "Total Expenses", "Net Income"};

    public static void main(String[] args){ script(); } // end of main

    private static void script(){
        int result;
        boolean errorCaught = false;

        double[] gross = new double[12];
        double[] expenses = new double[12];
        double[] net = new double[12];

        String[] strGross = new String[12];
        String[] strExpenses = new String[12];
        String[] strNet = new String[12];

        for (int i = 0; i < months.length; i++){
            createPanel();

            result = JOptionPane.showConfirmDialog(null, panel,
                    "Sales for the month of " + months[i], JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    if (grossField.getText() == null || grossField.getText().equals("")){
                        gross[i] = 0;
                    } else {
                        gross[i] = Double.parseDouble(grossField.getText());
                    }

                    if (grossField.getText() == null || grossField.getText().equals("")){
                        expenses[i] = 0;
                        net[i] = 0;
                    } else {
                        expenses[i] = Double.parseDouble(expensesField.getText());
                        net[i] = gross[i] - expenses[i];
                    }

                    strGross[i] = String.format("%.2f", gross[i]);
                    strExpenses[i] = String.format("%.2f", expenses[i]);
                    strNet[i] = String.format("%.2f", net[i]);

                    rowData[i][0] = months[i];
                    rowData[i][1] = strGross[i];
                    rowData[i][2] = strExpenses[i];
                    rowData[i][3] = strNet[i];

                } catch (Exception e){
                    JOptionPane.showMessageDialog(null, "INVALID INPUT! Please try again.",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                    errorCaught = true;
                    break;
                }
            }

        } // end of for loop

        if (errorCaught){
            script(); // run script again
        }
        else{
            dataToFile(gross, expenses, net);
        }

    } // end of script method

    private static double getTotal(double[] array){
        double total = 0;
        for (double n : array) {
            total = total + n;
        }
        return total;
    } // end of getTotal method

    private static void dataToFile(double[] gross, double[] expenses, double[] net){

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
            FileWriter writer = new FileWriter(new File("Sales.txt"));

            writer.write("\n************************* AISHIE MARKETING ************************");
            writer.write("\nYear\t:\t 2023");
            writer.write("\n*******************************************************************\n\n");

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
                    int padding = maxLengths[j] - value.length() + 5; // add 5 extra spaces for padding
                    writer.write(value + String.format("%" + padding + "s", "") + "\t\t");
                }
                writer.write("\n");
            }

            writer.write("TOTAL: " + String.format("%5s", "") + "\t\t");
            writer.write(String.format("%.2f", getTotal(gross)) + String.format("%5s", "") + "\t\t");
            writer.write(String.format("%.2f", getTotal(expenses)) + String.format("%5s", "") + "\t\t");
            writer.write(String.format("%.2f", getTotal(net)) + String.format("%5s", "") + "\t\t");
            writer.write("\n\n************************* nothing follows *************************\n");
            writer.write("\n\n(Submitted by: Aisha Nicole L. Dones)\n");

            writer.close();
            JOptionPane.showMessageDialog(null, "Sales information has been saved in file.");

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "An error occurred while writing to file.",
                    "ERROR", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    } // end of dataToFile method

    private static void createPanel(){

        JPanel grossPanel, expensesPanel;

        panel = new JPanel();

        grossField = new JTextField();
        expensesField = new JTextField();

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        grossPanel = new JPanel(new GridLayout(1, 2));
        expensesPanel = new JPanel(new GridLayout(1, 2));

        createPanel(grossPanel, grossField, "Gross Income: ");
        createPanel(expensesPanel, expensesField, "Total Expenses: ");


    } // end of createPanel method

    private static void createPanel(JPanel p, JTextField jt, String label){
        p.add(new JLabel(label));
        p.add(jt);
        panel.add(p);
        panel.add(Box.createVerticalStrut(5)); // a spacer
    } // end of createPanel method with 3 parameters

} // end of class