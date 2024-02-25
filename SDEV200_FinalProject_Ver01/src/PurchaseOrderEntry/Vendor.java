/* Define the standard vendor class wit the following:
    - Constructors
    - Getter and setter methods
    - toString method for printing the line item instances
*/

package PurchaseOrderEntry;

public class Vendor {
    private String vendorID;
    private String vendorName;
    private String address;
    private String phone;

    // No-arg constructor
    public Vendor() {
    }

    // Constructor with parameters
    public Vendor(String vendorID, String vendorName, String address, String phone) {
        this.vendorID = vendorID;
        this.vendorName = vendorName;
        this.address = address;
        this.phone = phone;
    }

    // Define accessor and mutator methods
    public String getVendorID() {
        return vendorID;
    }

    public void setVendorID(String vendorID) {
        this.vendorID = vendorID;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Return a string representation of this object
    public String toString() {
        return "Vendor Master ID: " + vendorID +
            "\n -Vendor Name: " + vendorName +
            "\n -Address: " + address +
            "\n -Phone: " + phone;
    }
}
