import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;

public class RDS_GUI extends JFrame {
    DefaultTableModel modelTable;
    JTable table;
    JButton connect;
    JButton delete;
    JOptionPane passwordPane;
    JFrame frame;
    private Connection connection;
    private PreparedStatement pst;

    public RDS_GUI() throws SQLException, ClassNotFoundException {

        if(connectRDS()==true){

            String []coloumnNames = {"First Name","Surname","Email","Title","Origin","Destination"};

            setTitle("RDS Database");
            setLayout(new MigLayout());
            modelTable =new DefaultTableModel();
            modelTable.setColumnIdentifiers(coloumnNames);
            table.setModel(modelTable);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            table.setFillsViewportHeight(true);
            JScrollPane scrollPane = new JScrollPane(table);

            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            add(scrollPane);
            String name="";
            String surname="";
            String email ="";
            String title="";
            String origin ="";
            String dest="";



            try{
                pst = connection.prepareStatement("SELECT * FROM peopleDetails where First_Name =?");
                ResultSet rs = pst.executeQuery();

                while(rs.next()){

                    name=rs.getString("First_Name");
                    surname=rs.getString("Last_Name");
                    email =rs.getString("Email");
                    title=rs.getString("Title");
                    origin =rs.getString("Origin");
                    dest=rs.getString("Destination");
                    modelTable.addRow(new Object[]{"First Name","Surname","Email","Title","Origin","Destination"});

                }


            }catch(Exception ex){
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }



        }else{
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        }

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(700,900));
        pack();
        setVisible(true);
    }

    private boolean connectRDS() throws ClassNotFoundException, SQLException {


        frame=new JFrame("Connect");

        String userName = JOptionPane.showInputDialog(frame,"Enter User to Connect");
        String password = JOptionPane.showInputDialog(frame,"Enter Password to Connect");
        String dbName = "Details";
        String hostname = "college-db.czetjngd8wtj.eu-west-1.rds.amazonaws.com";
        String port = "3306";
        String jdbcUrl = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName +
                "?user=" + userName + "&password=" + password;

        if(userName.equals("Master") && password.equals("grapet45")) {

            String driver = "com.mysql.jdbc.Driver";
            Class.forName(driver);
            connection = DriverManager.getConnection(jdbcUrl);
            JOptionPane.showMessageDialog(frame,"Connection Established");
            return true;
        }
        else{
            JOptionPane.showMessageDialog(frame,"Connection Refused");
            return false;
        }

    }

}
