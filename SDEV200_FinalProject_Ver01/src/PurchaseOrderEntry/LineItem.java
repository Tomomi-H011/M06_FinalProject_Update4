/* Define the PO line item class with the following:
    - Constructors
    - Getter and setter methods
    - Method for calculating each line total
    - toString method for printing the line item instances
*/

package PurchaseOrderEntry;


public class LineItem {
    private String lineNo;
    private String itemNo;
    private String itemName;
    private double unitPrice;
    private int quantity;
    private String uom;

    // No-arg constructor
    public LineItem() {
    }

    // Constructor with parameters
    public LineItem(String lineNo, String itemNo, String itemName, double unitPrice, int quantity, String uom) {
        this.lineNo = lineNo;
        this.itemNo = itemNo;
        this.itemName = itemName;
        this.unitPrice = unitPrice;
        this.quantity = quantity ;
        this.uom = uom;
    }

    // Define accessor and mutator methods
    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemID(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getUnitPrice() {
        return unitPrice;
    }
    
    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    // Calculate the line total
    public double getLineTotal() {
        return unitPrice * quantity;
    }


    // Return a string representation of this object
    public String toString() {
        return "Line No: " + lineNo + 
            "\n -Item No: " + itemNo +
            "\n -Item Name: " + itemName +
            "\n -Quantity: " + quantity +
            "\n -UOM: " + uom +
            "\n -Unit Price: $" + String.format("%.2f", unitPrice) +
            "\n -Line Total: $" + String.format("%.2f", getLineTotal());         
    }
}
