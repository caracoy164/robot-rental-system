import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import java.sql.SQLException;

public class App {
    private static final Scanner scanner = new Scanner(System.in);
    private static final ArrayList<Robot> robots = new ArrayList<>();
    private static final ArrayList<RentalOrder> rentalOrders = new ArrayList<>();

    public static void main(String[] args) {
        Database.conn = Database.initializeDB("robot_company.db");
        boolean running = true;

        while (running) {
            System.out.println("\n=== Robot Rental System ===");
            System.out.println("1. Manage Robots");
            System.out.println("2. Manage Rental Orders");
            System.out.println("3. Rent a Robot");
            System.out.println("4. Return Equipment");
            System.out.println("5. Schedule Robot Delivery");
            System.out.println("6. Schedule Robot Pickup");
            System.out.println("7. View Useful Reports");
            System.out.println("8. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    robotMenu();
                    break;
                case "2":
                    rentalOrderMenu();
                    break;
                case "3":
                    rentRobot();
                    break;
                case "4":
                    returnEquipment();
                    break;
                case "5":
                    scheduleDelivery();
                    break;
                case "6":
                    schedulePickup();
                    break;
                case "7":
                    viewUsefulReports();
                    break;
                case "8":
                    running = false;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }

        scanner.close();
        try {
            Database.conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    private static void robotMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("\n=== Manage Robots ===");
            System.out.println("1. Add Robot");
            System.out.println("2. List Robots");
            System.out.println("3. Search Robot");
            System.out.println("4. Edit Robot");
            System.out.println("5. Delete Robot");
            System.out.println("6. Back to Main Menu");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    addRobot();
                    break;
                case "2":
                    listRobots();
                    break;
                case "3":
                    searchRobot();
                    break;
                case "4":
                    editRobot();
                    break;
                case "5":
                    deleteRobot();
                    break;
                case "6":
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }


    private static void rentalOrderMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("\n=== Manage Rental Orders ===");
            System.out.println("1. Add Rental Order");
            System.out.println("2. List Rental Orders");
            System.out.println("3. Search Rental Order");
            System.out.println("4. Edit Rental Order");
            System.out.println("5. Delete Rental Order");
            System.out.println("6. Back to Main Menu");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    addRentalOrder();
                    break;
                case "2":
                    listRentalOrders();
                    break;
                case "3":
                    searchRentalOrder();
                    break;
                case "4":
                    editRentalOrder();
                    break;
                case "5":
                    deleteRentalOrder();
                    break;
                case "6":
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    
    private static void addRobot() {
        //not doing any check for any attribute value except for date because foreign key/primary keys in the databse will do it anyway.
        System.out.print("Serial number: ");
        String serialNumber = scanner.nextLine().trim();

        System.out.print("Status: ");
        String status = scanner.nextLine().trim();

        System.out.print("Warranty expiration (YYYY-MM-DD): ");
        LocalDate warrantyExpiration;

        //checking date, because otherwise the code may except the wrong form of date, just because its a string, but we also want it to be in proper date form, so adding the check here. 
        try {
            warrantyExpiration = LocalDate.parse(scanner.nextLine().trim());
        } catch (Exception e) {
            //reaches here if date is not valid and so we must exit. 
            System.out.println("Invalid date. Robot was not added.");
            return;
        }

        System.out.print("Robot model number (press Enter to leave blank): ");
        String robotModelNumber = scanner.nextLine().trim();
        //inserting null, when robot model is empty. 
        if(robotModelNumber.isEmpty()){
            robotModelNumber=null;
        }

        String robotSql= "INSERT INTO Robot VALUES(?,?,?,?)";
        SQL.ps_AddRobot(robotSql, serialNumber, status, warrantyExpiration.toString(),robotModelNumber);

        System.out.print("Facility state:");
        String state = scanner.nextLine().trim();

        System.out.print("Facility city:");
        String city = scanner.nextLine().trim();

        System.out.print("Facility street adress:");
        String adress = scanner.nextLine().trim();
        
        String assetSql= "INSERT INTO Facility_Assets VALUES(?,?,?,?)";
        SQL.ps_AddFacilityAsset(assetSql, serialNumber, state, city, adress);
        System.out.println("Robot added successfully.");
    }

    private static void listRobots() {
        if (robots.isEmpty()) {
            System.out.println("No robots found.");
            return;
        }

        for (Robot robot : robots) {
            System.out.println(robot);
        }
    }

    private static void searchRobot() {
        System.out.print("Enter robot serial number: ");
        String serialNumber = scanner.nextLine().trim();
        String sql = "SELECT * FROM Robot WHERE Robot_Serial_Number = ?";
        SQL.ps_SearchRobot(sql, serialNumber);
    }

    private static void editRobot() {
        System.out.print("Enter serial number of robot to edit: ");
        String serialNumber = scanner.nextLine().trim();

        Robot robot = SQL.getRobot(serialNumber);
        if (robot == null) {
            System.out.println("No such robot found");
            return;
        }

        System.out.print("New status (press Enter to keep current): ");
        String status = scanner.nextLine().trim();
        if(status.isEmpty()){
            status= robot.getStatus();
        }

        System.out.print("New warranty expiration YYYY-MM-DD (press Enter to keep current): ");
        String dateInput = scanner.nextLine().trim();
        if(dateInput.isEmpty()){
            LocalDate temp= robot.getWarrantyExpiration();
            dateInput= temp.toString();
        }

        System.out.print("New robot model (press Enter to keep current): ");
        String model = scanner.nextLine().trim();
        if (model.isEmpty()) {
            model= robot.getRobotModelNumber();
        }

        String sql = "UPDATE Robot SET Status = ?, Warranty_Expiration = ?, Robot_Model_Number = ? WHERE Robot_Serial_Number = ?";
        SQL.ps_UpdateRobot(sql, status, dateInput, model, serialNumber);
    }

    private static void deleteRobot() {
        System.out.print("Enter serial number of robot to delete: ");
        String serialNumber = scanner.nextLine().trim();
        String sql = "DELETE FROM Robot WHERE Robot_Serial_Number = ?";
        SQL.ps_DeleteRobot(sql, serialNumber);
    }


    private static void addRentalOrder() {
        int rentalNumber;
        // making sure input is a vlaid number. 
        try {
            System.out.print("Rental number: ");
            rentalNumber = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid rental number.");
            return;
        }

        System.out.print("Customer SSN: ");
        String customerSsn = scanner.nextLine().trim();

        System.out.print("Robot serial number: ");
        String robotSerialNumber = scanner.nextLine().trim();

        LocalDate checkOutDate = readDate("Check out date (YYYY-MM-DD): ");
        if (checkOutDate == null) {
            System.out.println("Invalid date. Rental order was not added.");
            return;
        }

        LocalDate dueDate = readDate("Due date (YYYY-MM-DD): ");
        if (dueDate == null || dueDate.isBefore(checkOutDate)) {
            System.out.println("Invalid due date. It must be on or after checkout.");
            return;
        }

        System.out.print("Return date (YYYY-MM-DD, or press Enter if not returned): ");
        String returnInput = scanner.nextLine().trim();
        LocalDate returnDate = null;

        if (!returnInput.isEmpty()) {
            try {
                returnDate = LocalDate.parse(returnInput);

                if (returnDate.isBefore(checkOutDate)) {
                    System.out.println("Return date cannot be before checkout.");
                    return;
                }
            } catch (Exception e) {
                System.out.println("Invalid return date.");
                return;
            }
        }

        String returnDateStr = null;
        if(returnDate != null){
            returnDateStr = returnDate.toString();
        }

        System.out.print("Status: ");
        String status = scanner.nextLine().trim();

        double fees;
        try {
            System.out.print("Fees: ");
            fees = Double.parseDouble(scanner.nextLine().trim());

            if (!Double.isFinite(fees) || fees < 0) {
                System.out.println("Fees must be a nonnegative number.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid fees.");
            return;
        }

        System.out.print("Drop off driverless car serial number (press enter to leave blank): ");
        String dcDrop= scanner.nextLine().trim();
        //inserting null, when robot model is empty. 
        if(dcDrop.isEmpty()){
            dcDrop=null;
        }

        System.out.print("Pick up driverless car serial number (press enter to leave blank): ");
        String dcPick= scanner.nextLine().trim();
        //inserting null, when robot model is empty. 
        if(dcPick.isEmpty()){
            dcPick=null;
        }

        String sql= "INSERT INTO Rental_Order VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        SQL.ps_AddRentalOrder(sql, rentalNumber, customerSsn, robotSerialNumber,dcPick, dcDrop, checkOutDate.toString(),dueDate.toString(), returnDateStr, status, fees);
    }

    private static void listRentalOrders() {
        if (rentalOrders.isEmpty()) {
            System.out.println("No rental orders found.");
            return;
        }

        for (RentalOrder order : rentalOrders) {
            System.out.println(order);
        }
    }

    private static void searchRentalOrder() {
        int rentalNumber;

        try {
            System.out.print("Enter rental number: ");
            rentalNumber = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid rental number.");
            return;
        }
        
        String sql = "SELECT * FROM Rental_Order WHERE Rental_Number = ?";
        SQL.ps_SearchRentalOrder(sql, rentalNumber);
    }

    private static void editRentalOrder() {
        int rentalNumber;

        try {
            System.out.print("Enter rental number to edit: ");
            rentalNumber = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid rental number.");
            return;
        }

        RentalOrder order = SQL.getRentalOrder(rentalNumber);
        if (order == null) {
            System.out.println("Rental order not found.");
            return;
        }

        System.out.print("New checkout date (press Enter to keep current): ");
        String checkoutInput = scanner.nextLine().trim();
        if (checkoutInput.isEmpty()) {
            checkoutInput = order.getCheckOutDate().toString();
        }

        System.out.print("New due date (press Enter to keep current): ");
        String dueInput = scanner.nextLine().trim();
        if (dueInput.isEmpty()) {
            dueInput = order.getDueDate().toString();
        }

        System.out.print("New return date (press Enter to keep current, type NONE to clear): ");
        String returnInput = scanner.nextLine().trim();
        String returnDateStr;

        if (returnInput.equalsIgnoreCase("NONE")) {
            returnDateStr = null;
        } else if (returnInput.isEmpty()) {
            returnDateStr=null;
            if(order.getReturnDate() !=null){
                returnDateStr=order.getReturnDate().toString();
            }
        }else {
            returnDateStr = returnInput;
        }
    
        System.out.print("New status (press Enter to keep current): ");
        String status = scanner.nextLine().trim();
        if (status.isEmpty()) {
            status = order.getStatus();
        }

        System.out.print("New fees (press Enter to keep current): ");
        String feesInput = scanner.nextLine().trim();
        double fees= order.getFees();
        if(!feesInput.isEmpty()){
            fees= Double.parseDouble(feesInput);
        }

        String sql = "UPDATE Rental_Order SET Check_Out_Date = ?, Due_Date = ?, Return_Date = ?, Status = ?, Fees = ? WHERE Rental_Number = ?";
        SQL.ps_UpdateRentalOrder(sql, checkoutInput, dueInput, returnDateStr, status, fees, rentalNumber);
    }

    private static void deleteRentalOrder() {
        int rentalNumber;

        try {
            System.out.print("Enter rental number to delete: ");
            rentalNumber = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid rental number.");
            return;
        }

        String sql = "DELETE FROM Rental_Order WHERE Rental_Number = ?";
        SQL.ps_DeleteRentalOrder(sql, rentalNumber);

    }

    private static LocalDate readDate(String prompt) {
        System.out.print(prompt);

        try {
            return LocalDate.parse(scanner.nextLine().trim());
        } catch (Exception e) {
            return null;
        }
    }

    private static void rentRobot() {
        System.out.println("\n=== Rent a Robot ===");

        int rentalNumber;
        try {
            System.out.print("Enter rental number to edit: ");
            rentalNumber = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid rental number.");
            return;
        }

        System.out.print("Enter customer SSN: ");
        String customerSSN = scanner.nextLine().trim();

        System.out.print("Enter robot serial number: ");
        String robotSerialNumber = scanner.nextLine().trim();

        LocalDate checkOutDate = readDate("Check out date (YYYY-MM-DD): ");
        if (checkOutDate == null) {
            System.out.println("Invalid date, so rental order not created.");
            return;
        }
        
        LocalDate dueDate = readDate("Due date (YYYY-MM-DD): ");
        if (dueDate == null || dueDate.isBefore(checkOutDate)) {
            System.out.println("Invalid due date, so rental order not created. Please remeber due date must be on or after checkout date.");
            return;
        }

        String rental_sql = "INSERT INTO Rental_Order VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        //creating new rental order, with dcserialnumber for pick up and drop=null, sicne driverless car is not assgined yet, and with fees=0, since its not calculated yet. 
        SQL.ps_AddRentalOrder(rental_sql, rentalNumber, customerSSN, robotSerialNumber,null, null, checkOutDate.toString(), dueDate.toString(), null, "Active", 0.0);
        Robot robot = SQL.getRobot(robotSerialNumber);
        //using update method, instead of creating seperate methods, to updat the status. using current values for all atribute values except for status, to ensure nothing else is changed.
        if (robot != null) {
            String updateSql = "UPDATE Robot SET Status = ?, Warranty_Expiration = ?, Robot_Model_Number = ? WHERE Robot_Serial_Number = ?";
            SQL.ps_UpdateRobot(updateSql, "Rented", robot.getWarrantyExpiration().toString(), robot.getRobotModelNumber(), robotSerialNumber);
        }

        System.out.println("Rental order information entered successfully.");
    }

    private static void returnEquipment() {
        System.out.println("\n=== Return Equipment ===");

        int rentalNumber;
        try {
            System.out.print("Enter rental number to edit: ");
            rentalNumber = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid rental number.");
            return;
        }

        RentalOrder order = SQL.getRentalOrder(rentalNumber);
        if (order == null) {
            System.out.println("Rental order not found.");
            return;
        }

        LocalDate returnDate = readDate("Return date (YYYY-MM-DD): ");
        if (returnDate == null) {
            System.out.println("Invalid date, so return is not stored.");
            return;
        }

        String rental_sql = "UPDATE Rental_Order SET Check_Out_Date = ?, Due_Date = ?, Return_Date = ?, Status = ?, Fees = ? WHERE Rental_Number = ?";
        SQL.ps_UpdateRentalOrder(rental_sql, order.getCheckOutDate().toString(), order.getDueDate().toString(), returnDate.toString(), "Returned", order.getFees(), rentalNumber);

        Robot robot = SQL.getRobot(order.getRobotSerialNumber());
        //reusing update method, with current values of robot, except for status, which is the one we are tyring to update.
        if (robot != null) {
            String updateSql = "UPDATE Robot SET Status = ?, Warranty_Expiration = ?, Robot_Model_Number = ? WHERE Robot_Serial_Number = ?";
            SQL.ps_UpdateRobot(updateSql, "Available", robot.getWarrantyExpiration().toString(), robot.getRobotModelNumber(), robot.getSerialNumber());
        }

        System.out.println("Equipment return stored successfully.");
    }

    private static void scheduleDelivery() {
        System.out.println("\n=== Schedule Robot Delivery ===");

        int rentalNumber;
        try {
            System.out.print("Enter rental number to edit: ");
            rentalNumber = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid rental number.");
            return;
        }

        System.out.print("Enter driverless car serial number for the car that will drop the robot: ");
        String dcSerialNumber = scanner.nextLine().trim();

        String sql = "UPDATE Rental_Order SET DC_Serial_Number_Dropoff = ? WHERE Rental_Number = ?";
        SQL.ps_UpdateDropoffCar(sql, dcSerialNumber, rentalNumber);

        System.out.println("Delivery information entered correctly.");
    }

    private static void schedulePickup() {
        System.out.println("\n=== Schedule Robot Pick up ===");

        int rentalNumber;
        try {
            System.out.print("Enter rental number to edit: ");
            rentalNumber = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid rental number.");
            return;
        }

        System.out.print("Enter driverless car serial number for the car that will pick up the robot: ");
        String dcSerialNumber = scanner.nextLine().trim();

        String sql = "UPDATE Rental_Order SET DC_Serial_Number_Pickup = ? WHERE Rental_Number = ?";
        SQL.ps_UpdatePickupCar(sql, dcSerialNumber, rentalNumber);
        
        System.out.println("Pick up information entered correctly.");
    }

    public static void viewUsefulReports() {
        System.out.println("\n=== Useful Reports ===");
        System.out.println("1. List all all of the robots rented out by a specific customer");
        System.out.println("2. Find the most used robot by rent time");
        System.out.println("3. Find the most popular manufacturer by the number of robots rented");
        System.out.println("4. Find the most used Driverless Car in the database by delivery distance");
        System.out.println("5. Find the customer who has rented the most robots");
        System.out.println("6. List all of the robots per manufacturer that were released before a specific year");
        System.out.println("7. Back to Main Menu");
        System.out.print("Choose an option: ");

        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                System.out.println("Enter customer SSN: ");
                int customerSSN = Integer.parseInt(scanner.nextLine());
                String sql1 = "SELECT * FROM Rental_Order WHERE Customer_SSN = ?";
                SQL.ps_listRobotsByCustomer(sql1, customerSSN);
                break;
            case "2":
                String sql2 = "SELECT Robot_Serial_Number, SUM(JULIANDAY(Return_Date) - JULIANDAY(Check_Out_Date)) AS Total_Rent_Days, COUNT(Rental_Number) AS Times_Rented FROM Rental_Order WHERE Robot_Serial_Number IS NOT NULL AND Check_Out_Date IS NOT NULL AND Return_Date IS NOT NULL GROUP BY Robot_Serial_Number ORDER BY Total_Rent_Days DESC LIMIT 1";
                SQL.ps_findMostUsedRobot(sql2);
                break;
            case "3":
                String sql3 = "SELECT M.Manufacturer_Name FROM Manufacturer AS M JOIN Model AS MO ON M.Manufacturer_Name = MO.Manufacturer_Name JOIN Robot AS R ON MO.Model_Number = R.Robot_Model_Number JOIN Rental_Order AS RO ON R.Robot_Serial_Number = RO.Robot_Serial_Number GROUP BY M.Manufacturer_Name ORDER BY COUNT(RO.Rental_Number) DESC LIMIT 1;";
                SQL.ps_findMostPopularManufacturer(sql3);
                break;
            case "4":
                String sql4 = "SELECT DC.DC_Serial_Number, DC.License_Plate, SUM(C.Facility_Distance) AS Total_Miles_Driven, COUNT(RO.Robot_Serial_Number) AS Total_Robots_Delivered FROM Driverless_Car AS DC JOIN Rental_Order RO ON DC.DC_Serial_Number = RO.DC_Serial_Number JOIN Customer C ON RO.Customer_SSN = C.Customer_SSN GROUP BY DC.DC_Serial_Number, DC.License_Plate ORDER BY Total_Robots_Delivered DESC LIMIT 1;";
                SQL.ps_findMostUsedDriverlessCar(sql4);
                break;
            case "5":
                String sql5 = "SELECT Customer_SSN, COUNT(Robot_Serial_Number) AS Robot_Serial_Number_Count FROM Rental_Order GROUP BY Customer_SSN ORDER BY Robot_Serial_Number_Count DESC LIMIT 1";
                SQL.ps_findCustomerWithMostRents(sql5);
                break;
            case "6":
                System.out.println("Enter manufacturer: ");
                String manufacturer = scanner.nextLine().trim();
                System.out.print("Enter year: ");
                int year = Integer.parseInt(scanner.nextLine());
                String sql6 = "SELECT Name FROM Manufacturer NATURAL JOIN Model WHERE Manufacturer_Name = ? AND Year < ?";
                SQL.ps_listRobotsByManufacturer(sql6, manufacturer, year);
                break;
            case "7":
                return; // Go back to main menu
            default:
                System.out.println("Invalid option. Try again.");
        }
    }

}
