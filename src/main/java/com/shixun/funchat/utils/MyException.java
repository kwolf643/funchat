package com.shixun.funchat.utils;

public class MyException extends Exception {
    private MyExceptionType type;

    public MyException() {
        super();
    }

    public MyException(MyExceptionType type) {
        this.type = type;
    }

    public MyExceptionType getType() {
        return type;
    }
}
