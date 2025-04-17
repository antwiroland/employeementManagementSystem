package com.example.employmentmanagement;

import java.util.Comparator;

public class Employee<T> implements Comparable<Employee<T>> {
    T employeeId;
    String fullName;
    String department;
    double salary;
    double performanceRating;

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(int yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public double getPerformanceRating() {
        return performanceRating;
    }

    public void setPerformanceRating(double performanceRating) {
        this.performanceRating = performanceRating;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    int yearsOfExperience;
    boolean isActive;


    public T getEmployeeId() {
        return employeeId;
    }

    public Employee(T employeeId, String fullName, String department, double salary,
                    double performanceRating, int yearsOfExperience, boolean isActive) {
        this.employeeId = employeeId;
        this.fullName = fullName;
        this.department = department;
        this.salary = salary;
        this.performanceRating = performanceRating;
        this.yearsOfExperience = yearsOfExperience;
        this.isActive = isActive;
    }

    @Override
    public int compareTo(Employee<T> other) {
        return Integer.compare(this.yearsOfExperience, other.yearsOfExperience);
    }


    @Override
    public String toString() {
        return fullName + " (" + yearsOfExperience + " yrs)";
    }
}

class SortEmployeeBySalary<T> implements Comparator<Employee<T>>{
    @Override
    public int compare(Employee<T> emp1, Employee<T> emp2) {
        return Double.compare(emp2.getSalary(), emp1.getSalary());

    }
}

class SortEmployeeByPerformanceRating<T> implements Comparator<Employee<T>>{
    @Override
    public int compare(Employee<T> emp1, Employee<T> emp2) {
        return Double.compare(emp2.getPerformanceRating(), emp1.getPerformanceRating());

    }
}