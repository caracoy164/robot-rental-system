import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class App {
    private static final Scanner scanner = new Scanner(System.in);
    private static final ArrayList<Robot> robots = new ArrayList<>();
    private static final ArrayList<RentalOrder> rentalOrders = new ArrayList<>();

    public static void main(String[] args) {
       boolean running = true;

        while (running) {
            System.out.println("\n=== Robot Rental System ===");
            System.out.println("1. Manage Robots");
            System.out.println("2. Manage Rental Orders");
            System.out.println("3. Rent a Robot");
            System.out.println("4. Return Equipment");
            System.out.println("5. Schedule Robot Delivery");
            System.out.println("6. Schedule Robot Pickup");
            System.out.println("7. Exit");
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
                    running = false;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }

        scanner.close();
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
        System.out.print("Serial number: ");
        String serialNumber = scanner.nextLine().trim();

        if (findRobot(serialNumber) != null) {
            System.out.println("A robot with that serial number already exists.");
            return;
        }

        System.out.print("Status: ");
        String status = scanner.nextLine().trim();

        System.out.print("Warehouse location: ");
        String location = scanner.nextLine().trim();

        System.out.print("Warranty expiration (YYYY-MM-DD): ");
        LocalDate warrantyExpiration;

        try {
            warrantyExpiration = LocalDate.parse(scanner.nextLine().trim());
        } catch (Exception e) {
            System.out.println("Invalid date. Robot was not added.");
            return;
        }

        Robot robot = new Robot(
            serialNumber, status, location, warrantyExpiration
        );

        robots.add(robot);
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

        Robot robot = findRobot(serialNumber);

        if (robot == null) {
            System.out.println("Robot not found.");
        } else {
            System.out.println(robot);
        }
    }

    private static Robot findRobot(String serialNumber) {
        for (Robot robot : robots) {
            if (robot.getSerialNumber().equalsIgnoreCase(serialNumber)) {
                return robot;
            }
        }

        return null;
    }

    private static void editRobot() {
        System.out.print("Enter serial number of robot to edit: ");
        String serialNumber = scanner.nextLine().trim();

        Robot robot = findRobot(serialNumber);

        if (robot == null) {
            System.out.println("Robot not found.");
            return;
        }

        System.out.print("New status (press Enter to keep current): ");
        String status = scanner.nextLine().trim();
        if (!status.isEmpty()) {
            robot.setStatus(status);
        }

        System.out.print("New warehouse location (press Enter to keep current): ");
        String location = scanner.nextLine().trim();
        if (!location.isEmpty()) {
            robot.setWarehouseLocation(location);
        }

        System.out.print("New warranty expiration YYYY-MM-DD (press Enter to keep current): ");
        String dateInput = scanner.nextLine().trim();

        if (!dateInput.isEmpty()) {
            try {
                robot.setWarrantyExpiration(LocalDate.parse(dateInput));
            } catch (Exception e) {
                System.out.println("Invalid date. Other changes were saved, but the date was unchanged.");
            }
        }

        System.out.println("Robot updated.");
    }

    private static void deleteRobot() {
        System.out.print("Enter serial number of robot to delete: ");
        String serialNumber = scanner.nextLine().trim();

        Robot robot = findRobot(serialNumber);

        if (robot == null) {
            System.out.println("Robot not found.");
            return;
        }

        // Prevent deleting a robot referenced by a rental order.
        for (RentalOrder order : rentalOrders) {
            if (order.getRobotSerialNumber().equalsIgnoreCase(serialNumber)) {
                System.out.println("Cannot delete this robot because a rental order references it.");
                return;
            }
        }

        robots.remove(robot);
        System.out.println("Robot deleted.");
    }


    private static void addRentalOrder() {
        int rentalNumber;

        try {
            System.out.print("Rental number: ");
            rentalNumber = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid rental number.");
            return;
        }

        if (findRentalOrder(rentalNumber) != null) {
            System.out.println("That rental number already exists.");
            return;
        }

        System.out.print("Robot serial number: ");
        String robotSerialNumber = scanner.nextLine().trim();

        // Validate the foreign key.
        if (findRobot(robotSerialNumber) == null) {
            System.out.println("Robot not found. Rental order was not added.");
            return;
        }

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

        RentalOrder order = new RentalOrder(
            rentalNumber,
            robotSerialNumber,
            checkOutDate,
            dueDate,
            returnDate,
            status,
            fees
        );

        rentalOrders.add(order);
        System.out.println("Rental order added successfully.");
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

        RentalOrder order = findRentalOrder(rentalNumber);

        if (order == null) {
            System.out.println("Rental order not found.");
        } else {
            System.out.println(order);
        }
    }

    private static RentalOrder findRentalOrder(int rentalNumber) {
        for (RentalOrder order : rentalOrders) {
            if (order.getRentalNumber() == rentalNumber) {
                return order;
            }
        }

        return null;
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

        RentalOrder order = findRentalOrder(rentalNumber);

        if (order == null) {
            System.out.println("Rental order not found.");
            return;
        }

        System.out.println("Press Enter to keep the current value.");

        System.out.print("New checkout date (" + order.getCheckOutDate() + "): ");
        String checkoutInput = scanner.nextLine().trim();
        LocalDate newCheckout = order.getCheckOutDate();

        if (!checkoutInput.isEmpty()) {
            try {
                newCheckout = LocalDate.parse(checkoutInput);
            } catch (Exception e) {
                System.out.println("Invalid checkout date. No changes saved.");
                return;
            }
        }

        System.out.print("New due date (" + order.getDueDate() + "): ");
        String dueInput = scanner.nextLine().trim();
        LocalDate newDue = order.getDueDate();

        if (!dueInput.isEmpty()) {
            try {
                newDue = LocalDate.parse(dueInput);
            } catch (Exception e) {
                System.out.println("Invalid due date. No changes saved.");
                return;
            }
        }

        if (newDue.isBefore(newCheckout)) {
            System.out.println("Due date cannot be before checkout. No changes saved.");
            return;
        }

        System.out.print("New return date (" + order.getReturnDate()
                + ", type NONE to clear): ");
        String returnInput = scanner.nextLine().trim();
        LocalDate newReturn = order.getReturnDate();

        if (returnInput.equalsIgnoreCase("NONE")) {
            newReturn = null;
        } else if (!returnInput.isEmpty()) {
            try {
                newReturn = LocalDate.parse(returnInput);
            } catch (Exception e) {
                System.out.println("Invalid return date. No changes saved.");
                return;
            }
        }

        if (newReturn != null && newReturn.isBefore(newCheckout)) {
            System.out.println("Return date cannot be before checkout. No changes saved.");
            return;
        }

        System.out.print("New status (" + order.getStatus() + "): ");
        String newStatus = scanner.nextLine().trim();

        System.out.print("New fees (" + order.getFees() + "): ");
        String feesInput = scanner.nextLine().trim();
        double newFees = order.getFees();

        if (!feesInput.isEmpty()) {
            try {
                newFees = Double.parseDouble(feesInput);

                if (!Double.isFinite(newFees) || newFees < 0) {
                    System.out.println("Fees must be nonnegative. No changes saved.");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid fees. No changes saved.");
                return;
            }
        }

        // Apply changes only after all inputs have been validated.
        order.setCheckOutDate(newCheckout);
        order.setDueDate(newDue);
        order.setReturnDate(newReturn);

        if (!newStatus.isEmpty()) {
            order.setStatus(newStatus);
        }

        order.setFees(newFees);

        System.out.println("Rental order updated successfully.");
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

        RentalOrder order = findRentalOrder(rentalNumber);

        if (order == null) {
            System.out.println("Rental order not found.");
            return;
        }

        rentalOrders.remove(order);
        System.out.println("Rental order deleted successfully.");
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

        System.out.print("Enter customer ID: ");
        String customerId = scanner.nextLine();

        System.out.print("Enter robot serial number: ");
        String serialNumber = scanner.nextLine();

        System.out.print("Enter checkout date (YYYY-MM-DD): ");
        String checkoutDate = scanner.nextLine();

        System.out.print("Enter due date (YYYY-MM-DD): ");
        String dueDate = scanner.nextLine();

        System.out.println("Robot rental information entered successfully.");
    }

    private static void returnEquipment() {
        System.out.println("\n=== Return Equipment ===");

        System.out.print("Enter rental number: ");
        String rentalNumber = scanner.nextLine();

        System.out.print("Enter robot serial number: ");
        String serialNumber = scanner.nextLine();

        System.out.print("Enter return date (YYYY-MM-DD): ");
        String returnDate = scanner.nextLine();

        System.out.println("Equipment return information entered successfully.");
    }

    private static void scheduleDelivery() {
        System.out.println("\n=== Schedule Robot Delivery ===");

        System.out.print("Enter rental number: ");
        String rentalNumber = scanner.nextLine();

        System.out.print("Enter robot serial number: ");
        String serialNumber = scanner.nextLine();

        System.out.print("Enter delivery address: ");
        String address = scanner.nextLine();

        System.out.print("Enter delivery date (YYYY-MM-DD): ");
        String deliveryDate = scanner.nextLine();

        System.out.print("Enter driverless car ID: ");
        String carId = scanner.nextLine();

        System.out.println("Delivery information entered correctly.");
    }

    private static void schedulePickup() {
        System.out.println("\n=== Schedule Robot Pickup ===");

        System.out.print("Enter rental number: ");
        String rentalNumber = scanner.nextLine();

        System.out.print("Enter robot serial number: ");
        String serialNumber = scanner.nextLine();

        System.out.print("Enter pickup address: ");
        String address = scanner.nextLine();

        System.out.print("Enter pickup date (YYYY-MM-DD): ");
        String pickupDate = scanner.nextLine();

        System.out.print("Enter driverless car ID: ");
        String carId = scanner.nextLine();

        System.out.println("Robot pickup information entered correctly.");
    }




}
