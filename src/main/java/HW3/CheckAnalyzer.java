package HW3;

import AST.Nodes.ExprNodes.*;
import AST.Nodes.FunctionNode;

import AST.Nodes.*;
import AST.Nodes.PredNodes.ComparisonNode;
import AST.Nodes.PredNodes.LogicalOpNode;
import AST.Nodes.PredNodes.NotNode;
import AST.Nodes.StmtNodes.*;
import AST.NodesInterfaces.PredNode;
import AST.NodesInterfaces.StmtNode;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CheckAnalyzer {
    private final List<SemanticError> errors = new ArrayList<>();
    private final Map<java.lang.String, FunctionNode> global_funcs = new HashMap<>();
    private final Scope globalfunc_scope = new Scope(null);
    private final TypeResolver typeResolver;

    public CheckAnalyzer(){
        this.typeResolver = new TypeResolver(errors, global_funcs);
    }



    public List<SemanticError> getErrors(){
        return errors;
    }

    public void analyze(ProgramNode programm){
        for(FunctionNode f: programm.functions){
            if(global_funcs.containsKey(f.name)){
                errors.add(new SemanticError("repeated announs: '" + f.name + "'", 0, 0));
            }
            else{
                global_funcs.put(f.name, f);
                globalfunc_scope.declare(f.name, "function");
            }
        }

        for(FunctionNode f: programm.functions){
            analyzeFunc(f);
        }
    }

    public void analyzeFunc(FunctionNode f){
        Scope local_scope = new Scope(globalfunc_scope);

        for(ParamNode p: f.params){
            if(local_scope.isDeclared(p.name)){
                errors.add(new SemanticError("repeated announs: '" + f.name + "'", p.line, p.col));
            }
            local_scope.declare(p.name, p.type);
        }

        for(ParamNode p: f.returns){
            if(local_scope.isDeclared(p.name)){
                errors.add(new SemanticError("repeated announs: '" + f.name + "'", p.line, p.col));
            }
            local_scope.declare(p.name, p.type);
        }

        if(f.requires!=null) visitPred(f.requires, local_scope);
        if(f.ensures!=null) visitPred(f.ensures, local_scope);

        visitStmt(f.body, local_scope);
    }

    private void visitStmt(StmtNode stmt, Scope loc_scope){
        if(stmt instanceof BlockNode){
            for(StmtNode s: ((BlockNode) stmt).statements){
                visitStmt(s, loc_scope);
            }
        }

        else if(stmt instanceof AssignNode) {
            AssignNode asg = (AssignNode) stmt;

            String leftType = loc_scope.getType(asg.target);
            String rightType = typeResolver.resolveType(asg.value, loc_scope);

            if (leftType == null) {
                loc_scope.declare(asg.target, Scope.UNKNOWN);
            }
            else {
                if (!leftType.equals(Scope.UNKNOWN) && !rightType.equals(Scope.UNKNOWN) && !leftType.equals(rightType)) {
                    errors.add(new SemanticError("Несовместимые типы при присваивании: нельзя записать " + rightType + " в " + leftType, asg.line, asg.col));
                }
            }
        }

        else if(stmt instanceof ArrayAssignNode){
            ArrayAssignNode arrayAssignNode = (ArrayAssignNode) stmt;
            String type = loc_scope.getType(arrayAssignNode.arrayName);

            if(type == null){
                errors.add(new SemanticError("unknown array: '" + arrayAssignNode.arrayName + "'", arrayAssignNode.line, arrayAssignNode.col));

            }
            else if(!type.equals(Scope.INT_ARRAY)){
                errors.add(new SemanticError("Переменная '" + arrayAssignNode.arrayName + "' не является массивом", arrayAssignNode.line, arrayAssignNode.col));
            }

            String indexType = typeResolver.resolveType(arrayAssignNode.index, loc_scope);
            if (!indexType.equals(Scope.INT) && !indexType.equals(Scope.UNKNOWN)) {
                errors.add(new SemanticError("Индекс массива должен быть типа int", arrayAssignNode.line, arrayAssignNode.col));
            }

            String valueType = typeResolver.resolveType(arrayAssignNode.value, loc_scope);
            if (!valueType.equals(Scope.INT) && !valueType.equals(Scope.UNKNOWN)) {
                errors.add(new SemanticError("Значение для массива должно быть типа int", arrayAssignNode.line, arrayAssignNode.col));
            }
        }

        else if(stmt instanceof IfNode){
            IfNode ifNode = (IfNode) stmt;
            visitPred(ifNode.condition, loc_scope);
            visitStmt(ifNode.thenBranch, loc_scope);
            if(ifNode.elseBranch != null) visitStmt(ifNode.elseBranch, loc_scope);
        }

        else if(stmt instanceof WhileNode){
            WhileNode whileNode = (WhileNode) stmt;
            visitPred(whileNode.condition, loc_scope);
            if(whileNode.invariant != null) visitPred(whileNode.invariant, loc_scope);
            visitStmt(whileNode.body, loc_scope);
        }

        else if(stmt instanceof AssertNode){
            visitPred(((AssertNode) stmt).predicate,loc_scope);
        }
        else if(stmt instanceof  AssumeNode){
            visitPred(((AssumeNode) stmt).predicate,loc_scope);
        }
    }

    public void visitPred(PredNode pred, Scope scope){
        if (pred instanceof ComparisonNode) {
            ComparisonNode cmp = (ComparisonNode) pred;

            String leftT = typeResolver.resolveType(cmp.left, scope);
            String rightT = typeResolver.resolveType(cmp.right, scope);

            if ((!leftT.equals(Scope.INT) && !leftT.equals(Scope.UNKNOWN)) || (!rightT.equals(Scope.INT) && !rightT.equals(Scope.UNKNOWN))) {
                errors.add(new SemanticError("Оператор сравнения '" + cmp.op + "' требует int операнды", cmp.line, cmp.col));
            }
        }

        else if (pred instanceof LogicalOpNode) {
            visitPred(((LogicalOpNode) pred).left, scope);
            visitPred(((LogicalOpNode) pred).right, scope);
        }
        else if (pred instanceof NotNode) {
            visitPred(((NotNode) pred).expr, scope);
        }
    }
}
