package com.jlox;
abstract class Expr{
// <R> is the generic type parameter. it means that the code can operate on any type that the user specifies
// R means that the return type can be anything but the caller must specify
    abstract <R> R accept(Visitor<R> visitor);
    //create an accept function for reverse polish notation
    abstract <R> R acceptRPN(Visitor<R> visitor);
        interface Visitor<R> {
            R visitBinaryExpr(Binary expr);
            R visitGroupingExpr(Grouping expr);
            R visitLiteralExpr(Literal expr);
            R visitUnaryExpr(Unary expr);
            //add visitor classes to turn the expresions into reverse polish notation format
            R visitBinaryExprRPN(Binary expr);
            R visitGroupingExprRPN(Grouping expr);
            R visitLiteralExprRPN(Literal expr);
            R visitUnaryExprRPN(Unary expr);
        }
    static class Binary extends Expr {
        Binary(Expr left, Token operator, Expr right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }
 
        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitBinaryExpr(this);
        }
        @Override
        <R> R acceptRPN(Visitor<R> visitor) {
            return visitor.visitBinaryExprRPN(this);
        }
        final Expr left;
        final Token operator;
        final Expr right;
    }
    static class Grouping extends Expr {
        Grouping(Expr expression) {
            this.expression = expression;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitGroupingExpr(this);
        }
        @Override
        <R> R acceptRPN(Visitor<R> visitor) {
            return visitor.visitGroupingExprRPN(this);
        }
        final Expr expression;
    }
    static class Literal extends Expr {
        Literal(Object value) {
            this.value = value;
        }
 

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitLiteralExpr(this);
        }
        @Override
        <R> R acceptRPN(Visitor<R> visitor) {
            return visitor.visitLiteralExprRPN(this);
        }
        final Object value;
    }
    static class Unary extends Expr {
        Unary(Token operator, Expr right) {
            this.operator = operator;
            this.right = right;
        }
 

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitUnaryExpr(this);
        }
        @Override
        <R> R acceptRPN(Visitor<R> visitor) {
            return visitor.visitUnaryExprRPN(this);
        }
        final Token operator;
        final Expr right;
    }
}
