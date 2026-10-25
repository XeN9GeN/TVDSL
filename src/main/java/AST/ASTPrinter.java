package AST;


import AST.Nodes.*;
import AST.Nodes.ExprNodes.*;
import AST.Nodes.PredNodes.ComparisonNode;
import AST.Nodes.PredNodes.LogicalOpNode;
import AST.Nodes.PredNodes.NotNode;
import AST.Nodes.StmtNodes.*;
import AST.NodesInterfaces.*;

public class ASTPrinter {

    public static void print(ProgramNode program) {
        for (FunctionNode func : program.functions) {
            printFunction(func, 0);
        }
    }

    private static void printFunction(FunctionNode func, int indent) {
        printIndent(indent);
        System.out.println("Function: " + func.name);

        if (func.requires != null) {
            printIndent(indent + 1);
            System.out.println("Requires:");
            printPredicate(func.requires, indent + 2);
        }

        printIndent(indent + 1);
        System.out.println("Body:");
        printBlock(func.body, indent + 2);
    }

    private static void printBlock(BlockNode block, int indent) {
        for (StmtNode stmt : block.statements) {
            printStatement(stmt, indent);
        }
    }

    private static void printStatement(StmtNode stmt, int indent) {
        printIndent(indent);

        if (stmt instanceof AssignNode) {
            AssignNode a = (AssignNode) stmt;
            System.out.println("Assign: " + a.target);
            printExpr(a.value, indent + 1);
        }
        else if (stmt instanceof WhileNode) {
            WhileNode w = (WhileNode) stmt;
            System.out.println("While:");
            printPredicate (w.condition, indent + 1); // condition это ExprNode
            if (w.invariant != null) {
                printIndent(indent + 1);
                System.out.println("Invariant:");
                printPredicate(w.invariant, indent + 2);
            }
            printIndent(indent + 1);
            System.out.println("Body:");
            if (w.body instanceof BlockNode) {
                printBlock((BlockNode) w.body, indent + 2);
            }
        }
        else if (stmt instanceof IfNode) {
            IfNode i = (IfNode) stmt;
            System.out.println("If:");
            printPredicate (i.condition, indent + 1);
            printIndent(indent + 1);
            System.out.println("Then:");
            if (i.thenBranch instanceof BlockNode) printBlock((BlockNode) i.thenBranch, indent + 2);

            if (i.elseBranch != null) {
                printIndent(indent + 1);
                System.out.println("Else:");
                if (i.elseBranch instanceof BlockNode) printBlock((BlockNode) i.elseBranch, indent + 2);
            }
        }
        else if (stmt instanceof AssertNode) {
            System.out.println("Assert:");
            printPredicate(((AssertNode) stmt).predicate, indent + 1);
        }
        else if (stmt instanceof AssumeNode) {
            System.out.println("Assume:");
            printPredicate(((AssumeNode) stmt).predicate, indent + 1);
        }
        else {
            System.out.println("Unknown Statement: " + stmt.getClass().getSimpleName());
        }
    }

    private static void printExpr(ExprNode expr, int indent) {
        printIndent(indent);
        if (expr instanceof IntNode) {
            System.out.println("Int: " + ((IntNode) expr).value);
        } else if (expr instanceof VarNode) {
            System.out.println("Var: " + ((VarNode) expr).name);
        } else if (expr instanceof BinaryOpNode) {
            BinaryOpNode b = (BinaryOpNode) expr;
            System.out.println("BinaryOp: " + b.op);
            printExpr(b.l, indent + 1);
            printExpr(b.r, indent + 1);
        } else if (expr instanceof ArrayAccessNode) {
            ArrayAccessNode a = (ArrayAccessNode) expr;
            System.out.println("ArrayAccess: " + a.arrayName);
            printExpr(a.index, indent + 1);
        } else if (expr instanceof FuncCallNode) {
            FuncCallNode f = (FuncCallNode) expr;
            System.out.println("FuncCall: " + f.functionName);
            for (ExprNode arg : f.args) printExpr(arg, indent + 1);
        } else {
            System.out.println("Unknown Expr: " + expr.getClass().getSimpleName());
        }
    }

    private static void printPredicate(PredNode pred, int indent) {
        if (pred == null) return;
        printIndent(indent);
        if (pred instanceof ComparisonNode) {
            ComparisonNode c = (ComparisonNode) pred;
            System.out.println("Comparison: " + c.op);
            printExpr(c.left, indent + 1);
            printExpr(c.right, indent + 1);
        } else if (pred instanceof LogicalOpNode) {
            LogicalOpNode l = (LogicalOpNode) pred;
            System.out.println("LogicalOp: " + l.op);
            printPredicate(l.left, indent + 1);
            printPredicate(l.right, indent + 1);
        } else if (pred instanceof NotNode) {
            System.out.println("Not:");
            printPredicate(((NotNode) pred).expr, indent + 1);
        } else {
            System.out.println("Unknown Predicate: " + pred.getClass().getSimpleName());
        }
    }

    private static void printIndent(int n) {
        for (int i = 0; i < n; i++) System.out.print("  ");
    }
}