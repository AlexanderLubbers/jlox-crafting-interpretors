package com.jlox;

import java.util.HashMap;
import java.util.Map;

public class Environment {
    public Environment enclosing;
    private final Map<String, Object> variables = new HashMap<>();

    public Environment() {
        enclosing = null;
    }
    public Environment(Environment enclosing) {
        this.enclosing = enclosing;
    }
    public void define(String name, Object value) {
        variables.put(name, value);
    }
    public Object get(Token name) {
        if(variables.containsKey(name.lexeme)) {
            return variables.get(name.lexeme);
        }
        if(enclosing != null) {
            return enclosing.get(name);
        }
        throw new RuntimeError(name, "Undefined variable: " + name.lexeme + ".");

    }
    void assign(Token name, Object value) {
        if(variables.containsKey(name.lexeme)) {
            variables.put(name.lexeme, value);
            return;
        }
        if(enclosing != null) {
            enclosing.assign(name, value);
            return;
        }
        throw new RuntimeError(name, "Undefined variable '" + name.lexeme + "'.");

    }
}
