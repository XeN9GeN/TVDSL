package HW3;

import AST.Nodes.FunctionNode;

import AST.Nodes.*;
import AST.Nodes.StmtNodes.*;
import AST.NodesInterfaces.StmtNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CheckAnalyzer {
    private final List<SemanticError> errors = new ArrayList<>();
    private final Map<java.lang.String, FunctionNode> global_funcs = new HashMap<>();
    private final Scope globalfunc_scope = new Scope(null);

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

        //global_funcs.put("length", null);

        for(FunctionNode f: programm.functions){
            analyzeFunc(f);
        }
    }

    public void analyzeFunc(FunctionNode f){
        Scope local_scope = new Scope(globalfunc_scope);

        for(ParamNode p: f.params){
            if(local_scope.isDeclared(p.name)){
                errors.add(new SemanticError("repeated announs: '" + f.name + "'", 0, 0));
            }
            local_scope.declare(p.name, p.type);
        }

        for(ParamNode p: f.returns){
            if(local_scope.isDeclared(p.name)){
                errors.add(new SemanticError("repeated announs: '" + f.name + "'", 0, 0));
            }
            local_scope.declare(p.name, p.type);
        }

        //TODO user parse?

        if(f.requires!=null) visitPred(f.requires, local_scope);
        if(f.ensures!=null) visitPred(f.ensures, local_scope);

        visitStmt(f.body, local_scope);//body func
    }

    private void visitStmt(StmtNode stmt, Scope loc_scope){
        if(stmt instanceof BlockNode){
            for(StmtNode s: ((BlockNode) stmt).statements){
                visitStmt(s, loc_scope);
            }
        }

        else if(stmt instanceof AssignNode){
            AssignNode asg = (AssignNode) stmt;
            visitExpr(asg.value, loc_scope);

            if(!loc_scope.isDeclared(asg.target)){
                loc_scope.declare(asg.target, Scope.UNKNOWN);
            }
        }

        else if(stmt instanceof ArrayAssignNode){
            ArrayAssignNode arrayAssignNode = (ArrayAssignNode) stmt;
            String type = loc_scope.getType(arrayAssignNode.arrayName);

            if(type == null){
                errors.add(new SemanticError("unknown array: '" + arrayAssignNode.arrayName + "'", 0, 0));

            }
            else if(!type.equals(Scope.INT_ARRAY)){
                errors.add(new SemanticError("Переменная '" + arrayAssignNode.arrayName + "' не является массивом", 0, 0));
            }

            visitExpr(arrayAssignNode.index, loc_scope);
            visitExpr(arrayAssignNode.value, loc_scope);
        }

        else if(stmt instanceof IfNode){
            IfNode ifNode = (IfNode) stmt;
            visitPred(ifNode.condition, loc_scope);
            visitPred(ifNode.thenBranch, loc_scope);
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



}
