package PurchaseOrderEntry;

import java.sql.*;
import java.util.Arrays;
import java.util.List;

import com.mysql.cj.xdevapi.Table;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Database {
    private static final String url = "jdbc:mysql://localhost:3306/javabook";
    private static final String user = "root";
    private static final String password = "";
    private static String tableName = "";
    private static Connection connection;
    private static Statement stmt;
    // private static final String tableNamePr = "tblOpenPR";
    // private static final String tableNameVendor = "tblVendor";
    // private static final String tableNamePo = "tblIssuedPo";
    // private static final String tableNameItem = "tblItem";


    // Create and populate a table
    static void initializeDB() {
        try {
            //Load the JDBC driver
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver loaded");
            
            // Establish a connection
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Database connected");

            // Create a statement
            stmt = connection.createStatement();

            // List of table names
            // List<String> tableNamesList = Arrays.asList("tblVendor", "tblPoHeader", "tblPoItem");
            List<String> dropTableNamesList = Arrays.asList("tblPoItem", "tblPoHeader", "tblVendor");

            // Loop through table names and drop tables
            for (String tableName : dropTableNamesList) {
                checkAndDropTables(stmt, tableName);
            }      
            
            // List of table names
            List<String> createTableNamesList = Arrays.asList("tblVendor", "tblPoHeader", "tblPoItem");
   
            // Loop through table names and setup tables
            for (String tableName : createTableNamesList) {
                setupTables(stmt, tableName);
            } 
           

        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    // Check for table and drop if exists
    public static void checkAndDropTables(Statement stmt, String tableName) {
        try{
            // Check if table exists
            if (checkTableExists(stmt, tableName)) {
                // Drop StaffTable if it exists
                    dropTable(stmt, tableName);
                }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    // Set up tables
    public static void setupTables(Statement stmt, String tableName) {
        try{
            // Check if table exists
            if (checkTableExists(stmt, tableName)) {
                // Drop StaffTable if it exists
                    dropTable(stmt, tableName);
                }
            else {
                //Create table
                createTable(stmt, tableName, getCreateTableString(tableName));
            }

            // Insert data into table
            insertData(stmt, tableName, getInsertDataString(tableName));

        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    // Check if table exists
    public static boolean checkTableExists(Statement stmt, String tableName) throws SQLException {
        try (ResultSet resultSet = connection.getMetaData().getTables(null, null, tableName, null)) {
            return resultSet.next();
        }
    }

    // Drop table if exists
    public static void dropTable(Statement stmt, String tableName) throws SQLException {
        String dropTableStmt = "DROP TABLE " + tableName;
        stmt.executeUpdate(dropTableStmt);
        System.out.println(tableName + " dropped");
    }
    
    // Create a table
    private static void createTable(Statement stmt, String tableName, String CreateTableString) throws SQLException {
        String createTableStmt = "CREATE TABLE " + tableName + CreateTableString;
        
        stmt.executeUpdate(createTableStmt);
        System.out.println(tableName + " created");
    }    

    // Insert initial data into table
    private static void insertData(Statement stmt, String tableName, String InsertDataString) throws SQLException {
        String insertDataStmt = "INSERT INTO " + tableName +  InsertDataString;
        stmt.executeUpdate(insertDataStmt);
        System.out.println("Data inserted into " + tableName);  
    }

    // Get String for creating tables
    private static String getCreateTableString(String tableName) {
        switch (tableName) {
            case "tblVendor":
                return " ("
                + "vendorId CHAR(6) NOT NULL,"
                + "vendorName VARCHAR(35),"
                + "address VARCHAR(50),"
                + "phone CHAR(12),"
                + "PRIMARY KEY (vendorId)"
                + ")";
            case "tblPoHeader":
                return " ("
                + "poNo CHAR(9) NOT NULL,"
                + "poOrderDate DATE,"
                + "poOrderType VARCHAR(10), "
                + "poOrderChategory VARCHAR(20), "
                + "poVendorId CHAR(6),"
                + "poStatus VARCHAR(10),"
                + "FOREIGN KEY (poVendorId) REFERENCES tblVendor(vendorId),"
                + "PRIMARY KEY (poNo)"
                + ")";               
            case "tblPoItem":
                return "("
                + "poNo CHAR (9) NOT NULL,"
                + "poLineNo CHAR(3) NOT NULL,"
                + "poItemNo CHAR(7),"
                + "poItemName VARCHAR(80),"
                + "poQuantity INTEGER,"
                + "poUnitPrice DECIMAL(10, 2),"
                + "poUom CHAR(2),"
                + "poLineTotal DECIMAL(10, 2),"
                + "FOREIGN KEY (poNo) REFERENCES tblPoHeader(poNo),"
                + "PRIMARY KEY (poNo, poLineNo)"
                + ")";
            default:
                return "";
        }
    }

    // Get String for creating tables
    private static String getInsertDataString(String tableName) {
        switch (tableName) {
            case "tblVendor":
                return " ("
                + "vendorId, vendorName, address, phone) "
                + "VALUES "
                + "('200001', 'ABC Auto Manufacturing, Inc.', '100 First Ave, Chicago, IL 60007', '630-986-5436'), "
                + "('200002', 'XYZ Electronic Services, Inc.', 'PO Box 222 New York, NY 10027', '212-123-4444'), "
                + "('200003', 'Johnson Advertisement, Inc.', 'PO Box 456 New York, NY 10027', '212-123-5555'), "
                + "('200004', 'New York Law Office, LLC', '1800 Broadway St, New York, NY 10027', '212-123-6666'), "
                + "('200005', 'TM Transmission Systems, Inc.', '1212 Industrial Pkwy, San Antonio, TX 78205', '210-735-6543'), "
                + "('200006', 'Uniform Distribution, Inc.', 'PO Box 7891 Indianapolis, IN 46201', '317-545-3000')";

            case "tblPoHeader":
                return " ("
                + "poNo, poOrderDate, poOrderType, poOrderChategory, poVendorId, poStatus) "
                + "VALUES "
                + "('450000031', '2024-02-21' , 'Standard', 'Parts', '200001', 'Pending'), "
                + "('450000032', '2024-02-21' , 'Service', 'Maintenance', '200002', 'Pending'), "
                + "('450000033', '2024-01-12' , 'Standard', 'Uniform', '200006', 'Issued'), "
                + "('450000034', '2024-02-02' , 'Service', 'Consultation', '200004', 'Issued')";   

            case "tblPoItem":
                return "("
                 + "poNo, poLineNo, poItemNo, poItemName, poQuantity, poUnitPrice, poUom, poLineTotal) "
                + "VALUES "
                + "('450000031', '001', '8000198' , 'Rear Brake Pedal-A Unit', '5', '178.98', 'EA', '894.90'), "
                + "('450000031', '002', '8000123' , 'Brake Line kit', '5', '50.79', 'EA', '253.95'), "
                + "('450000031', '003', '8000024' , 'ABS Hydraulic Unit', '2', '250.00', 'EA', '500.00'), "
                + "('450000032', '001', '8000000' , 'Duct cleaning - Labor', '1', '1500.00', 'AU', '1500.00'), "
                + "('450000032', '002', '8000000' , 'Air conditioning unit repair - Labor', '1', '500.00', 'AU', '500.00'), "
                + "('450000033', '001', '8000000' , 'Long sleeve shirt - Women M', '20', '23.98', 'EA', '539.60'), "
                + "('450000033', '002', '8000000' , 'Long sleeve shirt - Women M', '20', '23.98', 'EA', '539.60'), "
                + "('450000033', '003', '8000000' , 'Short sleeve shirt - Men M', '20', '13.98', 'EA', '279.60'), "
                + "('450000034', '001', '8000000' , 'Legal consultation', '1', '7500.00', 'AU', '7500.00')";
            default:
                return "";
        }
    }   


    // Called by btnPoList and cboPoStatus. Return PO headers as an ObservableList.
    public static ObservableList<ObservableList<String>> queryPoHeader(String selectedPoStatus) {

        ObservableList<ObservableList<String>> dataPoHeader = FXCollections.observableArrayList();
        
        try {
            String queryPoHeaderString;
            PreparedStatement pstmtQueryPoHeader;

            if (selectedPoStatus.isEmpty()) {
                queryPoHeaderString = "SELECT * FROM tblPoHeader";  // For btnPoList
                pstmtQueryPoHeader = connection.prepareStatement(queryPoHeaderString);
            }
            else {
                queryPoHeaderString = "SELECT * FROM tblPoHeader WHERE poStatus = ?";  // For cboPoStatus
                pstmtQueryPoHeader = connection.prepareStatement(queryPoHeaderString);
                pstmtQueryPoHeader.setString(1, selectedPoStatus);
            }

            ResultSet queryPoHeaderReultSet = pstmtQueryPoHeader.executeQuery();

        // Get the metadata of the ResultSet to get the column count
        ResultSetMetaData metaData = queryPoHeaderReultSet.getMetaData();
        int columnCount = metaData.getColumnCount();  // Return the column count of result set

        while (queryPoHeaderReultSet.next()) {
            // Create a new ObservableList to store row data
            ObservableList<String> row = FXCollections.observableArrayList();

            // Iterate through columns and add data to the row
            for (int i = 1; i <= columnCount; i++) {
                row.add(queryPoHeaderReultSet.getString(i));
            }

            // Add the row to the main data list
            dataPoHeader.add(row);
        }

        return dataPoHeader;

        }
        
        catch (Exception e) {
            e.printStackTrace();
        }

        return dataPoHeader;
    }
   
    // Get information from tblPoItem
    public static ObservableList<ObservableList<String>> queryPoItem(String poNo) {
        
        // Create a ObservableList to hold row data
        ObservableList<ObservableList<String>> dataPoItem = FXCollections.observableArrayList();
        
        try {
            String queryPoItemString = "(SELECT * FROM tblPoItem WHERE poNo = "  + poNo + ")";

            ResultSet queryPoItemReultSet = stmt.executeQuery(queryPoItemString);
        
            // Get the metadata of the ResultSet to get the column count
            ResultSetMetaData metaData = queryPoItemReultSet.getMetaData();
            int columnCount = metaData.getColumnCount();  // Return the column count of result set

            while (queryPoItemReultSet.next()) {
                // Create a new ObservableList to store row data
                ObservableList<String> row = FXCollections.observableArrayList();
    
                // Iterate through columns and add data to the row
                for (int i = 1; i <= columnCount; i++) {
                    row.add(queryPoItemReultSet.getString(i));
                }
    
                // Add the row to the main data list
                dataPoItem.add(row);
            }
            return dataPoItem;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    return dataPoItem;
    }


    // Get information from tblVendor. Return only one row.
    public static ObservableList<String> queryPoVendor(String poVendorId) {
        
        // Create a ObservableList to hold row data
        ObservableList<String> dataPoVendor = FXCollections.observableArrayList();
        
        try {
            String queryPoVendorString = "SELECT * FROM tblVendor WHERE vendorId = "  + poVendorId;

            ResultSet queryPoVendorReultSet = stmt.executeQuery(queryPoVendorString);
        
            // Get the metadata of the ResultSet to get the column count
            ResultSetMetaData metaData = queryPoVendorReultSet.getMetaData();
            int columnCount = metaData.getColumnCount();  // Return the column count of result set

            if (queryPoVendorReultSet.next()) {
                // Iterate through columns and add data to the row
                for (int i = 1; i <= columnCount; i++) {
                    dataPoVendor.add(queryPoVendorReultSet.getString(i));
                }
            }
            return dataPoVendor;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    return dataPoVendor;
    }

// Update the poStatus when btnIssuePo is clicked
public static String updatePoStatus(String poNo) {

    String updatePoStatusString;
    PreparedStatement pstmtUpdatePoStatus;

    String checkUpdateResultString;
    PreparedStatement pstmtCheckUpdateResult;

    String updatePoStatusResult = "";

    updatePoStatusString = "UPDATE tblPoHeader SET poStatus = 'Issued' WHERE poNo = ?";
    
    try {
        pstmtUpdatePoStatus = connection.prepareStatement(updatePoStatusString);
        pstmtUpdatePoStatus.setString(1, poNo );
        pstmtUpdatePoStatus.executeUpdate();

        // check if the poStatus was changed to 'Issued'
        checkUpdateResultString = "SELECT * FROM tblPoHeader WHERE poNo = ?";
        pstmtCheckUpdateResult = connection.prepareStatement(checkUpdateResultString);
        pstmtCheckUpdateResult.setString(1, poNo);

        try (ResultSet updatedPoHeaderResultSet = pstmtCheckUpdateResult.executeQuery()) {
            if (updatedPoHeaderResultSet.next()) {
                String updatedPoStatus = updatedPoHeaderResultSet.getString("poStatus");
                if ("Issued".equals(updatedPoStatus)) {
                    System.out.println("poStatus changed to 'Issued'");
                    return updatePoStatusResult = "Updated";
                }
            } 
            else {
                System.out.println("No rows found for poNo: " + poNo);
                return null;
            }
        }
    }
    catch (SQLException e) {
        e.printStackTrace();
    }

    return updatePoStatusResult;

}

// Update the quantity and line total when btnIssuePo is clicked
public static boolean updateLineItem(String poNo, String poLineNo, String Quantity, String LineTotal) {

    String updateLineItemString;
    PreparedStatement pstmtUpdateLineItem;

    // String checkUpdateResultString;
    // PreparedStatement pstmtCheckUpdateResult;

    String updateLineItemResult = "";

    updateLineItemString = "UPDATE tblPoItem SET poQuantity = ?, poLineTotal = ? WHERE poNo = ? AND poLineNo = ?";
    
    
    try {
        pstmtUpdateLineItem = connection.prepareStatement(updateLineItemString);
        // Set paramaters
        pstmtUpdateLineItem.setString(1, Quantity );
        pstmtUpdateLineItem.setString(2, LineTotal);
        pstmtUpdateLineItem.setString(3, poNo);
        pstmtUpdateLineItem.setString(4, poLineNo);

        // Execute the update and check if the update was successful
        int rowsAffected = pstmtUpdateLineItem.executeUpdate();
        return rowsAffected > 0;

    }
    catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}


}
