package com.example.employmentmanagement;

public class InvalidSalaryException extends RuntimeException{
    public InvalidSalaryException(String message){
        super(message);
    }
}
