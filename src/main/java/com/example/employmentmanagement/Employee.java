package com.example.employmentmanagement;

import java.util.Comparator;
import java.util.Objects;

public class Employee<T> implements Comparable<Employee<T>> {
    private final T employeeId;
    private String fullName;
    private String department;
    private double salary;
    private double performanceRating;
    private int yearsOfExperience;
    private boolean isActive;

    public Employee(T employeeId, String fullName, String department, double salary,
                    double performanceRating, int yearsOfExperience, boolean isActive) {
        this.employeeId = validateEmployeeId(employeeId);
        this.fullName = validateFullName(fullName);
        this.department = validateDepartment(department);
        this.salary = validateSalary(salary);
        this.performanceRating = validatePerformanceRating(performanceRating);
        this.yearsOfExperience = validateYearsOfExperience(yearsOfExperience);
        this.isActive = isActive;
    }

    // Validation methods
    private T validateEmployeeId(T id) {
        if (id == null) {
            throw new IllegalArgumentException("Employee ID cannot be null");
        }
        if (id instanceof String && ((String) id).trim().isEmpty()) {
            throw new IllegalArgumentException("Employee ID cannot be empty");
        }
        return id;
    }

    private String validateFullName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be null or empty");
        }
        return name.trim();
    }

    private String validateDepartment(String dept) {
        if (dept == null || dept.trim().isEmpty()) {
            throw new InvalidDepartmentException("Department cannot be null or empty");
        }
        return dept.trim();
    }

    private double validateSalary(double salary) {
        if (salary < 0) {
            throw new InvalidSalaryException("Salary cannot be negative");
        }
        return salary;
    }

    private double validatePerformanceRating(double rating) {
        if (rating < 0 || rating > 10) {
            throw new IllegalArgumentException("Performance rating must be between 0 and 10");
        }
        return rating;
    }

    private int validateYearsOfExperience(int years) {
        if (years < 0) {
            throw new IllegalArgumentException("Years of experience cannot be negative");
        }
        return years;
    }

    // Getters and Setters with validation
    public T getEmployeeId() {
        return employeeId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = validateFullName(fullName);
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = validateDepartment(department);
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = validateSalary(salary);
    }

    public double getPerformanceRating() {
        return performanceRating;
    }

    public void setPerformanceRating(double performanceRating) {
        this.performanceRating = validatePerformanceRating(performanceRating);
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(int yearsOfExperience) {
        this.yearsOfExperience = validateYearsOfExperience(yearsOfExperience);
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public int compareTo(Employee<T> other) {
        Objects.requireNonNull(other, "Cannot compare with null employee");
        return Integer.compare(this.yearsOfExperience, other.yearsOfExperience);
    }

    @Override
    public String toString() {
        return fullName + " (" + yearsOfExperience + " yrs)";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee<?> employee = (Employee<?>) o;
        return employeeId.equals(employee.employeeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId);
    }
}

class SortEmployeeBySalary<T> implements Comparator<Employee<T>> {
    @Override
    public int compare(Employee<T> emp1, Employee<T> emp2) {
        Objects.requireNonNull(emp1, "First employee cannot be null");
        Objects.requireNonNull(emp2, "Second employee cannot be null");
        return Double.compare(emp2.getSalary(), emp1.getSalary());
    }
}

class SortEmployeeByPerformanceRating<T> implements Comparator<Employee<T>> {
    @Override
    public int compare(Employee<T> emp1, Employee<T> emp2) {
        Objects.requireNonNull(emp1, "First employee cannot be null");
        Objects.requireNonNull(emp2, "Second employee cannot be null");
        return Double.compare(emp2.getPerformanceRating(), emp1.getPerformanceRating());
    }
}