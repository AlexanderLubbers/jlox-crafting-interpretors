package com.jlox;

class AstPrinter implements Expr.Visitor<String> {
    public static void main(String[] args)  {
        Expr expression = new Expr.Binary(
            new Expr.Unary(
                new Token(TokenType.MINUS, "-", null, 1),
                new Expr.Literal(123)),
            new Token(TokenType.STAR, "*", null, 1),
            new Expr.Grouping(
                new Expr.Literal(45.67)));
        System.out.println(new AstPrinter().print(expression));
        System.out.println("reverse polish notation: ");
        System.out.println(new AstPrinter().printRPN(expression));
    }
    
    String print(Expr expr) {
        return expr.accept(this);
    }
    String printRPN(Expr expr) {
        return expr.acceptRPN(this);
    }

    @Override
    public String visitTernaryExpr(Expr.Ternary expr) {
        return parenthesize(expr.operator.lexeme+expr.otherOperator.lexeme, expr.condition, expr.branch, expr.otherBranch);
    }

    @Override
    public String visitBinaryExpr(Expr.Binary expr) {
        return parenthesize(expr.operator.lexeme, expr.left, expr.right);
    }

    @Override
    public String visitGroupingExpr(Expr.Grouping expr) {
        return parenthesize("group", expr.expression);
    }

    @Override
    public String visitLiteralExpr(Expr.Literal expr) {
        if (expr.value == null) return "nil";
        return expr.value.toString();
    }

    @Override
    public String visitUnaryExpr(Expr.Unary expr) {
        return parenthesize(expr.operator.lexeme, expr.right);
    }
    //Expr... exprs is a varargs parameter which means zero or more Exprs can be passed in
    //this function wraps up subexpressions in parenthesis. ex: (+ 1 2)
    private String parenthesize(String name,  Expr... exprs) {
        StringBuilder builder = new StringBuilder();
        builder.append("(").append(name);
        for (Expr expr : exprs) {
            builder.append(" ");
            builder.append(expr.accept(this));
        }
        builder.append(")");
        return builder.toString();
    }
    private String parenthesizeRPN(String name, Expr... exprs) {
        StringBuilder builder = new StringBuilder();
        for (Expr expr : exprs) {
            builder.append(expr.acceptRPN(this));
            builder.append(" ");
        }
        builder.append(name);
        return builder.toString();
    }

    @Override
    public String visitBinaryExprRPN(Expr.Binary expr) {
        return parenthesizeRPN(expr.operator.lexeme, expr.left, expr.right);
    }

    @Override
    public String visitGroupingExprRPN(Expr.Grouping expr) {
        return parenthesizeRPN("group", expr.expression);
    }

    @Override
    public String visitLiteralExprRPN(Expr.Literal expr) {
        if (expr.value == null) return "nil";
        return expr.value.toString();
    }

    @Override
    public String visitUnaryExprRPN(Expr.Unary expr) {
        return parenthesizeRPN(expr.operator.lexeme, expr.right);
    }
    @Override
    public String visitTernaryExprRPN(Expr.Ternary expr) {
        return parenthesizeRPN(expr.operator.lexeme+expr.otherOperator.lexeme, expr.condition, expr.branch, expr.otherBranch);
    }
}
