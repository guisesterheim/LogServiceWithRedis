package com.log.analytics.exceptions;

public class NoDataFoundException extends Throwable {

    public static final String NO_DATA_FOUND = "No data found";

    public NoDataFoundException(){}

    public NoDataFoundException(String message){
        super(message);
    }

}