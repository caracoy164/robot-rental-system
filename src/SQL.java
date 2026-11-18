import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

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
     * Method to get specifc robot, based on serial number. 
     */
    public static Robot getRobot(String serialNumber) {
        try {
            String sql = "SELECT Status, Warranty_Expiration, Robot_Model_Number FROM Robot WHERE Robot_Serial_Number = ?";
            ps = Database.conn.prepareStatement(sql);
            ps.setString(1, serialNumber);
            ResultSet rs = ps.executeQuery();
            //chekcing if there is a row, if not then no such robot exists.
            if (rs.next()) {
                String status = rs.getString(1);
                LocalDate warrantyExpiration = LocalDate.parse(rs.getString(2));
                String robotModelNumber = rs.getString(3);
                rs.close();
                //returning the robot, by creating a new instance of robot class.
                return new Robot(serialNumber, status, warrantyExpiration, robotModelNumber);
            }
            //if no row found, then no scuhc robot exists so reutrning null
            rs.close();
            return null;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * Method to get a specific rental order, based on rental number.
     */ 
    public static RentalOrder getRentalOrder(int rentalNumber) {
        try {
            String sql = "SELECT Check_Out_Date, Due_Date, Return_Date, Status, Fees, Customer_SSN, Robot_Serial_Number, DC_Serial_Number_Dropoff, DC_Serial_Number_Pickup FROM Rental_Order WHERE Rental_Number = ?";
            ps = Database.conn.prepareStatement(sql);
            ps.setInt(1, rentalNumber);
            ResultSet rs = ps.executeQuery();
            //chekcing if there is a row, if not then no such rental order exists.
            if (rs.next()) {
                LocalDate checkOutDate = LocalDate.parse(rs.getString(1));
                LocalDate dueDate = LocalDate.parse(rs.getString(2));
                String returnDateStr = rs.getString(3);
                LocalDate returnDate=null;
                if(returnDateStr!=null){
                    returnDate=LocalDate.parse(returnDateStr);
                }
                String status = rs.getString(4);
                double fees = rs.getDouble(5);
                String customerSsn = rs.getString(6);
                String robotSerialNumber = rs.getString(7);
                String dcSerialNumberDropOff = rs.getString(8);
                String dcSerialNumberPickUp = rs.getString(9);
                rs.close();
                //returning the rental order, by creating a new instance of rental orderclass.
                return new RentalOrder(rentalNumber, customerSsn, robotSerialNumber, dcSerialNumberPickUp, dcSerialNumberDropOff, checkOutDate, dueDate, returnDate, status, fees);
            }
            //if no row found, then no such rental order exists so reutrning null
            rs.close();
            return null;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
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

    /**
     * Create PreparedStatement to update an existing robot's status, warranty expiration, and model number.
     */
    public static void ps_UpdateRobot(String sql, String status, String warrantyExpiration, String robotModelNumber, String serialNumber) {
        try {
            ps = Database.conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setString(2, warrantyExpiration);
            ps.setString(3, robotModelNumber);
            ps.setString(4, serialNumber);
            ps.executeUpdate();
            System.out.println("update complete");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Create PreparedStatement to update an existing rental order's dates, status, and fees.
     */
    public static void ps_UpdateRentalOrder(String sql, String checkOutDate, String dueDate, String returnDate, String status, double fees, int rentalNumber) {
        try {
            ps = Database.conn.prepareStatement(sql);
            ps.setString(1, checkOutDate);
            ps.setString(2, dueDate);
            ps.setString(3, returnDate);
            ps.setString(4, status);
            ps.setDouble(5, fees);
            ps.setInt(6, rentalNumber);
            ps.executeUpdate();
            System.out.println("update complete");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Create PreparedStatement to delete a robot by serial number.
     */
    public static void ps_DeleteRobot(String sql, String serialNumber) {
        try {
            ps = Database.conn.prepareStatement(sql);
            ps.setString(1, serialNumber);
            ps.executeUpdate();
            System.out.println("deletion complete");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Create PreparedStatement to delete a rental order by rental number.
     */
    public static void ps_DeleteRentalOrder(String sql, int rentalNumber) {
        try {
            ps = Database.conn.prepareStatement(sql);
            ps.setInt(1, rentalNumber);
            ps.executeUpdate();
            System.out.println("deletion complete");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Create PreparedStatement to search a robot by serial number.
     */
    public static void ps_SearchRobot(String sql, String serialNumber){
        try {
            ps = Database.conn.prepareStatement(sql);
            ps.setString(1, serialNumber);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        sqlQuery(Database.conn, ps);
    }

    /**
     * Create PreparedStatement to search a rental order by rental number.
     */
    public static void ps_SearchRentalOrder(String sql, int rentalNumber){
        try {
            ps = Database.conn.prepareStatement(sql);
            ps.setInt(1, rentalNumber);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        sqlQuery(Database.conn, ps);
    }

}

