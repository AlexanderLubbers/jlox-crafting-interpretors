package com.jlox;
import static com.jlox.TokenType.*;
import java.util.ArrayList;
import java.util.List;
public class Scanner {
    //the raw source code will be stored in this string
    private final String source;
    //create an empty list to be filled with tokens
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 1;
    Scanner(String source) {
        this.source = source;
    }
    List<Token> scanTokens() {
        while(!isAtEnd()) {
            start = current;
            scanToken();
        }
        tokens.add(new Token(EOF, "", null, line));
        return tokens;

    }
    private void scanToken() {
        char c = advance();
        switch (c) {
            case '(' -> addToken(LEFT_PAREN);
            case ')' -> addToken(RIGHT_PAREN);
            case '{' -> addToken(LEFT_BRACE);
            case '}' -> addToken(RIGHT_BRACE);
            case ',' -> addToken(COMMA);
            case '.' -> addToken(DOT);
            case '-' -> addToken(MINUS);
            case '+' -> addToken(PLUS);
            case ';' -> addToken(SEMICOLON);
            case '*' -> addToken(STAR);
            default -> Lox.error(line, "Unexpected character.");
        }
    }
    private boolean isAtEnd() {
        //return whether the current field is greater than or equal to the total length of the file
        return current >= source.length();
    }
    private char advance() {
        current++;
        //get the character at the current location
        return source.charAt(current - 1);
    }
    private void addToken(TokenType type) {
        addToken(type, null);
    }
    //this function grabs the current lexeme and creates a token for it
    private void addToken(TokenType type, Object literal) {
        //returns the single character detected
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }
}
