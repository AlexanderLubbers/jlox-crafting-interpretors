package com.jlox;

import static com.jlox.TokenType.BANG;
import static com.jlox.TokenType.BANG_EQUAL;
import static com.jlox.TokenType.EOF;
import static com.jlox.TokenType.EQUAL_EQUAL;
import static com.jlox.TokenType.FALSE;
import static com.jlox.TokenType.GREATER;
import static com.jlox.TokenType.GREATER_EQUAL;
import static com.jlox.TokenType.LEFT_PAREN;
import static com.jlox.TokenType.LESS;
import static com.jlox.TokenType.LESS_EQUAL;
import static com.jlox.TokenType.MINUS;
import static com.jlox.TokenType.NIL;
import static com.jlox.TokenType.NUMBER;
import static com.jlox.TokenType.PLUS;
import static com.jlox.TokenType.RIGHT_PAREN;
import static com.jlox.TokenType.SLASH;
import static com.jlox.TokenType.STAR;
import static com.jlox.TokenType.STRING;
import static com.jlox.TokenType.TRUE;

import java.util.List;
import javax.print.DocFlavor;

class Parser {
    //list of all tokens
    private final List<Token> tokens;
    private int current = 0;
    Parser(List<Token> tokens) {
        //set the tokens variable equal to the input of the constructor
        this.tokens = tokens;
    }
    private Expr expression() {
        //the rule for an expression expands to equality so the rule of that should be looked at
        return equality();
    }
    // equality -> comparison ( ( "!=" | "==" ) comparison )* ;
    //this method matches an equality operator or anything of higher precedence
    private Expr equality() {
        Expr expr = comparison();
        //while loop to make sure the entire sequence of comparison operators is handled
        while(match(BANG_EQUAL, EQUAL_EQUAL)) {
            //the previous token was either != or ==
            Token operator = previous();
            Expr right = comparison();
            //will repeatedly append the new operator and right value to the expr
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }
    //the ... indicates that there can be a variable number of arguments
    private boolean match(TokenType... types) {
        //checks to see if the token matches any of the given types
        for(TokenType type: types) {
            //check to see whether type matches the current token
            if(check(type)) {
                //if so consume the token and return true
                advance();
                return true;
            }
        }
        //return false and do nothing to the current token
        return false;
    }
    private boolean check(TokenType type) {
        //return false if the end of the list of tokens was reached
        if(isAtEnd()) return false;
        //check to see if the type matches the argument
        return peek().type == type;
    }
    private Expr comparison() {
        Expr expr = term();
        while(match(GREATER, GREATER_EQUAL, LESS, LESS_EQUAL)) {
            //since match consumes a token, the operater is the previous token
            Token operator = previous();
            Expr right = term();

            expr = new Expr.Binary(expr, operator, right);
            return expr;
        }
        return expr;
    }
    //return the previous token that was just consumed
    private Token previous() {
        return tokens.get(current - 1);
    }
    //read the current token, go to the next one, and then return the token it just read.
    private Token advance() {
        if(!isAtEnd()) current++;
        return previous();
    }
    //check to see if the end of the tokens list has been reached
    private boolean isAtEnd() {
        //if the token is an end of file token, then the end of the list of tokens was reached
        if(peek().type == EOF)  {
            return true;
        }
        return false;
    }
    private Token peek() {
        return tokens.get(current);
    }
    //term handles additiona nd subtraction
    private Expr term() {
        Expr expr = factor();
        while(match(PLUS, MINUS)) {
            //since match consumes a token, the operator will be the previous token that was just consumed
            Token operator = previous();
            Expr right = factor();

            expr = new Expr.Binary(expr, operator, right);
            return expr;
        }
        return expr;
    }
    private Expr factor() {
        Expr expr = unary();
        while(match(STAR, SLASH)) {

            Token operator = previous();
            Expr right = unary();

            expr = new Expr.Binary(expr, operator, right);
            return expr;
        }
        return expr;
    }
    private Expr unary() {
        if (match(BANG, MINUS)) {
            Token operator = previous();
            Expr right = unary();
            
            return new Expr.Unary(operator, right);
        }
        //if there is no unary operator then it must be a primary
        return primary();
    }
    private Expr primary() {
        if(match(FALSE)) return new Expr.Literal(false);
        if(match(TRUE)) return new Expr.Literal(true);
        if(match(NIL)) return new Expr.Literal(null);
        if(match(NUMBER, STRING)) {
            return new Expr.Literal(previous().literal);
        }
        if(match(LEFT_PAREN)) {
            Expr expr = expression();
            consume(RIGHT_PAREN, "Expect ')' after expression. ");
            return new Expr.Grouping(expr);
        }
    }
} 