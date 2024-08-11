package com.jlox;

class AstPrinter implements Expr.Visitor<String> {
    String print(Expr expr) {
        
        return expr.accept(this);
    }
    @Override
    public String visitBinaryExpr(Expr.Binary expr) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String visitGroupingExpr(Expr.Grouping expr) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String visitLiteralExpr(Expr.Literal expr) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String visitUnaryExpr(Expr.Unary expr) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    //Expr exprs is a varargs parameter which means zero or more Exprs can be passed in
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
}
