import java.time.LocalDate;

public class Robot {
    private String serialNumber;    //pk
    private String status;
    private LocalDate warrantyExpiration;
    private String robotModelNumber; //fk

    public Robot(String serialNumber, String status, LocalDate warrantyExpiration, String modelNumber) {
        this.serialNumber = serialNumber;
        this.status = status;
        this.warrantyExpiration = warrantyExpiration;
        this.robotModelNumber= modelNumber;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getStatus() {
        return status;
    }

    public String getRobotModelNumber() {
        return robotModelNumber;
    }

    public LocalDate getWarrantyExpiration() {
        return warrantyExpiration;
    }

    // not sure if this option should be available for the primary key
    public void setSerialNumber(String serialNumber){
        this.serialNumber = serialNumber;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setrobotModelNumber(String modelNumber) {
        this.robotModelNumber = modelNumber;
    }

    public void setWarrantyExpiration(LocalDate warrantyExpiration) {
        this.warrantyExpiration = warrantyExpiration;
    }

    @Override
    public String toString() {
        return "Robot{" +
                "serialNumber='" + serialNumber + '\'' +
                ", status='" + status + '\'' +
                ", robot model number ='" + robotModelNumber + '\'' +
                ", warranty expiration=" + warrantyExpiration +
                '}';
    }
}
