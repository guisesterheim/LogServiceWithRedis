package com.log.analytics.exceptions;

public class InvalidIngestionInputException extends Throwable {

    public static final String INPUT_CANNOT_BE_EMPTY = "Input cannot be  empty";

    public InvalidIngestionInputException(){}

    public InvalidIngestionInputException(String message){
        super(message);
    }

}