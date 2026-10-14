import java.time.LocalDate;

public class Robot {
    private String serialNumber;    //pk
    private String status;
    private String warehouseLocation;
    private LocalDate warrantyExpiration;

    public Robot(String serialNumber, String status, String warehouseLocation, LocalDate warrantyExpiration) {
        this.serialNumber = serialNumber;
        this.status = status;
        this.warehouseLocation = warehouseLocation;
        this.warrantyExpiration = warrantyExpiration;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getStatus() {
        return status;
    }

    public String getWarehouseLocation() {
        return warehouseLocation;
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

    public void setWarehouseLocation(String warehouseLocation) {
        this.warehouseLocation = warehouseLocation;
    }

    public void setWarrantyExpiration(LocalDate warrantyExpiration) {
        this.warrantyExpiration = warrantyExpiration;
    }

    @Override
    public String toString() {
        return "Robot{" +
                "serialNumber='" + serialNumber + '\'' +
                ", status='" + status + '\'' +
                ", warehouseLocation='" + warehouseLocation + '\'' +
                ", warrantyExpiration=" + warrantyExpiration +
                '}';
    }
}
