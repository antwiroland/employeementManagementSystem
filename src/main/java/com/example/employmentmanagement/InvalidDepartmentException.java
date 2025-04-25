package com.example.employmentmanagement;

public class InvalidDepartmentException extends RuntimeException{
    public InvalidDepartmentException(String message){
        super(message);
    }
}
