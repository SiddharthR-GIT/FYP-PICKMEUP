package q2;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;



public class DisplayEmpData extends JFrame implements ActionListener {


    JFrame frame1;

    JLabel l0, l1, l2;

    JComboBox c1;

    JButton b1;

    Connection con;

    ResultSet rs, rs1;

    Statement st, st1;

    PreparedStatement pst;

    String ids;

    static JTable table;

    String[] columnNames = {"User name", "Email", "Password", "Country"};

    String from;


    DisplayEmpData() {




        try {
            String userName = "Master";
            String password = "grapet45";
            String dbName = "Details";
            String hostname = "college-db.czetjngd8wtj.eu-west-1.rds.amazonaws.com";
            String port = "3306";
            String jdbcUrl = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName +
                    "?user=" + userName + "&password=" + password;

            Class.forName("com.mysql.jdbc.Driver");

            con = DriverManager.getConnection(jdbcUrl);


        } catch (Exception e) {

        }

    }


    public void actionPerformed(ActionEvent ae) {

        if (ae.getSource() == b1) {

            showTableData();

        }


    }


    public void showTableData() {


        frame1 = new JFrame("Database Search Result");

        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame1.setLayout(new BorderLayout());

//TableModel tm = new TableModel();

        DefaultTableModel model = new DefaultTableModel();

        model.setColumnIdentifiers(columnNames);

//DefaultTableModel model = new DefaultTableModel(tm.getData1(), tm.getColumnNames());

//table = new JTable(model);

        table = new JTable();

        table.setModel(model);

        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        table.setFillsViewportHeight(true);

        JScrollPane scroll = new JScrollPane(table);

        scroll.setHorizontalScrollBarPolicy(

                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        scroll.setVerticalScrollBarPolicy(

                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        from = (String) c1.getSelectedItem();


        String uname = "";

        String email = "";




        try {

            pst = con.prepareStatement("select * from peopleDetails where First_name=?");

            ResultSet rs = pst.executeQuery();

            int i = 0;

            if (rs.next()) {

                uname = rs.getString("First_Name");

                email = rs.getString("Email");



                model.addRow(new Object[]{uname, email});

            }


        } catch (Exception ex) {

            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

        }

        frame1.add(scroll);

        frame1.setVisible(true);

        frame1.setSize(400, 300);

    }


    public static void main(String args[]) {

        new DisplayEmpData();

    }

}