package com.example.employmentmanagement;

import java.util.*;
import java.util.stream.Collectors;

public class EmployeeDatabase<T> {
    Map<T,Employee<T>> database = new HashMap<>();

    public void addEmployee(Employee<T> employee){
        database.put(employee.getEmployeeId(),employee);
    }

    public void removeEmployee(T employeeId){
        database.remove(employeeId);
    }

    public String updateEmployee(T employeeId,String field,Object newValue){
        Employee<T> employee = database.get(employeeId);
        switch (field.toLowerCase()){
            case "name":
                if(newValue instanceof String){
                    employee.setFullName(newValue.toString());
                    return "Employee name changed ";
                }
                break;
            case "department":
                if(newValue instanceof String){
                    employee.setDepartment(newValue.toString());
                    return "Employee department changed ";
                }
                break;
            case "salary":
                if(newValue instanceof Double){
                    employee.setSalary((double) newValue);
                    return "Employee salary changed ";
                }
                break;
            case "performancerating":
                if(newValue instanceof Double){
                    employee.setPerformanceRating((double) newValue);
                    return "Employee years of performance rating changed ";
                }
                break;
            case "yearsofexperience":
                if(newValue instanceof Double){
                    employee.setYearsOfExperience((int) newValue);
                    return "Employee years of experience changed ";
                }
                break;
            default:
                return "Field Not Found " + field;
        }
        return "Field Not Found " + field;
    }

    public Optional<Employee<T>> getByIdEmployee(String id) {
        if (id == null) {
            throw new EmployeeNotFoundException("Employee ID cannot be null");
        }
        if (id.trim().isEmpty()) {
            throw new IllegalArgumentException("Employee ID cannot be empty");
        }

        try {
            Employee<T> employee = database.get(id);
            return Optional.ofNullable(employee);
        } catch (EmployeeNotFoundException ex) {
            System.err.println("Employee not found: " + ex.getMessage());
            return Optional.empty();
        } catch (Exception ex) {
            System.err.println("Error retrieving employee: " + ex.getMessage());
            return Optional.empty();
        }
    }

    public Collection<Employee<T>> getAllEmployee(){
        return database.values();
    }

    public List<String> sortByDepartment(String department){
//        return database.values().stream()
//                .filter(emp -> emp.getDepartment().equalsIgnoreCase(department))
//                .map(Employee::toString)
//                .collect(Collectors.joining());

        return  getAllEmployee().stream()
                .filter(emp -> emp.getDepartment().equalsIgnoreCase(department))
                .map(emp -> emp.getEmployeeId() + " - " + emp.toString())
                .toList();

    }


    public String getPerformanceRating(double rating){
        return  database.values().stream()
                .filter(emp -> emp.getPerformanceRating() >= rating)
                .map(Employee::toString).collect(Collectors.joining());
    }

    public  String getSalaryRange(double startingSalary, double endingSalary){
        return  database.values().stream()
                .filter(emp -> emp.getSalary() >= startingSalary && emp.getSalary() <= endingSalary)
                .map(Employee::toString).collect(Collectors.joining());
    }

    public void raiseSalary(double rating, double amount) {
        database.values().stream()
                .filter(emp -> emp.getPerformanceRating() >= rating)
                .forEach(emp -> {
                    emp.setSalary(emp.getSalary() + amount);
                    System.out.println(emp.getFullName() + " got a raise. New salary: " + emp.getSalary());
                });
    }

    public List<Employee<T>> getHighestPaid(){
        return database.values().stream()
                .sorted(Comparator.comparingDouble(Employee::getSalary))
                .limit(5)
                .toList();
    }

    public double getAverageSalaryOfDepartment(String department) {
        return database.values().stream()
                .filter(emp -> emp.getDepartment().equals(department))
                .mapToDouble(Employee::getSalary)
                .average()
                .orElse(0.0);
    }

    public void printToConsole() {
        database.values().forEach(emp ->
                System.out.println(
                        "Full Name: " + emp.getFullName() +
                                " | Department: " + emp.getDepartment() +
                                " | Salary: " + emp.getSalary() +
                                " | Performance Rating: " + emp.getPerformanceRating() +
                                " | Years of Experience: " + emp.getYearsOfExperience() +
                                " | Status: " + (emp.isActive() ? "Active" : "Inactive")
                )
        );
    }

}

