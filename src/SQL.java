import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;

/**
 * 
 * All database connectivity should be handled from within here.
 *
 */
public class SQL {
	
	private static PreparedStatement ps;
	
    /**
     * Queries the database and prints the results.
     * 
     * @param conn a connection object
     * @param sql a SQL statement that returns rows:
     * 		this query is written with the Statement class, typically 
     * 		used for static SQL SELECT statements.
     */
    public static void sqlQuery(Connection conn, String sql){
        try {
        	Statement stmt = conn.createStatement();
        	ResultSet rs = stmt.executeQuery(sql);
        	ResultSetMetaData rsmd = rs.getMetaData();
        	int columnCount = rsmd.getColumnCount();
        	for (int i = 1; i <= columnCount; i++) {
        		String value = rsmd.getColumnName(i);
        		System.out.print(value);
        		if (i < columnCount) System.out.print(",  ");
        	}
			System.out.print("\n");
        	while (rs.next()) {
        		for (int i = 1; i <= columnCount; i++) {
        			String columnValue = rs.getString(i);
            		System.out.print(columnValue);
            		if (i < columnCount) System.out.print(",  ");
        		}
    			System.out.print("\n");
        	}
        	rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    
    /**
     * Queries the database and prints the results.
     * 
     * @param conn a connection object
     * @param sql a SQL statement that returns rows:
     * 		this query is written with the PrepareStatement class, typically 
     * 		used for dynamic SQL SELECT statements.
     */
    public static void sqlQuery(Connection conn, PreparedStatement sql){
        try {
        	ResultSet rs = sql.executeQuery();
        	ResultSetMetaData rsmd = rs.getMetaData();
        	int columnCount = rsmd.getColumnCount();
        	for (int i = 1; i <= columnCount; i++) {
        		String value = rsmd.getColumnName(i);
        		System.out.print(value);
        		if (i < columnCount) System.out.print(",  ");
        	}
			System.out.print("\n");
        	while (rs.next()) {
        		for (int i = 1; i <= columnCount; i++) {
        			String columnValue = rs.getString(i);
            		System.out.print(columnValue);
            		if (i < columnCount) System.out.print(",  ");
        		}
    			System.out.print("\n");
        	}
        	rs.close();
        	ps.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    
    /**
     * Create PreparedStatement to insert a new robot.
     * 
     * @param sql query for prepared statement
     * 
     * @param serialNumber robot serial number
     * @param status robot status
     * @param warrantyExpiration warranty expiration of robot
     * @param robotModelNumber robot model number
     */
    public static void ps_AddRobot(String sql, String serialNumber, String status, String warrantyExpiration, String robotModelNumber){
    	try {
    		ps = Database.conn.prepareStatement(sql);
    		ps.setString(1, serialNumber);
            ps.setString(2, status);
            ps.setString(3, warrantyExpiration);
            ps.setString(4, robotModelNumber);
            ps.executeUpdate();
            System.out.println("insertion complete");
    	} catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Create PreparedStatement to insert a new facility asset, because each robot also requires a facilyt asset entry.
     * 
     * @param sql query for prepared statement
     * 
     * @param serialNumber robot serial number
     * @param state state of the location of the warehouse robot is associated with.
     * @param city city of the location of the warehouse robot is associated with.
     * @param streetAddress state of the location of the warehouse robot is associated with.
     */
    public static void ps_AddFacilityAsset(String sql, String serialNumber, String state, String city, String streetAddress){
    	try {
    		ps = Database.conn.prepareStatement(sql);
    		ps.setString(1, serialNumber);
            ps.setString(2, state);
            ps.setString(3, city);
            ps.setString(4, streetAddress);
            ps.executeUpdate();
            System.out.println("insertion complete");
    	} catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Create PreparedStatement to insert a new rental order.
     * 
     * @param sql query for prepared statement
     * 
     * @param rentalNumber rental order numer
     * @param CustomerSsn ssn of customer
     * @param robotSerialNumber serial number of robot rented.
     * @param dcSerialNumberPickUp serial number of driverless car that picks up the robot.
     * @param dcSerialNumberDropOff serial number of driverless car that delviers and Drops off up the robot.
     * @param checkOutDate date the robot is checked out.
     * @param dueDate date the robot is due.
     * @param returnDate date the robot is returned.
     * @param status status of the order.
     * @param fees fees for the order.
     */

    public static void ps_AddRentalOrder(String sql, int rentalNumber, String CustomerSsn, String robotSerialNumber, String dcSerialNumberPickUp, String dcSerialNumberDropOff, String checkOutDate, String dueDate, String returnDate, String status, double fees){
    	try {
    		ps = Database.conn.prepareStatement(sql);
    		ps.setInt(1, rentalNumber);
            ps.setString(2, checkOutDate);
            ps.setString(3, dueDate);
            ps.setString(4, returnDate);
            ps.setString(5, status);
            ps.setDouble(6, fees);
            ps.setString(7, CustomerSsn);
            ps.setString(8, robotSerialNumber);
            ps.setString(9, dcSerialNumberDropOff);
            ps.setString(10, dcSerialNumberPickUp);
            ps.executeUpdate();
            System.out.println("insertion complete");
    	} catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}

