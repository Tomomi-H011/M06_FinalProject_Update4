/* Define the standard purchase order class with the following:
    - Constructors
    - Getter and setter methods
    - toString method for printing the line item instances
*/

package PurchaseOrderEntry;

import java.time.LocalDate;

public class StandardOrder extends PurchaseOrder {
    private String itemType;

    // No-arg constructor
    public StandardOrder() {
    }

    // Constructor with parameters
    public StandardOrder(String poNo, LocalDate orderDate, String orderType, String itemType, String poVendor, String poStatus) {
        super.setPoNo(poNo);
        super.setOrderDate(orderDate);
        super.setOrderType(orderType);
        super.setPoVendorId(poVendor);
        super.setPoStatus(poStatus);
        this.itemType = itemType;
    }

    // Define accessor and mutator methods
    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    // Return a string representation of this object
    @Override
    public String toString() {
        return "Purchase Order No.: " + super.getPoNo() +
            "\n -Order Date: " + super.getOrderDate() +
            "\n -Order Type: " + super.getOrderType() +
            "\n -Order Total: " + String.format("%.2f", super.getPoTotal()) +
            "\n -Item Type: " + itemType +
            "\n -PO Vendor " + super.getPoVendorId() +
            "\n -PO Status: " + super.getPoStatus();

    }

}
