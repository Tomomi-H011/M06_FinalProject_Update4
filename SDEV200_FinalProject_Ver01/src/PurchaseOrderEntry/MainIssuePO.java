/**
* Assignment: SDEV200_M06_FinalProject_Update4
* File: MainIssuePO.java
* Version: 1.0
* Date: 2/24/2024
* Author: Tomomi Hobara
* Description: This program is a Purchase Order entry application for an automotive dealership. 
                It issues two types of Purchase Orders. Standandard POs are issued for ordering physical items such as 
                automotive parts, office supplies, and uniforms. Service POs are for ordering 
                services such as maintenance, housekeeping, consultation, and advertisement services.
                The program allows users to review a list of POs that are in 'Pending' or 'Issued' status.
                For pending POs, users can change the line item quantity and confirm the POs by clicking the 'Issue PO' button.
                When a PO is issued, the PO status, line item quantity, and line total are updated in the underlying MySQL database.
* Variables: 
    -tvPoHeader: TableView for displaying PO headers
    -tvLineItem: TableView for displaying PO line items. The quantity column is editable for standard POs (ordering physical items).
    -btnPoList: Button for querying the PO Header table and displaying a Tableview of POs
    -btnLogout: Button for exiting the program
    -cboPoStatus: ComboBox for filtering the PO TableView by PO status ('Pending' vs. 'Issued')
    -btnClear: Button to clear PO details displayed in the Line Item TableView and other Texts
    -btnShowPo: Button for displaying line items in a TableView form.
* Note: Remaining tasks
    - Disable the quantity column when the PO is a service PO.
    - Disable the btnIssuePo for already issued POs
    - Add checking mechanisms for user entries (ex. empty quantity columns)
    - If more functions are needed, add a function to create POs by allowing users to manually enter POs and 
        create them using class constructors.
    - Consider adding a login screen
    - Delete the lines that are used for testing purposes (System.out.println, etc.)
    - Delete unnecessary imports
    - Add more data to the tables
    - Clean up issues/warnings
    - Change variable names that do not match (Purchase Order vs PO, tcPendingPoNo, tcPendingOrderType, etc.)   
*/

package PurchaseOrderEntry;

// import java.sql.*;

import javafx.scene.control.TableColumn;
// import javafx.scene.control.CellDataFeatures; //To Do: Delete 
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
// import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
// import javafx.scene.control.PasswordField;
// import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.TableView;
// import javafx.scene.control.TextArea;
// import javafx.scene.control.TextField;
// import javafx.scene.layout.Border;
// import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
// import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Alert;

public class MainIssuePO extends Application {
    // Fields for the PO stage
    // Menu pane
    private Button btnPoList = new Button("PO List");
    private Button btnLogout = new Button("Logout");

    // PO header pane - Purchase Order class
    private Label lblPurchaseOrderNo = new Label("PO No: ");
    private Label lblOrderDate = new Label("Order Date: ");
    private Label lblOrderType = new Label("Order Type: ");
    private Label lblOrderTotal = new Label("Order Total: ");
    private Label lblPoVendorId = new Label("Vendor ID: ");
    private Label lblPoStatus = new Label("Status");
    private Label lblPoCategory = new Label("Category");
    private Text txtPoNo = new Text("");  // Data from DB
    private Text txtOrderDate = new Text("");
    private Text txtOrderType = new Text("");
    private Text txtOrderTotal = new Text("$");
    private Text txtPoVendorId = new Text("");
    private Text txtPoStatus = new Text("");
    private Text txtPoCategory = new Text("");
    // private ObservableList<String> selectedPoHeaderRow;  // For passing the queryPoHeader result

    // PO header pane - Vendor class
    private Label lblVendorName = new Label("Vendor Name: ");
    private Label lblAddress = new Label("Address: ");
    private Label lblPhone = new Label("Phone: ");
    private Text txtVendorName = new Text("");
    private Text txtAddress = new Text("");
    private Text txtPhone = new Text("");

    // Set up PO line item TableView  // To Do: Replace Object in the bracket to the actual obj type
    private TableView<ObservableList<String>> tvLineItem = new TableView<>();
    private TableColumn<ObservableList<String>, String> tcLineNo = new TableColumn<>("Line No");
    private TableColumn<ObservableList<String>, String> tcItemNo = new TableColumn<>("Item No");
    private TableColumn<ObservableList<String>, String> tcItemName = new TableColumn<>("Item Name");
    private TableColumn<ObservableList<String>, String> tcUnitPrice = new TableColumn<>("Unit Price");
    private TableColumn<ObservableList<String>, String> tcQuantity = new TableColumn<>("Quantity");
    private TableColumn<ObservableList<String>, String> tcLineTotal = new TableColumn<>("Line Total");
    private TableColumn<ObservableList<String>, String> tcUom = new TableColumn<>("UOM");

    // PO button pane 
    private Text txtUpdateResult = new Text("");
    private Button btnClear = new Button("Clear");
    private Button btnIssuePO = new Button("Issue PO");

    // Fields for the second stage (stageSelectPo)
    // PO Header Treeview
    private TableView<ObservableList<String>> tvPoHeader = new TableView<>();
    private TableColumn<ObservableList<String>, String> tcPendingPoNo = new TableColumn<>("PO No"); 
    private TableColumn<ObservableList<String>, String> tcPendingOrderType = new TableColumn<>("Order Type");
    private TableColumn<ObservableList<String>, String> tcPendingCategory = new TableColumn<>("Category");
    private TableColumn<ObservableList<String>, String> tcPendingPoVendor = new TableColumn<>("Vendor");
    private TableColumn<ObservableList<String>, String> tcPendingOrderTotal = new TableColumn<>("Total $");
    private TableColumn<ObservableList<String>, String> tcPendingPoStatus = new TableColumn<>("PO Status");
    
    // Other nodes for the second stage
    private Label lblForCboPoStatus = new Label("PO Status");
    private ComboBox<String> cboPoStatus = new ComboBox<>();
    private Button btnShowPo = new Button("Show PO");
    private Button btnClose = new Button("Close");


    public static void main(String[] args) {
        Application.launch(args);
    }

    /*Initialize the database and opens the primary stage.*/
    @Override
    public void start(Stage primaryStage) {
        //Initialize MySQL database
        Database.initializeDB();

        // Create a VBox for the menu
        VBox paneForMenu = new VBox();
        paneForMenu.setPadding(new Insets(10));
        paneForMenu.setAlignment(Pos.TOP_CENTER);
        paneForMenu.setSpacing(20);
        paneForMenu.getChildren().addAll(btnPoList, btnLogout);
        btnPoList.setPrefWidth(90);
        btnLogout.setPrefWidth(90);

        // Create a VBox for the PO header - Status
        VBox paneForPoStatus = new VBox();
        paneForPoStatus.setMinWidth(150);
        paneForPoStatus.setPadding(new Insets(10));
        paneForPoStatus.setSpacing(5);
        paneForPoStatus.setAlignment(Pos.BASELINE_LEFT);
        paneForPoStatus.getChildren().addAll(lblPoStatus, txtPoStatus, lblOrderTotal, txtOrderTotal);
        


        // Create a GridPane for the PO header - PO
        GridPane paneForPOHeaderDetail = new GridPane();
        paneForPOHeaderDetail.setMinWidth(300);
        paneForPOHeaderDetail.setPadding(new Insets(10));
        paneForPOHeaderDetail.setVgap(5);
        paneForPOHeaderDetail.setHgap(5);
        paneForPOHeaderDetail.setAlignment(Pos.BASELINE_LEFT);
        paneForPOHeaderDetail.addRow(0, lblPurchaseOrderNo, txtPoNo);
        paneForPOHeaderDetail.addRow(1, lblOrderDate, txtOrderDate);
        paneForPOHeaderDetail.addRow(2, lblOrderType, txtOrderType);
        paneForPOHeaderDetail.addRow(3, lblPoCategory, txtPoCategory);


        // Create a GridPane for the PO header - Vendor
        GridPane paneForVendor = new GridPane();
        paneForVendor.setMinWidth(350);
        paneForVendor.setPadding(new Insets(10));
        paneForVendor.setVgap(5);
        paneForVendor.setHgap(5);
        paneForVendor.setAlignment(Pos.BASELINE_LEFT);
        paneForVendor.addRow(0, lblPoVendorId, txtPoVendorId);
        paneForVendor.addRow(1, lblVendorName,txtVendorName);
        paneForVendor.addRow(2, lblAddress, txtAddress);
        paneForVendor.addRow(3, lblPhone, txtPhone);
   

        // HBox to group PO header panes
        HBox paneForPoHeader = new HBox();
        paneForPoHeader.getChildren().addAll(paneForPoStatus, paneForPOHeaderDetail, paneForVendor);

        // Pane for PO Item TreeView
        Pane paneForPOItem = new Pane();
        paneForPOItem.setPadding(new Insets(10));
        paneForPOItem.getChildren().add(tvLineItem);

        // Format the TreeView and columns
        tvLineItem.setMinHeight(400);
        tvLineItem.setMaxHeight(400);
        tcLineNo.setPrefWidth(60);
        tcItemNo.setPrefWidth(100);
        tcItemName.setPrefWidth(200);
        tcUnitPrice.setPrefWidth(100);
        tcQuantity.setPrefWidth(100);
        tcLineTotal.setPrefWidth(100);
        tcUom.setPrefWidth(50);
        
        // Clear existing columns
        tvLineItem.getColumns().clear();

        // Map the columns
        tcLineNo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(1)));
        tcItemNo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(2)));
        tcItemName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(3)));
        tcQuantity.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(4)));
        tcUnitPrice.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(5)));
        tcUom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(6)));
        tcLineTotal.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(7)));

        // Set up a TableView for the PO Item
        tvLineItem.getColumns().addAll(tcLineNo, tcItemNo, tcItemName, tcQuantity, tcUnitPrice, tcLineTotal, tcUom);
    
        // HBox for update text
        HBox paneForUpdateText = new HBox();
        paneForUpdateText.setPadding(new Insets(20));
        paneForUpdateText.setAlignment(Pos.CENTER);
        paneForUpdateText.getChildren().add(txtUpdateResult);

        // HBox for buttons at the bottom
        HBox paneForButtons = new HBox();
        paneForButtons.setPadding(new Insets(10));
        paneForButtons.setSpacing(15);
        paneForButtons.setAlignment(Pos.BASELINE_CENTER);
        paneForButtons.getChildren().addAll(btnClear, btnIssuePO);

        // VBox to combine PO header, Items, and button panes
        VBox paneForPO = new VBox();
        paneForPO.getChildren().addAll(paneForPoHeader, paneForPOItem, paneForUpdateText, paneForButtons);

        // Create a Hbox to place all the panes
        HBox root = new HBox();
        root.getChildren().addAll(paneForMenu, paneForPO);
       

        // Create a scene and place it in the stage
        Scene scene = new Scene(root, 900, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Purchase Order Entry");
        primaryStage.show();

        // Set up event handlers
        btnPoList.setOnAction(e -> {
            // Reset txtUpdateResult
            txtUpdateResult.setText("");

            // Reset cboPoStatus before opening the selectPoStage
            cboPoStatus.getSelectionModel().clearSelection();
            cboPoStatus.setValue("");

            selectPoStage();  // Open the TableView of pending POs
            fillTableViewPOHeader(tvPoHeader);  // Fill the TableView with pending POs
        });

        // Exit the program when btnLogout is clicked
        btnLogout.setOnAction(e -> {
            System.exit(0);
        });
    }
    
    // Create the stage for selecting and opening a PO detail
    private void selectPoStage() {

        //Pane for cboPoStatus
        GridPane paneForPoStatus = new GridPane();
        paneForPoStatus.setPadding(new Insets(20, 10, 40, 30));
        paneForPoStatus.setAlignment(Pos.BASELINE_LEFT);
        paneForPoStatus.setHgap(5);
        paneForPoStatus.setVgap(5);
        paneForPoStatus.addRow(0, lblForCboPoStatus);
        paneForPoStatus.addRow(1, cboPoStatus);
        cboPoStatus.setPrefWidth(120);
        cboPoStatus.getItems().clear();  // Clear existing items in the ComboBox
        cboPoStatus.getItems().addAll("Pending", "Issued");  // Add values to the ComboBox
        cboPoStatus.setValue("");  // Set default

        // System.out.println("PoStatus when selectPoStage is open: " + cboPoStatus.getSelectionModel().getSelectedItem());  // To Do: Testing, delete later

        // Pane for pendingPO TableView
        HBox paneForPoHeader = new HBox();
        paneForPoHeader.setPadding(new Insets(10));
        paneForPoHeader.setAlignment(Pos.CENTER);
        paneForPoHeader.getChildren().add(tvPoHeader);

        // Format the TableView and columns
        tvPoHeader.setMinHeight(400);
        tvPoHeader.setMaxHeight(400);
        tcPendingPoNo.setPrefWidth(80);
        tcPendingOrderType.setPrefWidth(80);
        tcPendingCategory.setPrefWidth(130);
        tcPendingPoVendor.setPrefWidth(140);
        tcPendingOrderTotal.setPrefWidth(130);
        tcPendingPoStatus.setPrefWidth(90);

        //Pane for Buttons
        HBox paneForPendingPoButtons = new HBox();
        paneForPendingPoButtons.setPadding(new Insets(10));
        paneForPendingPoButtons.setSpacing(10);
        paneForPendingPoButtons.setAlignment(Pos.CENTER);
        paneForPendingPoButtons.getChildren().addAll(btnShowPo, btnClose);

        // Clear existing columns
        tvPoHeader.getColumns().clear();

        // Map the columns
        tcPendingPoNo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(0)));
        tcPendingOrderType.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(2)));
        tcPendingCategory.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(3)));
        tcPendingPoVendor.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(4)));
        tcPendingOrderTotal.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(1))); //To do: switch to total/add to resultset
        tcPendingPoStatus.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(5)));

        // Set up a TableView for the PO Item
        tvPoHeader.getColumns().addAll(tcPendingPoNo, tcPendingOrderType, tcPendingCategory, tcPendingPoVendor, tcPendingOrderTotal, tcPendingPoStatus);


        // Create a Hbox to place all the panes
        VBox rootSelectPo = new VBox();
        rootSelectPo.getChildren().addAll(paneForPoStatus, paneForPoHeader, paneForPendingPoButtons);
       

        // Create a scene and place it in the stage
        Scene sceneSelectPo = new Scene(rootSelectPo, 700, 600);
        Stage stageSelectPo = new Stage();
        stageSelectPo.setScene(sceneSelectPo);
        stageSelectPo.setTitle("Select Purchase Order");
        stageSelectPo.show();


        //Set up an event handler for cboPoStatus
        cboPoStatus.setOnAction(e -> {
            String selectedPoStatus = cboPoStatus.getSelectionModel().getSelectedItem();

            // System.out.println("PoStatus when cboPoStatus is clicked: " + cboPoStatus.getSelectionModel().getSelectedItem());  // To Do: Testing, delete later


            ObservableList <ObservableList<String>> dataPoHeader =  Database.queryPoHeader(selectedPoStatus);
            
            // Clear existing rows from the TableView
            tvPoHeader.getItems().clear();

            // Rebuild rows in the TableView based on the filter (Pending vs Issued)
            for (ObservableList<String> row: dataPoHeader) {
                tvPoHeader.getItems().add(row);
            }
        });


        // Set up an event handler for btnClose 
        btnClose.setOnAction(e -> {
            stageSelectPo.close();

            // System.out.println("PoStatus when close btn is clicked: " + cboPoStatus.getSelectionModel().getSelectedItem());  // To Do: Testing, delete later

        });

        // Set up an event handler for btnShowPo
        btnShowPo.setOnAction(e -> {
            // Get the value of PoNo from the selected row from the TableView, tvPoHeader
            String tempPoStatus = showPo();
            stageSelectPo.close();
            cboPoStatus.setValue("");; // Clear the selected item

            // System.out.println("PoStatus when btnShowPo is clicked: " + cboPoStatus.getSelectionModel().getSelectedItem());  // To Do: Testing, delete later
            // System.out.println("tempPoStatus after showPo() AND txt.PoStatus.getText" + tempPoStatus); // To Do: Testing, delete later
            
            // Change the quantity column editable when the PO is in 'Pending' status. //To Do: Add another condition 'Pending AND Standard PO'
            if ("Pending".equals(tempPoStatus)) {
                makeColumnEditable(tvLineItem);
            }
                   
        });


        // Clear all data from the primary stage
        btnClear.setOnAction(e -> {
            // Clear text fields
            txtPoNo.setText("");
            txtOrderDate.setText("");
            txtOrderType.setText("");
            txtOrderTotal.setText("$");
            txtPoVendorId.setText("");
            txtPoStatus.setText("");
            txtPoCategory.setText("");
            txtVendorName.setText("");
            txtAddress.setText("");
            txtPhone.setText("");

            // Clear items from the TableView
            tvLineItem.getItems().clear();
        });

        // Issue PO by updating Order Total and poStatus in tblPoHeader and Quantity and Line Total in tblLineItem
        btnIssuePO.setOnAction(e -> {
            // Update Order Total and PoStatus in tblPoHeader
            String UpdatePoStatusResult = Database.updatePoStatus(txtPoNo.getText());

            if (UpdatePoStatusResult.equals("Updated")) {

                txtPoStatus.setText("Issued");  // Update the PoStatus in the PO header area


                // Update Quantity and Line Total in tblLineItem
                // Get all the rows from the line item tableview
                ObservableList<ObservableList<String>> items = tvLineItem.getItems();
                for (ObservableList<String> item : items) {
                    String poLineNo = item.get(1);
                    String quantity = item.get(4);
                    String lineTotal = item.get(7);

                    System.out.println("poLineNo: " + poLineNo + " qty: "+ quantity + " LineTotal: " + lineTotal);

                    boolean lineItemUpdateSuccessful = Database.updateLineItem(txtPoNo.getText(), poLineNo, quantity, lineTotal);

                    if (lineItemUpdateSuccessful) {
                        System.out.println("Update successful for poLineNo: " + poLineNo);
                    } 
                    else {
                        // false returned by SQLException
                        System.out.println("Update failed for poLineNo: " + poLineNo);
                    }
                }
                
                txtUpdateResult.setText("PO has been issued");  // Show the message above btnIssuePo

            }
            else{
                txtUpdateResult.setText("Error issuing PO");
            }
        });

    }


    // Called by btnPoList. Create a list of PO Headers in a TableView on the secondary stage, selectPoStage
    private ObservableList<ObservableList<String>> fillTableViewPOHeader(TableView tvPoHeader) {
        
        ObservableList <ObservableList<String>> dataPoHeader = Database.queryPoHeader("");
        // System.out.println("PoStatus when btnPoList is clicked: " + cboPoStatus.getSelectionModel().getSelectedItem());  // To Do: Testing, delete later

        // Clear existing rows from the TableView
        tvPoHeader.getItems().clear();

        // Add data to the TableView
        for (ObservableList<String> row: dataPoHeader) {
            tvPoHeader.getItems().add(row);
        }

        return dataPoHeader;
    }

    // Display the PO details based on the selected row on the TableView, tvPoHeader
    private String showPo() {
        // Create a SelectionModel
        TableView.TableViewSelectionModel<ObservableList<String>> selectionModel = tvPoHeader.getSelectionModel();

        String poNo = "";  // Used for querying the tblPoItem tble
        String poVendorId = "";  // Used for querying the tblVendor table
        String tempPoStatus = "";  // Used for making the Quantity column editable.

        ObservableList<String> selectedPoHeaderRow = selectionModel.getSelectedItem();  // This list will be used to fill PO Header on the primary stage
        // System.out.println("PoStatus in showPo()/btnShowPo is clicked and the second stg is closed (line just before !selectionModel.isEmpty) : " + cboPoStatus.getSelectionModel().getSelectedItem());  // To Do: Testing, delete later

        // Check if a row is selected
        if (!selectionModel.isEmpty()) {
            
            // Get the PoNo and poVendor from the selected row
            poNo = selectedPoHeaderRow.get(0);
            poVendorId = selectedPoHeaderRow.get(4);
            tempPoStatus = selectedPoHeaderRow.get(5);

            // System.out.println("tempPoStatus after tempPoStatus = selectedPoHeaderRow.get(5); " + tempPoStatus ); // To do delete
            for (String value : selectedPoHeaderRow) {  // To do delete
                System.out.println("Value: " + value);
            }

            System.out.println("Selected PoNo: " + poNo);
        } 
        else {
            // Show Alert if no row is selected before btnShowPo is pressed
            System.out.println("No row selected.");
            Alert noSelectedRowAlert = new Alert(Alert.AlertType.ERROR);
            noSelectedRowAlert.setTitle("Error: No row selected");
            noSelectedRowAlert.setHeaderText(null);
            noSelectedRowAlert.setContentText("Please select a row.");
            noSelectedRowAlert.showAndWait();
            return tempPoStatus = "";
        }

        // System.out.println("tempPoStatus just after if-else in showPO() " + tempPoStatus ); // To do delete

        // Pass the PoNo and run a query
        ObservableList <ObservableList<String>> dataPoItem =  Database.queryPoItem(poNo);
        ObservableList <String> dataPoVendor = Database.queryPoVendor(poVendorId);

        // Fill the tvLineItem and update text fields
        fillLineItemTableView(dataPoItem);
        updateVendorTextFields(dataPoVendor);
        fillPoHeaderTextFields(selectedPoHeaderRow);
        return tempPoStatus;

    }

    // Fill the Line Item TableView on the primary stage
    private void fillLineItemTableView(ObservableList<ObservableList<String>> dataPoItem) {

        // Clear existing tows from the TableView
        tvLineItem.getItems().clear();

        // Add data to the TableView
        for (ObservableList<String> row : dataPoItem) {
            tvLineItem.getItems().add(row);
        }

        tvLineItem.setEditable(false);  // Make tvLineItem non editable by default // Change the Quantity column to editable after showPo() runs

        // Calculate and update the OrderTotal
        calculateOrderTotal();

        System.out.println(txtPoStatus.getText().trim());  // To do Delete
        
    }

    // Fill text fields for vendor details
    private void updateVendorTextFields(ObservableList<String> dataPoVendor) {
        txtVendorName.setText(dataPoVendor.get(1));
        txtAddress.setText(dataPoVendor.get(2));
        txtPhone.setText(dataPoVendor.get(3));
    }

    // Fill text fields for PO Header
    private void fillPoHeaderTextFields(ObservableList<String> selectedPoHeaderRow) {
        txtPoNo.setText(selectedPoHeaderRow.get(0));
        txtOrderDate.setText(selectedPoHeaderRow.get(1));
        txtOrderType.setText(selectedPoHeaderRow.get(2));
        txtPoCategory.setText(selectedPoHeaderRow.get(3));
        txtPoVendorId.setText(selectedPoHeaderRow.get(4));
        txtPoStatus.setText(selectedPoHeaderRow.get(5));
    }

    // Calculate the grand total of a PO based on its line item totals
    private void calculateOrderTotal() {
        double orderTotal = 0.0;
    
        // Iterate through the items in the TableView
        for (ObservableList<String> item : tvLineItem.getItems()) {
            
            double lineTotal = Double.parseDouble(item.get(7));  
            orderTotal += lineTotal;
        }
    
        // Update the txtOrderTotal
        txtOrderTotal.setText("$" + String.format("%.2f", orderTotal));
    }



    private void makeColumnEditable(TableView tvLineItem){
        
        tvLineItem.setEditable(true);
        tcQuantity.setCellFactory(TextFieldTableCell.forTableColumn());  // Make the Quantity column editable when the PoStatus is Pending

        // Set up an event handler to the Quantity column to update the Line Total column automatically
        tcQuantity.setOnEditCommit( e -> {
            // Get the new quantity
            String newQuantity = e.getNewValue();

            // Update the Quantity
            ObservableList<String> editedItem = e.getRowValue();
            editedItem.set(4, newQuantity);

            // Recalculate the line total
            double unitPrice = Double.parseDouble(editedItem.get(5));
            double quantity = Double. parseDouble(newQuantity);
            double lineTotal =unitPrice * quantity;

            // Update the Observable list
            editedItem.set(7, String.format("%.2f", lineTotal));

            // Update the TableView
            tvLineItem.refresh();

            // Update the txtOrderTotal when quantity and line total are changed
            calculateOrderTotal();

            System.out.println(quantity);
            System.out.println(lineTotal);

        });

    }
   
    // private void makeColumnNonEditable(TableView tvLineItem){
    //     tvLineItem.setEditable(false);
    // }


} 
