import java.time.LocalDate;

public class RentalOrder {
    private int rentalNumber;   // pk
    private String robotSerialNumber;   // fk
    private LocalDate checkOutDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private String status;
    private double fees;

    public RentalOrder(int rentalNumber, String robotSerialNumber,
                       LocalDate checkOutDate, LocalDate dueDate,
                       LocalDate returnDate, String status, double fees) {
        this.rentalNumber = rentalNumber;
        this.robotSerialNumber = robotSerialNumber;
        this.checkOutDate = checkOutDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status;
        this.fees = fees;
    }

    public int getRentalNumber() {
        return rentalNumber;
    }

    public String getRobotSerialNumber() {
        return robotSerialNumber;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public String getStatus() {
        return status;
    }

    public double getFees() {
        return fees;
    }

    // not sure if this option should be available for the primary key
    public void setRentalNumber(int rentalNumber){
        this.rentalNumber = rentalNumber;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setFees(double fees) {
        this.fees = fees;
    }

    @Override
    public String toString() {
        return "RentalOrder{" +
                "rentalNumber=" + rentalNumber +
                ", robotSerialNumber='" + robotSerialNumber + '\'' +
                ", checkOutDate=" + checkOutDate +
                ", dueDate=" + dueDate +
                ", returnDate=" + returnDate +
                ", status='" + status + '\'' +
                ", fees=" + fees +
                '}';
    }
}
