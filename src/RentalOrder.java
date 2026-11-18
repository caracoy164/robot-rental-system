import java.time.LocalDate;

public class RentalOrder {
    private int rentalNumber;   // pk
    private String customerSSn; //fk
    private String robotSerialNumber;   // fk
    private String dcSerialNumberPickUp; //fk
    private String dcSerialNumberDropOff; //fk
    private LocalDate checkOutDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private String status;
    private double fees;

    public RentalOrder(int rentalNumber, String ssn, String robotSerialNumber, 
        String dcSerialNumberPickUp, String dcSerialNumberDropOff, LocalDate checkOutDate, LocalDate dueDate, 
        LocalDate returnDate, String status, double fees) {
        this.rentalNumber = rentalNumber;
        this.customerSSn=ssn;
        this.robotSerialNumber = robotSerialNumber;
        this.dcSerialNumberPickUp=dcSerialNumberPickUp;
        this.dcSerialNumberDropOff=dcSerialNumberDropOff;
        this.checkOutDate = checkOutDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status;
        this.fees = fees;
    }

    public int getRentalNumber() {
        return rentalNumber;
    }

    public String getCustomerSSn() {
        return customerSSn;
    }
    public String getRobotSerialNumber() {
        return robotSerialNumber;
    }

    public String getdcSerialNumberPickUp() {
        return dcSerialNumberPickUp;
    }

    public String getdcSerialNumberDropOff() {
        return dcSerialNumberDropOff;
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
    public void setCustomerSSn(String ssn){
        this.customerSSn=ssn;
    }

    public void setRobotSerialNumber(String robotSerialNumber) {
        this.robotSerialNumber = robotSerialNumber;
    }

    public void setdcSerialNumberPickUp(String dcSerialNumber) {
        this.dcSerialNumberPickUp = dcSerialNumber;
    }

    public void setdcSerialNumberDropOff(String dcSerialNumber) {
        this.dcSerialNumberDropOff = dcSerialNumber;
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
                "CustomerSSn =" + customerSSn +
                ", robotSerialNumber='" + robotSerialNumber + '\'' +
                ", dcSerialNumber pick up ='" + dcSerialNumberPickUp + '\'' +
                ", dcSerialNumber drop off ='" + dcSerialNumberDropOff + '\'' +
                ", checkOutDate=" + checkOutDate +
                ", dueDate=" + dueDate +
                ", returnDate=" + returnDate +
                ", status='" + status + '\'' +
                ", fees=" + fees +
                '}';
    }
}
