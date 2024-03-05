package com.example.coursecreation.exception;

public class CategoryNotFoundException extends RuntimeException{

    public CategoryNotFoundException(){
        super("category not found");
    }
}
