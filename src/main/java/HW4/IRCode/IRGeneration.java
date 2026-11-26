package HW4.IRCode;

import AST.Nodes.ExprNodes.*;
import AST.Nodes.FunctionNode;
import AST.Nodes.PredNodes.BoolNode;
import AST.Nodes.PredNodes.ComparisonNode;
import AST.Nodes.PredNodes.LogicalOpNode;
import AST.Nodes.PredNodes.NotNode;
import AST.Nodes.ProgramNode;
import AST.Nodes.StmtNodes.*;
import AST.NodesInterfaces.ExprNode;
import AST.NodesInterfaces.PredNode;
import AST.NodesInterfaces.StmtNode;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.join;

public class IRGeneration {
    private final BasedInstructions basedInstructions;

    public IRGeneration(ProgramNode ast){
        this.basedInstructions = new BasedInstructions(ast);
    }
    public List<String> getGeneratedCode() {
        return basedInstructions.getCode();
    }

    public String generateExprString(ExprNode node) {
        switch (node) {
            case IntNode intNode -> {
                int val = intNode.getValue();
                return Integer.toString(val);
            }

            case VarNode varNode -> {
                return varNode.name;
            }
            case BinaryOpNode binaryOpNode -> {
                String right = generateExprString(binaryOpNode.r);
                String left = generateExprString(binaryOpNode.l);

                String temp = basedInstructions.newTemp();
                basedInstructions.addToCode(temp + " = " + left + " " + binaryOpNode.op + " " + right);
                return temp;
            }

            case ArrayAccessNode arrayAccessNode -> {//x = a[i]
                String index = generateExprString(arrayAccessNode.index);

                String temp = basedInstructions.newTemp();
                basedInstructions.addToCode(temp + " = " + arrayAccessNode.arrayName + "[" + index + "]");
                return temp;
            }

            case FuncCallNode funcCallNode -> {
                List<String> params = new ArrayList<>();
                for (ExprNode arg : funcCallNode.args) {
                    params.add(generateExprString(arg));
                }

                String temp = basedInstructions.newTemp();
                basedInstructions.addToCode(temp + " = call " + funcCallNode.functionName + ", " + join(", ", params));
                return temp;
            }

            case UnaryOpNode unaryOpNode -> {
                String res = generateExprString(unaryOpNode.expr);

                String temp = basedInstructions.newTemp();
                basedInstructions.addToCode(temp + " = " + unaryOpNode.op + res);
                return temp;
            }

            case null, default -> {
                return "WTF is that node?";
            }
        }
    }

    public void generateJump(PredNode cond, String true_L, String false_L){
        switch (cond){

            case ComparisonNode comparisonNode ->{
                String left = generateExprString(comparisonNode.left);
                String right = generateExprString(comparisonNode.right);

                basedInstructions.addToCode("if " + left + " " + comparisonNode.op + " " + right + " goto " + true_L);
                basedInstructions.addToCode("goto " + false_L);
            }

            case LogicalOpNode logicalOpNode ->{
                if (logicalOpNode.op.equals("and")){
                    String mid_L = basedInstructions.newLabel();
                    generateJump(logicalOpNode.left,  mid_L, false_L);
                    basedInstructions.addToCode("L " + mid_L);
                    generateJump(logicalOpNode.right, true_L, false_L);
                }

                if (logicalOpNode.op.equals("or")){
                    String mid_L = basedInstructions.newLabel();
                    generateJump(logicalOpNode.left, true_L, mid_L);
                    basedInstructions.addToCode("L " + mid_L);
                    generateJump(logicalOpNode.right, true_L, false_L);
                }
            }

            case NotNode notNode ->{
                generateJump(notNode.expr, false_L, true_L);
            }

            case BoolNode boolNode ->{
                if (!boolNode.value){
                    basedInstructions.addToCode("goto " + false_L);
                }
                else basedInstructions.addToCode("goto " + true_L);
            }

            default -> throw new IllegalStateException("Unexpected value: " + cond);
        }
    }

    public void generateStmt(StmtNode stmtNode){
        switch (stmtNode){
            case AssignNode assignNode ->{// x = a+b
                String value = generateExprString(assignNode.value);
                basedInstructions.addToCode(assignNode.target + " = " + value);
            }

            case ArrayAssignNode assignNode ->{//a[i] = val
                String index = generateExprString(assignNode.index);
                String value = generateExprString(assignNode.value);
                basedInstructions.addToCode(assignNode.arrayName + "[" + index + "] = " + value);
            }

            case IfNode ifNode ->{
                String trueL = basedInstructions.newLabel();
                String falseL = basedInstructions.newLabel();
                String endL = basedInstructions.newLabel();

                generateJump(ifNode.condition, trueL, falseL);

                basedInstructions.addToCode("L " + trueL);//then
                generateStmt(ifNode.thenBranch);
                basedInstructions.addToCode("goto " + endL);

                basedInstructions.addToCode("L " + falseL);//else
                if(ifNode.elseBranch != null){
                    generateStmt(ifNode.elseBranch);
                }

                basedInstructions.addToCode("L " + endL);
            }

            case WhileNode whileNode ->{// LWh {while pred} goto {L1 stmt goto Lwh} goto Lend
                //i=0 while i<5 i++ return i
                String startL = basedInstructions.newLabel();
                String bodyL = basedInstructions.newLabel();
                String endL = basedInstructions.newLabel();

                basedInstructions.addToCode("L " + startL);
                generateJump(whileNode.condition, bodyL, endL);

                basedInstructions.addToCode("L " + bodyL);
                generateStmt(whileNode.body);
                basedInstructions.addToCode("goto " + startL);

                basedInstructions.addToCode("L " + endL);
            }

            case BlockNode blockNode ->{
                for(StmtNode s : blockNode.statements){
                    generateStmt(s);
                }
            }

            case AssertNode assertNode -> {
                basedInstructions.addToCode("assert " + generatePredicateString(assertNode.predicate));
            }

            case AssumeNode assumeNode -> {
                basedInstructions.addToCode("assume " + generatePredicateString(assumeNode.predicate));
            }

            default -> throw new IllegalStateException("Unexpected value: " + stmtNode);
        }
    }

    public String generatePredicateString(PredNode node) {
        switch (node) {
            case ComparisonNode comparisonNode -> {
                String left = generateExprString(comparisonNode.left);
                String right = generateExprString(comparisonNode.right);

                return left + " " + comparisonNode.op + " " + right;
            }

            case LogicalOpNode logicalOpNode -> {
                return "(" + generatePredicateString(logicalOpNode.left)
                        + " " + logicalOpNode.op + " "
                        + generatePredicateString(logicalOpNode.right) + ")";
            }

            case NotNode notNode -> {
                return "not (" + generatePredicateString(notNode.expr) + ")";
            }

            case BoolNode boolNode -> {
                return Boolean.toString(boolNode.value);
            }

            default -> throw new IllegalStateException(
                    "Unexpected predicate: " + node);
        }
    }

    public void generateProgram(ProgramNode program) {
        for (FunctionNode func : program.functions) {
            String params = func.params.stream()
                    .map(p -> p.name)
                    .collect(java.util.stream.Collectors.joining(", "));

            String returns = func.returns.stream()
                    .map(p -> p.name)
                    .collect(java.util.stream.Collectors.joining(", "));

            basedInstructions.addToCode(
                    "Function " + func.name + "(" + params + ") returns " + returns + ":");

            generateStmt(func.body);

            basedInstructions.addToCode("Return " + returns);
        }
    }
}
