/* Define the service purchase order class with the following:
    - Constructors
    - Getter and setter methods
    - toString method for printing the line item instances
*/

package PurchaseOrderEntry;

import java.time.LocalDate;

public class ServiceOrder extends PurchaseOrder {
    private String serviceType;

    // No-arg constructor
    public ServiceOrder() {
    }

    // Constructor with parameters
    public ServiceOrder(String purchaseOrderNo, LocalDate orderDate, String orderType, String serviceType, String poVendorId, String poStatus) {
        super.setPoNo(purchaseOrderNo);
        super.setOrderDate(orderDate);
        super.setOrderType(orderType);
        super.setPoVendorId(poVendorId);
        super.setPoStatus(poStatus);
        this.serviceType = serviceType;
    }

    // Define accessor and mutator methods
    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    // Return a string representation of this object
    @Override
    public String toString() {
        return "Purchase Order No.: " + super.getPoNo() +
            "\n -Order Date: " + super.getOrderDate() +
            "\n -Order Type: " + super.getOrderType() +
            "\n -Order Total: " + String.format("%.2f", super.getPoTotal()) +
            "\n -Service Type: " + serviceType +
            "\n -PO Vendor: " + super.getPoVendorId() +
            "\n -PO Status: " + super.getPoStatus();
    }

}
