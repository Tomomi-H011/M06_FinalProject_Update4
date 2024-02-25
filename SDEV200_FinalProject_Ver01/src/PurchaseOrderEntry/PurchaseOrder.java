/* Define the superclass (purchase order class) with the following:
    - Constructors
    - Getter and setter methods
    - toString method for printing the line item instances
    - A list to store line item totals
    - A method to add line item totals to the list
    - A method to sum the line totals and return a PO total
*/

package PurchaseOrderEntry;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public abstract class PurchaseOrder {
    
    private String poNo = "";
    private LocalDate orderDate = LocalDate.now();  // Assign today's date as default
    private String orderType = "";
    private String poVendorId = "";
    private String poStatus = "";

    // No-arg constructor
    protected PurchaseOrder() {
    }

    // Constructor with parameters
    protected PurchaseOrder(String poNo, LocalDate orderDate, String orderType, String poVendorId, String poStatus) {
        this.poNo = poNo;
        this.orderDate = orderDate;
        this.orderType = orderType;
        this.poVendorId = poVendorId;
        this.poStatus = poStatus;
    }

    // Define accessor and mutator methods
    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getPoVendorId() {
        return poVendorId;
    }

    public void setPoVendorId(String poVendorId){
        this.poVendorId = poVendorId;
    }

    public String getPoStatus() {
        return poStatus;
    }

    public void setPoStatus(String poStatus){
        this.poStatus = poStatus;
    }

    // Return a string representation of this object
    public String toString() {
        // Define the date format as mm/dd/yyyy
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        
        return "\nPurchase Order No.: " + poNo +
            "\nOrder Date: " + orderDate.format(formatter)  +
            "\nOrder Type: " + orderType +
            "\nOrder Total: " + String.format("%.2f", getPoTotal()) +
            "\n PO Vendor ID: " + poVendorId +
            "\n PO Status: " + poStatus;

    }

    // Create a list to store line items
    public List<Double> lineTotalsList = new ArrayList<>();

    // Add a line total to the list
    public void addLineTotals(LineItem lineItem) {
        lineTotalsList.add(lineItem.getLineTotal());
    }
    
    // Get the total of all lineTotals
    public Double getPoTotal() {
        double total = 0.00;

        // Iterate over line totals in lineTotalsList
        for (Double lineTotal : lineTotalsList) {
            total += lineTotal;
        }

        return total;
    }


}