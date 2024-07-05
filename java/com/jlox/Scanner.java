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
            case '!' -> addToken(match('=') ? BANG_EQUAL : BANG); //if the current character is not an =, then it is only ! not !=
            case '=' -> addToken(match('=') ? EQUAL_EQUAL : EQUAL);
            case '<' -> addToken(match('=') ? LESS_EQUAL : LESS);
            case '>' -> addToken(match('=') ? GREATER_EQUAL : GREATER);
            case '/' -> {
                if(match('/')) {
                    //commends go until the end of the line
                    //use advance to consume another character
                    while (peek() != '\n' && !isAtEnd()) advance();
                } else {
                    //if it is just a single slash then add a single slash token
                    addToken(SLASH);
                }
            }
            case ' ' -> {
            }
             //carriage return. Moves the cursor to the beginning of the line
            case '\r' -> {
            }
            //ignore whitespace
            case '\t' -> {
            }
            //if the new line character is detected then move to the next line
            //this will go back to the beginning of the loop
            case '\n' -> line++;
            case '"' -> string();

            default -> Lox.error(line, "Unexpected character.");
        }
}
    private void string() {
        //while the next character does not equal " and the end of the file is not reached
        while (peek() != '"' && !isAtEnd()) {
            //if a new line character is reached then go to the next line
            if (peek() == '\n') line++;
            //consume the character
            advance();
        }
        //if the end of the file is reached, then report an unterminated string
        if (isAtEnd()) {
            Lox.error(line, "Unterminated string.");
            return;
        }
        // consume one more character (the closing ")
        advance();
        //determine the string literal. do not include the starting and ending "
        String value = source.substring(start + 1, current - 1);
        //add a string token and pass in the value
        addToken(STRING, value);
    }
    //check to see if the next character matches the input supplied
     private boolean match(char expected) {
        if (isAtEnd()) return false;
        if (source.charAt(current) != expected) return false;
        current++;
        return true;
    }
    private char peek() {
        //return a null character if the end is reached
        if (isAtEnd()) return '\0';
        //return the next char which is what the current variable looks at
        //it only looks at the next char meaning this is called a lookahead
        return source.charAt(current);
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
