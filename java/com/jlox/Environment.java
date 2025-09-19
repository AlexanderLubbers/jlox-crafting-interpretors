package com.jlox;

import java.util.HashMap;
import java.util.Map;

public class Environment {
    private final Map<String, Object> variables = new HashMap<>();

    public void define(String name, Object value) {
        variables.put(name, value);
    }
    public Object get(Token name) {
        if(variables.containsKey(name.lexeme)) {
            return variables.get(name.lexeme);
        }

        throw new RuntimeError(name, "Undefined variable: " + name.lexeme + ".");

    }
}
