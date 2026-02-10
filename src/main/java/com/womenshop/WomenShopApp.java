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
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class WomenShopApp extends Application {

    // ========== CONSTANTS ==========
    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 700;
    private static final int LEFT_PANEL_WIDTH = 500;
    private static final int RIGHT_PANEL_WIDTH = 350;
    private static final int BUTTON_WIDTH = 200;

    // ========== STATE ==========
    private Store store;
    private ListView<Product> productListView;
    private ObservableList<Product> productObservableList;

    private Label capitalLabel;
    private Label purchaseCostLabel;
    private Label salesRevenueLabel;
    private Label profitLabel;

    private String currentFilter = "All";

    // ========== MAIN ENTRY POINT ==========

    @Override
    public void start(Stage primaryStage) {
        initializeState();
        initializeDatabase();

        BorderPane mainLayout = buildMainLayout();
        Scene scene = new Scene(mainLayout, WINDOW_WIDTH, WINDOW_HEIGHT);

        configureStage(primaryStage, scene);
        primaryStage.show();

        loadDemoData();
    }

    private void initializeState() {
        store = new Store();
        productObservableList = FXCollections.observableArrayList();
    }

    private void initializeDatabase() {
        try {
            DatabaseConnection.initializeDatabase();
        } catch (Exception e) {
            showError("Database initialization failed: " + e.getMessage());
        }
    }

    private BorderPane buildMainLayout() {
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(10));

        layout.setTop(createTitleBox());
        layout.setLeft(createProductListPanel());
        layout.setRight(createActionsPanel());
        layout.setBottom(createStatisticsPanel());

        return layout;
    }

    private void configureStage(Stage stage, Scene scene) {
        stage.setTitle("WomenShop Application");
        stage.setScene(scene);
        stage.setResizable(false);
    }

    // ========== UI COMPONENTS CREATION ==========

    private HBox createTitleBox() {
        Label titleLabel = new Label("WomenShop - Stock Management");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        HBox titleBox = new HBox(titleLabel);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(10));

        return titleBox;
    }

    private VBox createProductListPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(10));
        panel.setPrefWidth(LEFT_PANEL_WIDTH);

        Label label = createSectionLabel("Products");
        HBox filterBox = createFilterButtons();
        Button sortBtn = createButton("Sort by Price", this::sortProducts);

        productListView = new ListView<>(productObservableList);
        productListView.setPrefHeight(400);

        panel.getChildren().addAll(label, filterBox, sortBtn, productListView);
        return panel;
    }

    private HBox createFilterButtons() {
        HBox filterBox = new HBox(5);

        Button allBtn = createButton("All", () -> filterProducts("All"));
        Button clothingBtn = createButton("Clothing", () -> filterProducts("Clothing"));
        Button shoesBtn = createButton("Shoes", () -> filterProducts("Shoes"));
        Button accessoryBtn = createButton("Accessories", () -> filterProducts("Accessory"));

        filterBox.getChildren().addAll(allBtn, clothingBtn, shoesBtn, accessoryBtn);
        return filterBox;
    }

    private VBox createActionsPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(10));
        panel.setPrefWidth(RIGHT_PANEL_WIDTH);

        Label label = createSectionLabel("Actions");

        Button addBtn = createActionButton("Add Product", this::showAddProductDialog);
        Button editBtn = createActionButton("Edit Product", this::showEditProductDialog);
        Button deleteBtn = createActionButton("Delete Product", this::deleteProduct);
        Button purchaseBtn = createActionButton("Purchase Stock", this::showPurchaseDialog);
        Button sellBtn = createActionButton("Sell Product", this::showSellDialog);
        Button applyDiscountBtn = createActionButton("Apply Discounts", this::applyDiscounts);
        Button removeDiscountBtn = createActionButton("Remove Discounts", this::removeDiscounts);

        panel.getChildren().addAll(
                label,
                addBtn, editBtn, deleteBtn,
                new Separator(),
                purchaseBtn, sellBtn,
                new Separator(),
                applyDiscountBtn, removeDiscountBtn
        );

        return panel;
    }

    private VBox createStatisticsPanel() {
        VBox panel = new VBox(5);
        panel.setPadding(new Insets(10));
        panel.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #cccccc;");

        Label label = createSectionLabel("Statistics");

        capitalLabel = new Label("Current Capital: 30000.00 €");
        purchaseCostLabel = new Label("Total Purchase Cost: 0.00 €");
        salesRevenueLabel = new Label("Total Sales Revenue: 0.00 €");
        profitLabel = new Label("Profit: 0.00 €");

        panel.getChildren().addAll(label, capitalLabel, purchaseCostLabel,
                salesRevenueLabel, profitLabel);
        return panel;
    }

    // ========== UI HELPERS ==========

    private Label createSectionLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        return label;
    }

    private Button createButton(String text, Runnable action) {
        Button button = new Button(text);
        button.setOnAction(e -> action.run());
        return button;
    }

    private Button createActionButton(String text, Runnable action) {
        Button button = createButton(text, action);
        button.setPrefWidth(BUTTON_WIDTH);
        return button;
    }

    // ========== DATA LOADING ==========

    private void loadDemoData() {
        try {
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

    // ========== DIALOGS ==========

    private void showAddProductDialog() {
        Dialog<Product> dialog = new Dialog<>();
        dialog.setTitle("Add Product");
        dialog.setHeaderText("Add a new product");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = createProductFormGrid();

        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("Clothing", "Shoes", "Accessory");
        typeCombo.setValue("Clothing");

        TextField nameField = new TextField();
        TextField purchaseField = new TextField();
        TextField saleField = new TextField();
        TextField sizeField = new TextField();
        sizeField.setPromptText("Only for Clothing and Shoes");

        addFormFields(grid, typeCombo, nameField, purchaseField, saleField, sizeField);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return createProductFromForm(typeCombo.getValue(), nameField.getText(),
                        purchaseField.getText(), saleField.getText(),
                        sizeField.getText());
            }
            return null;
        });

        handleDialogResult(dialog.showAndWait());
    }

    private GridPane createProductFormGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        return grid;
    }

    private void addFormFields(GridPane grid, ComboBox<String> typeCombo,
                               TextField nameField, TextField purchaseField,
                               TextField saleField, TextField sizeField) {
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
    }

    private Product createProductFromForm(String type, String name,
                                          String purchaseStr, String saleStr,
                                          String sizeStr) {
        try {
            double purchase = Double.parseDouble(purchaseStr);
            double sale = Double.parseDouble(saleStr);

            switch (type) {
                case "Clothing":
                case "Shoes":
                    int size = Integer.parseInt(sizeStr);
                    return type.equals("Clothing")
                            ? new Clothing(name, purchase, sale, size)
                            : new Shoes(name, purchase, sale, size);
                case "Accessory":
                    return new Accessory(name, purchase, sale);
                default:
                    throw new IllegalArgumentException("Unknown product type: " + type);
            }
        } catch (NumberFormatException e) {
            showError("Invalid number format: " + e.getMessage());
            return null;
        } catch (Exception e) {
            showError("Error creating product: " + e.getMessage());
            return null;
        }
    }

    private void handleDialogResult(Optional<Product> result) {
        result.ifPresent(product -> {
            store.addProduct(product);
            refreshProductList();
            showInfo("Product added successfully!");
        });
    }

    private void showEditProductDialog() {
        Product selected = getSelectedProduct();
        if (selected == null) return;

        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Edit Product");
        dialog.setHeaderText("Edit product: " + selected.getName());

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = createProductFormGrid();
        TextField nameField = new TextField(selected.getName());
        TextField sizeField = new TextField();

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);

        addSizeFieldIfNeeded(grid, selected, sizeField);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return updateProductFromForm(selected, nameField.getText(), sizeField.getText());
            }
            return false;
        });

        Optional<Boolean> result = dialog.showAndWait();
        if (result.isPresent() && result.get()) {
            refreshProductList();
            showInfo("Product updated successfully!");
        }
    }

    private void addSizeFieldIfNeeded(GridPane grid, Product product, TextField sizeField) {
        if (product instanceof Clothing) {
            sizeField.setText(String.valueOf(((Clothing) product).getSize()));
            grid.add(new Label("Size:"), 0, 1);
            grid.add(sizeField, 1, 1);
        } else if (product instanceof Shoes) {
            sizeField.setText(String.valueOf(((Shoes) product).getSize()));
            grid.add(new Label("Size:"), 0, 1);
            grid.add(sizeField, 1, 1);
        }
    }

    private boolean updateProductFromForm(Product product, String name, String sizeStr) {
        try {
            product.setName(name);

            if (product instanceof Clothing) {
                ((Clothing) product).setSize(Integer.parseInt(sizeStr));
            } else if (product instanceof Shoes) {
                ((Shoes) product).setSize(Integer.parseInt(sizeStr));
            }

            store.updateProduct(product);
            return true;
        } catch (NumberFormatException e) {
            showError("Update failed: Invalid size format");
            return false;
        } catch (Exception e) {
            showError("Update failed: " + e.getMessage());
            return false;
        }
    }

    private void deleteProduct() {
        Product selected = getSelectedProduct();
        if (selected == null) return;

        try {
            store.removeProduct(selected);
            refreshProductList();
            showInfo("Product deleted successfully!");
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void showPurchaseDialog() {
        Product selected = getSelectedProduct();
        if (selected == null) return;

        showQuantityDialog(
                "Purchase Stock",
                "Purchase stock for: " + selected.getName(),
                quantity -> {
                    store.purchaseStock(selected, quantity);
                    refreshProductList();
                    updateStatistics();
                    showInfo("Stock purchased successfully!");
                }
        );
    }

    private void showSellDialog() {
        Product selected = getSelectedProduct();
        if (selected == null) return;

        showQuantityDialog(
                "Sell Product",
                "Sell: " + selected.getName() + " (Stock: " + selected.getStockQuantity() + ")",
                quantity -> {
                    store.sellProduct(selected, quantity);
                    refreshProductList();
                    updateStatistics();
                    showInfo("Product sold successfully!");
                }
        );
    }

    // ========== QUANTITY DIALOG HELPER ==========

    @FunctionalInterface
    private interface QuantityAction {
        void execute(int quantity) throws Exception;
    }

    private void showQuantityDialog(String title, String header, QuantityAction action) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText("Quantity:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(quantityStr -> {
            try {
                int quantity = Integer.parseInt(quantityStr);
                action.execute(quantity);
            } catch (NumberFormatException e) {
                showError("Invalid quantity: must be a number");
            } catch (Exception e) {
                showError(e.getMessage());
            }
        });
    }

    // ========== DISCOUNT OPERATIONS ==========

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

    // ========== PRODUCT LIST OPERATIONS ==========

    private void filterProducts(String category) {
        currentFilter = category;
        refreshProductList();
    }

    private void sortProducts() {
        List<Product> sorted = store.sortProductsByPrice(getCurrentProducts());
        productObservableList.setAll(sorted);
    }

    private void refreshProductList() {
        productObservableList.setAll(getCurrentProducts());
    }

    private List<Product> getCurrentProducts() {
        return currentFilter.equals("All")
                ? store.getAllProducts()
                : store.getProductsByCategory(currentFilter);
    }

    private Product getSelectedProduct() {
        Product selected = productListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a product");
        }
        return selected;
    }

    // ========== STATISTICS UPDATE ==========

    private void updateStatistics() {
        capitalLabel.setText(formatCurrency("Current Capital", store.getCurrentCapital()));
        purchaseCostLabel.setText(formatCurrency("Total Purchase Cost", store.getTotalPurchaseCost()));
        salesRevenueLabel.setText(formatCurrency("Total Sales Revenue", store.getTotalSalesRevenue()));
        profitLabel.setText(formatCurrency("Profit", store.getProfit()));
    }

    private String formatCurrency(String label, double amount) {
        return String.format("%s: %.2f €", label, amount);
    }

    // ========== ALERTS ==========

    private void showError(String message) {
        showAlert(Alert.AlertType.ERROR, "Error", message);
    }

    private void showInfo(String message) {
        showAlert(Alert.AlertType.INFORMATION, "Information", message);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // ========== MAIN ==========

    public static void main(String[] args) {
        launch(args);
    }
}