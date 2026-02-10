package com.womenshop;

import java.util.List;
import java.util.Optional;

import com.womenshop.model.Accessory;
import com.womenshop.model.Clothing;
import com.womenshop.model.Product;
import com.womenshop.model.Shoes;
import com.womenshop.model.Store;
import com.womenshop.util.DatabaseConnection;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class WomenShopApp extends Application {
    
    private Store store;
    private ListView<Product> productListView;
    private ObservableList<Product> productObservableList;
    
    private Label capitalLabel;
    private Label purchaseCostLabel;
    private Label salesRevenueLabel;
    private Label profitLabel;
    
    private String currentFilter = "All";

    @Override
    public void start(Stage primaryStage) {
        store = new Store();
        productObservableList = FXCollections.observableArrayList();

        try {
            DatabaseConnection.initializeDatabase();
        } catch (Exception e) {
            System.out.println("Database initialization failed: " + e.getMessage());
        }

        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(10));

        Label titleLabel = new Label("WomenShop - Stock Management");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        HBox titleBox = new HBox(titleLabel);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(10));

        mainLayout.setTop(titleBox);

        VBox leftPanel = createProductListPanel();
        mainLayout.setLeft(leftPanel);

        VBox rightPanel = createActionsPanel();
        mainLayout.setRight(rightPanel);

        VBox bottomPanel = createStatisticsPanel();
        mainLayout.setBottom(bottomPanel);

        Scene scene = new Scene(mainLayout, 1000, 700);

        primaryStage.setTitle("WomenShop Application");
        primaryStage.setScene(scene);

        primaryStage.setResizable(false);

        primaryStage.show();

        loadDemoData();
    }
    
    private VBox createProductListPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(10));
        panel.setPrefWidth(500);
        
        Label label = new Label("Products");
        label.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        HBox filterBox = new HBox(5);
        Button allBtn = new Button("All");
        Button clothingBtn = new Button("Clothing");
        Button shoesBtn = new Button("Shoes");
        Button accessoryBtn = new Button("Accessories");
        
        allBtn.setOnAction(e -> filterProducts("All"));
        clothingBtn.setOnAction(e -> filterProducts("Clothing"));
        shoesBtn.setOnAction(e -> filterProducts("Shoes"));
        accessoryBtn.setOnAction(e -> filterProducts("Accessory"));
        
        filterBox.getChildren().addAll(allBtn, clothingBtn, shoesBtn, accessoryBtn);
        
        Button sortBtn = new Button("Sort by Price");
        sortBtn.setOnAction(e -> sortProducts());
        
        productListView = new ListView<>(productObservableList);
        productListView.setPrefHeight(400);
        
        panel.getChildren().addAll(label, filterBox, sortBtn, productListView);
        return panel;
    }
    
    private VBox createActionsPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(10));
        panel.setPrefWidth(350);
        
        Label label = new Label("Actions");
        label.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        Button addBtn = new Button("Add Product");
        addBtn.setPrefWidth(200);
        addBtn.setOnAction(e -> showAddProductDialog());
        
        Button editBtn = new Button("Edit Product");
        editBtn.setPrefWidth(200);
        editBtn.setOnAction(e -> showEditProductDialog());
        
        Button deleteBtn = new Button("Delete Product");
        deleteBtn.setPrefWidth(200);
        deleteBtn.setOnAction(e -> deleteProduct());
        
        Button purchaseBtn = new Button("Purchase Stock");
        purchaseBtn.setPrefWidth(200);
        purchaseBtn.setOnAction(e -> showPurchaseDialog());
        
        Button sellBtn = new Button("Sell Product");
        sellBtn.setPrefWidth(200);
        sellBtn.setOnAction(e -> showSellDialog());
        
        Separator sep = new Separator();
        
        Button applyDiscountBtn = new Button("Apply Discounts");
        applyDiscountBtn.setPrefWidth(200);
        applyDiscountBtn.setOnAction(e -> applyDiscounts());
        
        Button removeDiscountBtn = new Button("Remove Discounts");
        removeDiscountBtn.setPrefWidth(200);
        removeDiscountBtn.setOnAction(e -> removeDiscounts());
        
        panel.getChildren().addAll(label, addBtn, editBtn, deleteBtn, 
                                    new Separator(), purchaseBtn, sellBtn,
                                    new Separator(), applyDiscountBtn, removeDiscountBtn);
        return panel;
    }
    
    private VBox createStatisticsPanel() {
        VBox panel = new VBox(5);
        panel.setPadding(new Insets(10));
        panel.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #cccccc;");
        
        Label label = new Label("Statistics");
        label.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        capitalLabel = new Label("Current Capital: 30000.00 €");
        purchaseCostLabel = new Label("Total Purchase Cost: 0.00 €");
        salesRevenueLabel = new Label("Total Sales Revenue: 0.00 €");
        profitLabel = new Label("Profit: 0.00 €");
        
        panel.getChildren().addAll(label, capitalLabel, purchaseCostLabel, 
                                    salesRevenueLabel, profitLabel);
        return panel;
    }
    
    private void loadDemoData() {
        try {

            // Create initial products as per scenario
            store.addProduct(new Clothing("Dress 1", 70, 100, 38));
            store.addProduct(new Clothing("Dress 2", 90, 120, 40));
            store.addProduct(new Shoes("Shoe 1", 30, 50, 38));
            store.addProduct(new Shoes("Shoe 2", 50, 70, 40));
            store.addProduct(new Accessory("Accessory 1", 20, 30));
            store.addProduct(new Accessory("Accessory 2", 30, 40));
            
            refreshProductList();
            updateStatistics();
        } catch (Exception e) {
            showError("Error loading demo data: " + e.getMessage());
        }
    }
    
    private void showAddProductDialog() {
        Dialog<Product> dialog = new Dialog<>();
        dialog.setTitle("Add Product");
        dialog.setHeaderText("Add a new product");
        
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("Clothing", "Shoes", "Accessory");
        typeCombo.setValue("Clothing");
        
        TextField nameField = new TextField();
        TextField purchaseField = new TextField();
        TextField saleField = new TextField();
        TextField sizeField = new TextField();
        sizeField.setPromptText("Only for Clothing and Shoes");
        
        grid.add(new Label("Type:"), 0, 0);
        grid.add(typeCombo, 1, 0);
        grid.add(new Label("Name:"), 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(new Label("Purchase Price:"), 0, 2);
        grid.add(purchaseField, 1, 2);
        grid.add(new Label("Sale Price:"), 0, 3);
        grid.add(saleField, 1, 3);
        grid.add(new Label("Size:"), 0, 4);
        grid.add(sizeField, 1, 4);
        
        dialog.getDialogPane().setContent(grid);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    String type = typeCombo.getValue();
                    String name = nameField.getText();
                    double purchase = Double.parseDouble(purchaseField.getText());
                    double sale = Double.parseDouble(saleField.getText());
                    
                    if (type.equals("Clothing")) {
                        int size = Integer.parseInt(sizeField.getText());
                        return new Clothing(name, purchase, sale, size);
                    } else if (type.equals("Shoes")) {
                        int size = Integer.parseInt(sizeField.getText());
                        return new Shoes(name, purchase, sale, size);
                    } else {
                        return new Accessory(name, purchase, sale);
                    }
                } catch (Exception e) {
                    showError("Error: " + e.getMessage());
                    return null;
                }
            }
            return null;
        });
        
        Optional<Product> result = dialog.showAndWait();
        result.ifPresent(product -> {
            store.addProduct(product);
            refreshProductList();
            showInfo("Product added successfully!");
        });
    }
    
    private void showEditProductDialog() {
        Product selected = productListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a product to edit");
            return;
        }
        
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Edit Product");
        dialog.setHeaderText("Edit product: " + selected.getName());
        
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField nameField = new TextField(selected.getName());
        TextField sizeField = new TextField();
        
        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        
        if (selected instanceof Clothing) {
            sizeField.setText(String.valueOf(((Clothing) selected).getSize()));
            grid.add(new Label("Size:"), 0, 1);
            grid.add(sizeField, 1, 1);
        } else if (selected instanceof Shoes) {
            sizeField.setText(String.valueOf(((Shoes) selected).getSize()));
            grid.add(new Label("Size:"), 0, 1);
            grid.add(sizeField, 1, 1);
        }
        
        dialog.getDialogPane().setContent(grid);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    selected.setName(nameField.getText());
                    if (selected instanceof Clothing) {
                        ((Clothing) selected).setSize(Integer.parseInt(sizeField.getText()));
                    } else if (selected instanceof Shoes) {
                        ((Shoes) selected).setSize(Integer.parseInt(sizeField.getText()));
                    }
                    store.updateProduct(selected); 
                    return true;
                } catch (NumberFormatException e) {
                    showError("Update failed: Invalid size format");
                    return false;
                } catch (Exception e) {
                    showError("Update failed: " + e.getMessage());
                    return false;
                }
            }
            return false;
        });
        
        Optional<Boolean> result = dialog.showAndWait();
        if (result.isPresent() && result.get()) {
            refreshProductList();
            showInfo("Product updated successfully!");
        }
    }
    
    private void deleteProduct() {
        Product selected = productListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a product to delete");
            return;
        }
        
        try {
            store.removeProduct(selected);
            refreshProductList();
            showInfo("Product deleted successfully!");
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }
    
    private void showPurchaseDialog() {
        Product selected = productListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a product");
            return;
        }
        
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Purchase Stock");
        dialog.setHeaderText("Purchase stock for: " + selected.getName());
        dialog.setContentText("Quantity:");
        
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(quantity -> {
            try {
                int qty = Integer.parseInt(quantity);
                store.purchaseStock(selected, qty);
                refreshProductList();
                updateStatistics();
                showInfo("Stock purchased successfully!");
            } catch (Exception e) {
                showError(e.getMessage());
            }
        });
    }
    
    private void showSellDialog() {
        Product selected = productListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a product");
            return;
        }
        
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Sell Product");
        dialog.setHeaderText("Sell: " + selected.getName() + " (Stock: " + selected.getStockQuantity() + ")");
        dialog.setContentText("Quantity:");
        
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(quantity -> {
            try {
                int qty = Integer.parseInt(quantity);
                store.sellProduct(selected, qty);
                refreshProductList();
                updateStatistics();
                showInfo("Product sold successfully!");
            } catch (Exception e) {
                showError(e.getMessage());
            }
        });
    }
    
    private void applyDiscounts() {
        try {
            store.applyDiscount("Clothing", 30);
            store.applyDiscount("Shoes", 20);
            store.applyDiscount("Accessory", 50);
            refreshProductList();
            showInfo("Discounts applied!\nClothing: 30%\nShoes: 20%\nAccessories: 50%");
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }
    
    private void removeDiscounts() {
        try {
            store.removeAllDiscounts();
            refreshProductList();
            showInfo("All discounts removed!");
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }
    
    private void filterProducts(String category) {
        currentFilter = category;
        refreshProductList();
    }
    
    private void sortProducts() {
        List<Product> products = getCurrentProducts();
        List<Product> sorted = store.sortProductsByPrice(products);
        productObservableList.setAll(sorted);
    }
    
    private void refreshProductList() {
        List<Product> products = getCurrentProducts();
        productObservableList.setAll(products);
    }
    
    private List<Product> getCurrentProducts() {
        if (currentFilter.equals("All")) {
            return store.getAllProducts();
        } else {
            return store.getProductsByCategory(currentFilter);
        }
    }
    
    private void updateStatistics() {
        capitalLabel.setText(String.format("Current Capital: %.2f €", store.getCurrentCapital()));
        purchaseCostLabel.setText(String.format("Total Purchase Cost: %.2f €", store.getTotalPurchaseCost()));
        salesRevenueLabel.setText(String.format("Total Sales Revenue: %.2f €", store.getTotalSalesRevenue()));
        profitLabel.setText(String.format("Profit: %.2f €", store.getProfit()));
    }
    
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
