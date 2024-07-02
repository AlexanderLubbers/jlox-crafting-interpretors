package com.jlox;
class Token {
    final TokenType type;
    //lexemes are part of the input stream from which tokens are identified
    final String lexeme;
    //object is a super class which means that it can refer to an instance of any class
    final Object literal;
    final int line;
    Token(TokenType type, String lexeme, Object literal, int line) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }
    public String toString() {
        return type + " " + lexeme + " " + literal;
    }
}
