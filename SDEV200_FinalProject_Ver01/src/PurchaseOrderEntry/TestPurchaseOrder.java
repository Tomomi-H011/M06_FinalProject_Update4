/**
* Assignment: SDEV200_M05_FinalProject_Update3
* File: TestPurchaseOrder.java
* Version: 1.0
* Date: 2/17/2024
* Author: Tomomi Hobara
* Description: This program creates and prints instances of Standard PO, Service PO, Vendors, and line items. 
* Variables: 
    - currentDate: LocalDate for issuing PO with today's date
* Steps:
     1. Create instances of all the classes
     2. Get the total of line items for each PO
     3. Print the instances in the console
* Note:
    - Standard PO is for purchasing automotive parts for the dealership business and 
        other physical items such as office supplies and uniforms.
    - Service PO is for ordering services such as housekeeping, maintenance, and advertisment services.    
*/

package PurchaseOrderEntry;

import java.time.LocalDate;

public class TestPurchaseOrder {
    public static void main(String[] args) {
        // Get the current date
        LocalDate currentDate = LocalDate.now();

        // Create instances for each class
        // Order 1
        StandardOrder stPo4500001 = new StandardOrder("4500001", currentDate, "ST", "Parts", "200001", "Pending");
        
        LineItem item45000010001 = new LineItem("45000010001","8000001", "Front Break Unit", 512.34, 4, "EA");
        stPo4500001.addLineTotals(item45000010001);  // Pass the item instance to getPoTotal method

        LineItem item45000010002 = new LineItem("45000010002","8000034", "Cabin Air Filter", 74.56, 10, "EA");
        stPo4500001.addLineTotals(item45000010002);

        Vendor PoVendor4500001 = new Vendor("200001", "ABC Auto Manufacturing, Inc.", "100 First Ave, Chicago, IL 60007", "630-986-543");
        
        // Print the instances
        System.out.println("------------------------------");
        System.out.println(stPo4500001.toString());
        System.out.println(item45000010001.toString());
        System.out.println(item45000010002.toString());
        System.out.println(PoVendor4500001);

        // Order 2
        // Create instances for each class
        ServiceOrder srPo4500002 = new ServiceOrder("4500002", currentDate, "SR", "Maintenance", "200002", "Issued");
        
        LineItem item45000020001 = new LineItem("45000020001","9000001", "Install air conditioning unit - Labor", 300.00, 1, "AU");
        srPo4500002.addLineTotals(item45000020001);  // Pass the item instance to getPoTotal method
        
        LineItem item45000020002 = new LineItem("45000020002","9000034", "Inspect heating unit - Labor", 100.11, 1, "AU");
        srPo4500002.addLineTotals(item45000020002);  // Pass the item instance to getPoTotal method
        Vendor PoVendor4500002 = new Vendor("200002", "XYZ Electronic Services, Inc.", "PO Box 222 New York, NY 10027", "212-123-4444");

        // Print the instances
        System.out.println("------------------------------");
        System.out.println(srPo4500002.toString());
        System.out.println(item45000020001.toString()); 
        System.out.println(item45000020002.toString()); 
        System.out.println(PoVendor4500002);
    }
}
