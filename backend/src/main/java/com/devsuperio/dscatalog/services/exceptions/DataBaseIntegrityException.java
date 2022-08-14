package com.devsuperio.dscatalog.services.exceptions;

public class DataBaseIntegrityException extends RuntimeException{

    public DataBaseIntegrityException(String msg) {
        super(msg);
    }
}
