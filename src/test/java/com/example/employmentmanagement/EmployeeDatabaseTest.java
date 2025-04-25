package com.example.employmentmanagement;

import java.util.Optional;
import java.util.regex.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;


class EmployeeDatabaseTest {
    private EmployeeDatabase<String> database;

    @BeforeEach
    void setUp() {
        database = new EmployeeDatabase<>();
    }

    @Test
    @DisplayName("Should not return empty after creating employee")
    void addEmployeeTest() {
        Employee<String> employee = new Employee<>("1222","roland antwi","IT",3434.3,6.5,8,true);
        database.addEmployee(employee);
        Assertions.assertFalse(database.getAllEmployee().isEmpty());
    }

    @Test
    @DisplayName("Should return null for employee with id 1222")
    void removeEmployee() {
        Employee<String> employee = new Employee<>("1222","roland antwi","IT",3434.3,6.5,8,true);
        database.removeEmployee("1222");
        Assertions.assertNull(database.getByIdEmployee("1222"));
    }

    @Test
    void sortByDepartment() {
        List<Employee<String>> employeeList = List.of(
                new Employee<>("1", "ama kate", "Science", 203.2, 3.5, 2, true),
                new Employee<>("2", "John Doe", "IT", 454.4, 4.0, 3, true),
                new Employee<>("3", "Steve Jobs", "IT", 5663.4, 5.5, 4, true),
                new Employee<>("4", "Bill Gate", "IT", 5605.3, 4.8, 5, true),
                new Employee<>("1222", "roland antwi", "Medicine", 3434.3, 6.5, 8, true)
        );

        try {
            employeeList.forEach(emp -> {
                try {
                    database.addEmployee(emp);
                } catch (EmployeeNotFoundException e) {
                    Assertions.fail("Failed to add employee: " + emp.getEmployeeId(), e);
                }
            });
        } catch (Exception e) {
            Assertions.fail("Database initialization failed", e);
        }

        List<String> output;
        try {
            output = database.sortByDepartment("IT");
            Assertions.assertNotNull(output, "Sorted list should not be null");
        } catch (Exception e) {
            Assertions.fail("Sorting failed", e);
            return;
        }

        for (String employeeString : output) {
            try {
                String[] parts = employeeString.split(" - ");
                Assertions.assertTrue(parts.length >= 1,
                        "Invalid employee string format: " + employeeString);

                String id = parts[0].trim();
                Assertions.assertFalse(id.isEmpty(), "Employee ID cannot be empty");

                Optional<Employee<String>> employeeOpt = database.getByIdEmployee(id);
                Assertions.assertTrue(employeeOpt.isPresent(),
                        "Employee with ID " + id + " not found");

                Employee<String> employee = employeeOpt.get();
                Assertions.assertEquals("IT", employee.getDepartment(),
                        "Employee " + employee.getFullName() + " is not from IT department");
            } catch (EmployeeNotFoundException e) {
                Assertions.fail("Error processing employee: " + employeeString, e);
            }
        }

        long itEmployeesInDb = employeeList.stream()
                .filter(e -> "IT".equals(e.getDepartment()))
                .count();

        Assertions.assertEquals(itEmployeesInDb, output.size(),
                "Not all IT employees were returned. Expected: " + itEmployeesInDb +
                        ", Actual: " + output.size());

        Assertions.assertTrue(
                output.stream().noneMatch(String::isEmpty),
                "Output list should not contain empty strings"
        );
    }
}