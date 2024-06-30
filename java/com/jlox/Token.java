package com.jlox;
class Token {
    final TokenType type;
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
}
