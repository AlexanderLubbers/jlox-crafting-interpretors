package com.jlox;

public class RuntimeError extends RuntimeException {
    final Token token;

    RuntimeError(Token operator, String error) {
        super(error);
        this.token = operator;
    }
}
