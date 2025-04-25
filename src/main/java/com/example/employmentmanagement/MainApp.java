package com.example.employmentmanagement;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.UUID;

public class MainApp extends Application {

    EmployeeDatabase<String> db = new EmployeeDatabase<>();
    ListView<String> employeeListView = new ListView<>();
    ObservableList<String> employeeList = FXCollections.observableArrayList();

    public static void main(String[] args) {
        printToConsole();
        launch(args);
    }

    public static void printToConsole(){
        EmployeeDatabase<Integer> db = new EmployeeDatabase<>();
        // Add sample employees
        db.addEmployee(new Employee<>(101, "Alice Johnson", "Engineering", 75000, 4.5, 5, true));
        db.addEmployee(new Employee<>(102, "Bob Smith", "Engineering", 68000, 3.9, 4, true));
        db.addEmployee(new Employee<>(103, "Charlie Brown", "Marketing", 55000, 4.8, 6, true));
        db.addEmployee(new Employee<>(104, "Diana Prince", "HR", 60000, 4.2, 3, false));
        db.addEmployee(new Employee<>(105, "Ethan Hunt", "Engineering", 85000, 4.9, 7, true));
        db.addEmployee(new Employee<>(106, "Fiona Gallagher", "Marketing", 70000, 3.5, 4, true));

        System.out.println("\n✅ All Employees:");
        db.printToConsole();

        System.out.println("\n💰 Giving a raise of $2000 to employees with performance rating >= 4.5");
        db.raiseSalary(4.5, 2000);

        System.out.println("\n📌 Updating department of employee 102 to 'Product'");
        System.out.println(db.updateEmployee(102, "department", "Product"));

        System.out.println("\n🔥 Top 5 Highest Paid Employees:");
        db.getHighestPaid().forEach(System.out::println);

        System.out.println("\n📊 Average Salary in 'Engineering' Department:");
        System.out.println("$" + db.getAverageSalaryOfDepartment("Engineering"));

        System.out.println("\n🔍 Employees with Salary between $60,000 and $80,000:");
        System.out.println(db.getSalaryRange(60000, 80000));

        System.out.println("\n⭐ Employees with Performance Rating >= 4.0:");
        System.out.println(db.getPerformanceRating(4.0));
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Employee Management System");

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        // Input Fields (no ID field now)
        TextField nameField = new TextField(); nameField.setPromptText("Full Name");
        TextField deptField = new TextField(); deptField.setPromptText("Department");
        TextField salaryField = new TextField(); salaryField.setPromptText("Salary");
        TextField ratingField = new TextField(); ratingField.setPromptText("Performance Rating");
        TextField expField = new TextField(); expField.setPromptText("Years of Experience");

        // Buttons
        Button addBtn = new Button("Add");
        Button removeBtn = new Button("Remove");
        Button updateBtn = new Button("Update Field");

        // Search / Filter / Sort
        TextField searchField = new TextField(); searchField.setPromptText("Search Department");
        Button searchBtn = new Button("Search");
        Button resetBtn = new Button("Reset");

        TextField filterRatingField = new TextField(); filterRatingField.setPromptText("Min Rating");
        Button filterRatingBtn = new Button("Filter by Rating");

        Button sortSalaryBtn = new Button("Sort by Salary");
        Button sortPerformanceBtn = new Button("Sort by Performance");

        // Raise Salary
        TextField raiseRatingField = new TextField(); raiseRatingField.setPromptText("Min Rating");
        TextField raiseAmountField = new TextField(); raiseAmountField.setPromptText("Raise Amount");
        Button raiseBtn = new Button("Raise Salary");

        // Field Update
        TextField updateFieldName = new TextField(); updateFieldName.setPromptText("Field Name");
        TextField updateNewValue = new TextField(); updateNewValue.setPromptText("New Value");

        // Layout
        HBox inputBox = new HBox(10, nameField, deptField, salaryField, ratingField, expField, addBtn);
        HBox updateBox = new HBox(10, updateFieldName, updateNewValue, updateBtn);
        HBox removeBox = new HBox(10, removeBtn);
        HBox searchBox = new HBox(10, searchField, searchBtn, resetBtn);
        HBox filterBox = new HBox(10, filterRatingField, filterRatingBtn);
        HBox sortBox = new HBox(10, sortSalaryBtn, sortPerformanceBtn);
        HBox raiseBox = new HBox(10, raiseRatingField, raiseAmountField, raiseBtn);

        employeeListView.setItems(employeeList);
        root.getChildren().addAll(inputBox, updateBox, removeBox, searchBox, filterBox, sortBox, raiseBox, employeeListView);

        // --- Actions ---
        addBtn.setOnAction(e -> {
            try {
                String id = UUID.randomUUID().toString();
                String name = nameField.getText();
                String dept = deptField.getText();

                double salary = Double.parseDouble(salaryField.getText());

                if (salary < 0) {
                    throw new InvalidSalaryException("Invalid salary: Salary must be positive");
                }

                if (dept.isEmpty()){
                    throw new InvalidDepartmentException("Invalid Department: You have to enter Department");
                }

                double rating = Double.parseDouble(ratingField.getText());
                int years = Integer.parseInt(expField.getText());

                db.addEmployee(new Employee<>(id, name, dept, salary, rating, years, true));
                refreshList();
                clearInputs(nameField, deptField, salaryField, ratingField, expField);
            } catch (InvalidSalaryException ex) {
                showAlert("Invalid Input", "Check fields and try again.");
                System.out.println(ex.getMessage());

            } catch (InvalidDepartmentException ex) {
                System.out.println(ex.getMessage());
                showAlert("Invalid Department", "Check fields and try again.");
            }
            catch (Exception ex) {
                showAlert("Invalid Input", "Check fields and try again.");
                System.out.println(ex.getMessage());
            }
        });

        removeBtn.setOnAction(e -> {
            String selected = employeeListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                String empId = selected.split(" ")[0];
                db.removeEmployee(empId);
                refreshList();
            }
        });

        updateBtn.setOnAction(e -> {
            String selected = employeeListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                String empId = selected.split(" ")[0];
                String field = updateFieldName.getText();
                String newVal = updateNewValue.getText();

                String msg = db.updateEmployee(empId, field, parseFieldValue(field, newVal));
                showAlert("Update Result", msg);
                refreshList();
            }
        });

        searchBtn.setOnAction(e -> {
            try{
                String dept = searchField.getText();
                if(dept.isEmpty()){
                    throw new InvalidDepartmentException("Enter valid department");
                }
                employeeList.setAll(
                        db.getAllEmployee().stream()
                                .filter(emp -> emp.getDepartment().equalsIgnoreCase(dept))
                                .map(emp -> emp.getEmployeeId() + " - " + emp.toString())
                                .toList()
                );
            }catch(InvalidDepartmentException ex){
                showAlert("Invalid Department", "Check fields and try again.");
                System.out.println(ex.getMessage());
            }

        });

        resetBtn.setOnAction(e -> refreshList());

        filterRatingBtn.setOnAction(e -> {
            try {
                double minRating = Double.parseDouble(filterRatingField.getText());
                employeeList.setAll(
                        db.getAllEmployee().stream()
                                .filter(emp -> emp.getPerformanceRating() >= minRating)
                                .map(emp -> emp.getEmployeeId() + " - " + emp.toString())
                                .toList()
                );
            } catch (Exception ex) {
                showAlert("Invalid Input", "Enter valid rating.");
                System.out.println(ex.getMessage());
            }
        });

        sortSalaryBtn.setOnAction(e -> {
            employeeList.setAll(
                    db.getAllEmployee().stream()
                            .sorted((a, b) -> Double.compare(b.getSalary(), a.getSalary()))
                            .map(emp -> emp.getEmployeeId() + " - " + emp.toString())
                            .toList()
            );
        });

        sortPerformanceBtn.setOnAction(e -> {
            employeeList.setAll(
                    db.getAllEmployee().stream()
                            .sorted((a, b) -> Double.compare(b.getPerformanceRating(), a.getPerformanceRating()))
                            .map(emp -> emp.getEmployeeId() + " - " + emp.toString())
                            .toList()
            );
        });

        raiseBtn.setOnAction(e -> {
            try {
                double rating = Double.parseDouble(raiseRatingField.getText());
                double amount = Double.parseDouble(raiseAmountField.getText());
                if(amount > 0){
                    throw new InvalidSalaryException("Amount must be a positive number");
                }
                db.raiseSalary(rating, amount);
                showAlert("Raise Complete", "Salaries updated.");
                refreshList();
            } catch (InvalidSalaryException ex) {
                showAlert("Amount Error", "Amount must be a positive number");
                System.out.println(ex.getMessage());
            }
        });

        refreshList();
        stage.setScene(new Scene(root, 1200, 600));
        stage.show();
    }

    private void refreshList() {
        employeeList.setAll(
                db.getAllEmployee().stream()
                        .map(emp -> emp.getEmployeeId() + " - " + emp.toString())
                        .toList()
        );
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private Object parseFieldValue(String field, String value) {
        try {
            return switch (field.toLowerCase()) {
                case "salary", "performancerating" -> Double.parseDouble(value);
                case "yearsofexperience" -> Integer.parseInt(value);
                default -> value;
            };
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return value;
        }
    }

    private void clearInputs(TextField... fields) {
        for (TextField f : fields) {
            f.clear();
        }
    }
}
