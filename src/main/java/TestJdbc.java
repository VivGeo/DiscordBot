/**
 * Created by vivek on 2016-07-24.
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class TestJdbc {

    private static Connection conn = null;
    private static Statement stmt = null;
    private static ResultSet rs = null;

    public static void main(String args[]){
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/dnd?" +
                    "user=vivek&password=passw0rd");
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT id,name FROM gamechar WHERE id = 4277");

            if ( rs != null ){
                while (rs.next()){
                    System.out.println( rs.getString(1) +"."+rs.getString(2) );
                }
            }


        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }


}
