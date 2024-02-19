package org.nepalimarket.nepalimarketproproject.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class MultipleExceptionsOccurredException extends RuntimeException {

    private final List<Exception> exceptions;

    public MultipleExceptionsOccurredException ( List<Exception> exceptions ) {
        this.exceptions = exceptions;
    }

}