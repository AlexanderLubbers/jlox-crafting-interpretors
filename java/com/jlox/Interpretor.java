package com.jlox;

//import com.jlox.Expr.Variable;
//import com.jlox.Stmt.Var;
import static com.jlox.TokenType.*;
import java.util.List;

class Interpretor implements Expr.Visitor<Object>, Stmt.Visitor<Void> {
    private Environment environment = new Environment();

    @Override
    public Object visitLiteralExpr(Expr.Literal expr) {
        return expr.value;
    }


    @Override
    public Object visitGroupingExpr(Expr.Grouping expr) {
        return evaluate(expr.expression);
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
                checkNumberOperands(expr.operator, left, right);
                return (double)left + -(double)right;
            case STAR:
                checkNumberOperands(expr.operator, left, right);
                return (double)right * (double)left;
            case SLASH:
                checkNumberOperands(expr.operator, left, right);
                if((double)right == 0.0) {
                    throw new RuntimeError(expr.operator, "Cannot divide by zero");
                }
                return (double)left / (double)right;
            case GREATER_EQUAL:
                checkNumberOperands(expr.operator, left, right);
                if((double)left >= (double)right) {
                    return true;
                }
                return false;
            case LESS_EQUAL:
                checkNumberOperands(expr.operator, left, right);
                if((double)left <= (double)right) {
                    return true;
                }
                return false;
            case GREATER:
                checkNumberOperands(expr.operator, left, right);
                if((double)left > (double)right) {
                    return true;
                }
                return false;
            case LESS:
                checkNumberOperands(expr.operator, left, right);
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
                if(left instanceof Double && right instanceof String) {
                    return stringify((double)left) + (String)right;
                }
                if(left instanceof String && right instanceof Double) {
                    return (String)left + stringify((double)right);
                }
                throw new RuntimeError(expr.operator, "Operands must be either two strings or two numbers");
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
                checkNumberOperand(expr.operator, right);
                return -(double)right;
            case BANG:
                return !isTruth(right);
        }

        return null;
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

    private boolean isEqual(Object left, Object right) {
        if(left == null && right == null) {
            return true;
        }
        if(left == null) {
            return false;
        }
        return left.equals(right);
    }

    private void checkNumberOperand(Token operator, Object operand) {
        if(operand instanceof Double) return;
        throw new RuntimeError(operator, "Operand must be a number");
    }
    private void checkNumberOperands(Token operator, Object left, Object right) {
        if(left instanceof Double && right instanceof Double) {
            return;
        }
        throw new RuntimeError(operator, "Operands must be numbers");
    }

    @Override
    public Object visitTernaryExpr(Expr.Ternary expr) {
        Object condition = evaluate(expr.condition);
        Object branch = evaluate(expr.branch);
        Object otherBranch = evaluate(expr.otherBranch);

        if(condition == null) {
            return otherBranch;   
        }
        if(condition instanceof Boolean bool) {
            if(bool == false) {
                return otherBranch;
            }
        }
        return branch;
    }
    public void interpret(List<Stmt> statements) {
        try {
            for(int i = 0; i < statements.size(); i++) {
                execute(statements.get(i));
            }
        } catch (RuntimeError error) {
            Lox.runtimeError(error);
        }
    }
    private void execute(Stmt statement) {
        statement.accept(this);
    }

    private String stringify(Object obj) {
        if(obj == null) return "nil";

        if(obj instanceof Double d) {
            String text = d.toString();
            if(text.endsWith(".0")) text = text.substring(0, text.length()-2);
            return text;

        }

        return obj.toString();
    }


    @Override
    public Void visitExpressionStmt(Stmt.Expression stmt) {
        evaluate(stmt.expression);
        return null;
    }

    @Override
    public Void visitPrintStmt(Stmt.Print stmt) {
        Object value = evaluate(stmt.expression);
        System.out.println(stringify(value));
        return null;
    }


    @Override
    public Void visitVarStmt(Stmt.Var stmt) {
        Object value = null;
        if(stmt.value != null) {
            value = evaluate(stmt.value);
        }
        environment.define(stmt.name.lexeme, value);
        return null;
    }


    @Override
    public Object visitVariableExpr(Expr.Variable expr) {
        return environment.get(expr.name);
    }

    // all of the RPN logic still needs to be implemented
    @Override
    public Object visitLiteralExprRPN(Expr.Literal expr) {
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

    @Override 
    public Object visitGroupingExprRPN(Expr.Grouping expr) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object visitVariableExprRPN(Expr.Variable expr) {
        throw new UnsupportedOperationException("Unimplemented method 'visitVariableExprRPN'");
    }

    

}