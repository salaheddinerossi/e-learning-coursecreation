package com.example.coursecreation.exception;

public class NoChildCategoriesException extends RuntimeException{
    public NoChildCategoriesException(){
        super("this category has no sub categories");
    }
}
