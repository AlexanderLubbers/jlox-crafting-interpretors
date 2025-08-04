package com.jlox;

import static com.jlox.TokenType.*;

class Interpretor implements Expr.Visitor<Object> {
    
    @Override
    public Object visitLiteralExpr(Expr.Literal expr) {
        return expr.value;
    }

    @Override
    public Object visitLiteralExprRPN(Expr.Literal expr) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object visitGroupingExpr(Expr.Grouping expr) {
        return evaluate(expr.expression);
    }

    @Override 
    public Object visitGroupingExprRPN(Expr.Grouping expr) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    //puts the expression inside of the grouping expression back into visitor implementation so that way it can be evaluated
    private Object evaluate(Expr expression) {
        return expression.accept(this);
    }

    @Override
    public Object visitBinaryExpr(Expr.Binary expr) {
        Object left = evaluate(expr.left);
        Object right = evaluate(expr.right);

        switch(expr.operator.type) {
            case MINUS:
                return (double)left + -(double)right;
            case STAR:
                return (double)right * (double)left;
            case SLASH:
                return (double)left / (double)right;
            case GREATER_EQUAL:
                if((double)left >= (double)right) {
                    return true;
                }
                return false;
            case LESS_EQUAL:
                if((double)left <= (double)right) {
                    return true;
                }
                return false;
            case GREATER:
                if((double)left > (double)right) {
                    return true;
                }
                return false;
            case LESS:
                if((double)left < (double)right) {
                    return true;
                }
                return false;
            case PLUS:
                if(left instanceof String && right instanceof String) {
                    return (String)left + (String)right;
                }
                if(left instanceof Double && right instanceof Double) {
                    return (double)left + (double)right;
                }
                break;
            case EQUAL_EQUAL:
                return isEqual(left, right);
            case BANG_EQUAL:
                return !isEqual(left, right);
        }
        return null;
    }

    @Override
    public Object visitUnaryExpr(Expr.Unary expr) {
        Object right = evaluate(expr.right);

        //refactor if more cases are not added
        switch(expr.operator.type) {
            case MINUS:
                return -(double)right;
            case BANG:
                return !isTruth(right);
        }

        return null;
    }

    @Override
    public Object visitTernaryExpr(Expr.Ternary expr) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object visitBinaryExprRPN(Expr.Binary expr) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object visitUnaryExprRPN(Expr.Unary expr) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object visitTernaryExprRPN(Expr.Ternary expr) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private boolean isTruth(Object object) {
        if(object == null) return false;
        try {
            return (boolean)object;
        } catch (Exception e) {
            //print out the error
            System.out.println(e.getCause().getMessage());
        }
        //if(object instanceof Boolean) return (boolean) object;
        return true;
    }

    private Object isEqual(Object left, Object right) {
        if(left == null && right == null) {
            return true;
        }
        if(left == null) {
            return false;
        }
        return left.equals(right);
    }
}
