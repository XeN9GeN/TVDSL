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

    private static String getIndent(int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) sb.append("  ");
        return sb.toString();
    }

    public static void printToJson(ProgramNode program) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n  \"functions\": [\n");
        for (int i = 0; i < program.functions.size(); i++) {
            sb.append("    ").append(functionToJson(program.functions.get(i), 2));
            if (i < program.functions.size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("  ]\n}");

        String path = "C:/Users/user/IdeaProjects/TVDSL/Funny lang/ast.json";
        try (java.io.FileWriter fw = new java.io.FileWriter(path)) {
            fw.write(sb.toString());
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
    private static String functionToJson(FunctionNode func, int indent) {
        String padIn = getIndent(indent + 1);
        String padEnd = getIndent(indent);
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append(padIn).append("\"type\": \"Function\",\n");
        sb.append(padIn).append("\"name\": \"").append(escapeJson(func.name)).append("\",\n");
        sb.append(padIn).append("\"requires\": ");
        if (func.requires == null) sb.append("null");
        else sb.append(predicateToJson(func.requires, indent + 1));
        sb.append(",\n");
        sb.append(padIn).append("\"body\": ").append(blockToJson(func.body, indent + 1)).append("\n");
        sb.append(padEnd).append("}");
        return sb.toString();
    }
    private static String blockToJson(BlockNode block, int indent) {
        String padIn = getIndent(indent + 1);
        String padEnd = getIndent(indent);
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append(padIn).append("\"type\": \"Block\",\n");
        sb.append(padIn).append("\"statements\": [\n");
        for (int i = 0; i < block.statements.size(); i++) {
            sb.append(getIndent(indent + 2)).append(statementToJson(block.statements.get(i), indent + 2));
            if (i < block.statements.size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append(padIn).append("]\n");
        sb.append(padEnd).append("}");
        return sb.toString();
    }
    private static String statementToJson(StmtNode stmt, int indent) {
        String padIn = getIndent(indent + 1);
        String padEnd = getIndent(indent);
        if (stmt instanceof AssignNode) {
            AssignNode a = (AssignNode) stmt;
            return "{\n" +
                    padIn + "\"type\": \"Assign\",\n" +
                    padIn + "\"target\": \"" + escapeJson(a.target) + "\",\n" +
                    padIn + "\"value\": " + exprToJson(a.value, indent + 1) + "\n" +
                    padEnd + "}";
        } else if (stmt instanceof WhileNode) {
            WhileNode w = (WhileNode) stmt;
            return "{\n" +
                    padIn + "\"type\": \"While\",\n" +
                    padIn + "\"condition\": " + predicateToJson(w.condition, indent + 1) + ",\n" +
                    padIn + "\"invariant\": " + (w.invariant == null ? "null" : predicateToJson(w.invariant, indent + 1)) + ",\n" +
                    padIn + "\"body\": " + (w.body instanceof BlockNode ? blockToJson((BlockNode) w.body, indent + 1) : "null") + "\n" +
                    padEnd + "}";
        } else if (stmt instanceof IfNode) {
            IfNode i = (IfNode) stmt;
            return "{\n" +
                    padIn + "\"type\": \"If\",\n" +
                    padIn + "\"condition\": " + predicateToJson(i.condition, indent + 1) + ",\n" +
                    padIn + "\"thenBranch\": " + (i.thenBranch instanceof BlockNode ? blockToJson((BlockNode) i.thenBranch, indent + 1) : "null") + ",\n" +
                    padIn + "\"elseBranch\": " + (i.elseBranch == null ? "null" : (i.elseBranch instanceof BlockNode ? blockToJson((BlockNode) i.elseBranch, indent + 1) : "null")) + "\n" +
                    padEnd + "}";
        } else if (stmt instanceof AssertNode) {
            return "{\n" +
                    padIn + "\"type\": \"Assert\",\n" +
                    padIn + "\"predicate\": " + predicateToJson(((AssertNode) stmt).predicate, indent + 1) + "\n" +
                    padEnd + "}";
        } else if (stmt instanceof AssumeNode) {
            return "{\n" +
                    padIn + "\"type\": \"Assume\",\n" +
                    padIn + "\"predicate\": " + predicateToJson(((AssumeNode) stmt).predicate, indent + 1) + "\n" +
                    padEnd + "}";
        } else {
            return "{\n" +
                    padIn + "\"type\": \"UnknownStatement\",\n" +
                    padIn + "\"class\": \"" + escapeJson(stmt.getClass().getSimpleName()) + "\"\n" +
                    padEnd + "}";
        }
    }
    private static String exprToJson(ExprNode expr, int indent) {
        String padIn = getIndent(indent + 1);
        String padEnd = getIndent(indent);
        if (expr instanceof IntNode) {
            return "{\n" +
                    padIn + "\"type\": \"Int\",\n" +
                    padIn + "\"value\": " + ((IntNode) expr).value + "\n" +
                    padEnd + "}";
        } else if (expr instanceof VarNode) {
            return "{\n" +
                    padIn + "\"type\": \"Var\",\n" +
                    padIn + "\"name\": \"" + escapeJson(((VarNode) expr).name) + "\"\n" +
                    padEnd + "}";
        } else if (expr instanceof BinaryOpNode) {
            BinaryOpNode b = (BinaryOpNode) expr;
            return "{\n" +
                    padIn + "\"type\": \"BinaryOp\",\n" +
                    padIn + "\"op\": \"" + escapeJson(b.op) + "\",\n" +
                    padIn + "\"left\": " + exprToJson(b.l, indent + 1) + ",\n" +
                    padIn + "\"right\": " + exprToJson(b.r, indent + 1) + "\n" +
                    padEnd + "}";
        } else if (expr instanceof ArrayAccessNode) {
            ArrayAccessNode a = (ArrayAccessNode) expr;
            return "{\n" +
                    padIn + "\"type\": \"ArrayAccess\",\n" +
                    padIn + "\"arrayName\": \"" + escapeJson(a.arrayName) + "\",\n" +
                    padIn + "\"index\": " + exprToJson(a.index, indent + 1) + "\n" +
                    padEnd + "}";
        } else if (expr instanceof FuncCallNode) {
            FuncCallNode f = (FuncCallNode) expr;
            StringBuilder sb = new StringBuilder();
            sb.append("{\n");
            sb.append(padIn).append("\"type\": \"FuncCall\",\n");
            sb.append(padIn).append("\"functionName\": \"").append(escapeJson(f.functionName)).append("\",\n");
            sb.append(padIn).append("\"args\": [\n");
            for (int i = 0; i < f.args.size(); i++) {
                sb.append(getIndent(indent + 2)).append(exprToJson(f.args.get(i), indent + 2));
                if (i < f.args.size() - 1) sb.append(",");
                sb.append("\n");
            }
            sb.append(padIn).append("]\n");
            sb.append(padEnd).append("}");
            return sb.toString();
        } else {
            return "{\n" +
                    padIn + "\"type\": \"UnknownExpr\",\n" +
                    padIn + "\"class\": \"" + escapeJson(expr.getClass().getSimpleName()) + "\"\n" +
                    padEnd + "}";
        }
    }
    private static String predicateToJson(PredNode pred, int indent) {
        if (pred == null) return "null";
        String padIn = getIndent(indent + 1);
        String padEnd = getIndent(indent);
        if (pred instanceof ComparisonNode) {
            ComparisonNode c = (ComparisonNode) pred;
            return "{\n" +
                    padIn + "\"type\": \"Comparison\",\n" +
                    padIn + "\"op\": \"" + escapeJson(c.op) + "\",\n" +
                    padIn + "\"left\": " + exprToJson(c.left, indent + 1) + ",\n" +
                    padIn + "\"right\": " + exprToJson(c.right, indent + 1) + "\n" +
                    padEnd + "}";
        } else if (pred instanceof LogicalOpNode) {
            LogicalOpNode l = (LogicalOpNode) pred;
            return "{\n" +
                    padIn + "\"type\": \"LogicalOp\",\n" +
                    padIn + "\"op\": \"" + escapeJson(l.op) + "\",\n" +
                    padIn + "\"left\": " + predicateToJson(l.left, indent + 1) + ",\n" +
                    padIn + "\"right\": " + predicateToJson(l.right, indent + 1) + "\n" +
                    padEnd + "}";
        } else if (pred instanceof NotNode) {
            return "{\n" +
                    padIn + "\"type\": \"Not\",\n" +
                    padIn + "\"expr\": " + predicateToJson(((NotNode) pred).expr, indent + 1) + "\n" +
                    padEnd + "}";
        } else {
            return "{\n" +
                    padIn + "\"type\": \"UnknownPredicate\",\n" +
                    padIn + "\"class\": \"" + escapeJson(pred.getClass().getSimpleName()) + "\"\n" +
                    padEnd + "}";
        }
    }
    private static String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }


}