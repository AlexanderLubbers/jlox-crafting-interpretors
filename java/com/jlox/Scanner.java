package com.jlox;
import static com.jlox.TokenType.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//overall purpose of this class is to read a file containing text and convert that text into tokens for the parser to parse
public class Scanner {
    //the raw source code will be stored in this string
    private final String source;
    //create an empty list to be filled with tokens
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 1;
    private static final Map<String, TokenType> keywords;
    static {
        //create a hashmap corresponds the word in the langauge to the token type
        keywords = new HashMap<>();
        keywords.put("and", AND);
        keywords.put("class", CLASS);
        keywords.put("else", ELSE);
        keywords.put("false", FALSE);
        keywords.put("for", FOR);
        keywords.put("fun", FUN);
        keywords.put("if", IF);
        keywords.put("nil", NIL);
        keywords.put("or", OR);
        keywords.put("print", PRINT);
        keywords.put("return", RETURN);
        keywords.put("super", SUPER);
        keywords.put("this", THIS);
        keywords.put("true", TRUE);
        keywords.put("var", VAR);
        keywords.put("while", WHILE);
 }
    Scanner(String source) {
        this.source = source;
    }
    List<Token> scanTokens() {
        while(!isAtEnd()) {
            start = current;
            scanToken();
        }
        //add an EOF token because the end of the source was reached
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
            case '?' -> addToken(Q);
            case ':' -> addToken(COLON);
            case '/' -> {
                if(match('/')) {
                    //commends go until the end of the line
                    //use advance to consume another character
                    while (peek() != '\n' && !isAtEnd()) advance();
                } else if(match('*')) {
                    while(peek() != '*' && peekNext() != '/') {
                        if(peek() == '\n') {
                            line++;
                        }
                        advance();
                    }
                    if(isAtEnd()) {
                        Lox.error(line, "Unterminated Comment");
                    } else {
                        //advance two times to consume the asterisk and slash that terminated the comment
                        advance();
                        advance();
                    }
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

            default -> {
                if (isDigit(c)) {
                    number();

                } else if (isAlpha(c)) {
                    //that means that variable names cannot start with numbers??
                    identifier();
                } else {
                    Lox.error(line, "Unexpected character");
                }
                
            }
        }
    }
    private void identifier() {
        //use alpha numberic so that numbers can be in the variable name
        while(isAlphaNumeric(peek())) advance();

        String text = source.substring(start, current);
        //check to see if the text matches any of the keywords
        // if so change the token type to that keyword
        TokenType type = keywords.get(text);
        if (type == null) type = IDENTIFIER;
        addToken(type);
    }
    private void number() {
        //consume all numbers
        //call advance until there are no more digits
        while(isDigit(peek())) advance();
        //check to see if the next character is a decimal point
        if (peek() == '.' && isDigit(peekNext())) {
            //consume the .
            advance();
            //consume everything avter the decimal point
            while(isDigit(peek())) advance();
        }
        //start and current can be accessed here because their scope is the entire class
        //Double.parseDouble() turns a string into a double
        addToken(NUMBER, Double.parseDouble(source.substring(start, current)));
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
    private char peekNext() {
        //if the next character does not exist then return null character
        if (current + 1 >= source.length()) return '\0';
        //current is technically the next character so to get the character after that add one
        return source.charAt(current+1);
    }
    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
               (c >= 'A' && c <= 'Z') ||
                c == '_';
    }
    private boolean isAlphaNumeric(char c) {
        //is the input either a number or a letter
        return isAlpha(c) || isDigit(c);
    }
    // check to see whether the char is a number
    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
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
