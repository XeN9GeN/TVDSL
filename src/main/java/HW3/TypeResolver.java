package HW3;
import AST.Nodes.ExprNodes.*;
import AST.Nodes.FunctionNode;
import AST.NodesInterfaces.ExprNode;
import java.util.List;
import java.util.Map;

public class TypeResolver {
    private final List<SemanticError> errors;
    private final Map<String, FunctionNode> global_functs;

    public TypeResolver(List<SemanticError> err, Map<String, FunctionNode> glb){
        this.errors = err;
        this.global_functs = glb;
    }

    public String resolveType(ExprNode expr, Scope scope){
        if(expr == null)
            return Scope.UNKNOWN;

        String resolveType = Scope.UNKNOWN;

        if(expr instanceof IntNode){
            resolveType = Scope.INT;
        }

        else if (expr instanceof VarNode){
            String name = (((VarNode) expr).name);
            String type = scope.getType(name);
            if(type!=null)
                resolveType = type;

            else if (!global_functs.containsKey(name)) {//before announcement
                errors.add(new SemanticError("Unknown variable: '" + name + "'", ((VarNode) expr).line,((VarNode) expr).col));
            }
        }

        else if (expr instanceof BinaryOpNode){
            String leftT = resolveType(((BinaryOpNode) expr).l, scope);
            String rightT = resolveType(((BinaryOpNode) expr).r, scope);

            if ((!leftT.equals(Scope.INT) && !leftT.equals(Scope.UNKNOWN)) ||
                    (!rightT.equals(Scope.INT) && !rightT.equals(Scope.UNKNOWN))) {
                errors.add(new SemanticError("Operator '" + ((BinaryOpNode) expr).op + "' requires int operands", ((BinaryOpNode) expr).line,((BinaryOpNode) expr).col));
            }
            resolveType = Scope.INT;
        }

        else if (expr instanceof UnaryOpNode){
            String operT = resolveType(((UnaryOpNode) expr).expr, scope);
            if (!operT.equals(Scope.INT) && !operT.equals(Scope.UNKNOWN)) {
                errors.add(new SemanticError("Unary minus requires int", ((UnaryOpNode)expr).line, ((UnaryOpNode)expr).col));
            }
            resolveType = Scope.INT;
        }

        else if (expr instanceof ArrayAccessNode){// x = a[1]
            ArrayAccessNode arr = (ArrayAccessNode) expr;
            String arrayT = scope.getType(arr.arrayName);

            if (arrayT == null) {
                errors.add(new SemanticError("Unknown array: '" + arr.arrayName + "'", arr.line, arr.col));
            }
            else if (!arrayT.equals(Scope.INT_ARRAY) && !arrayT.equals(Scope.UNKNOWN)) {
                errors.add(new SemanticError("Variable '" + arr.arrayName + "' is not an array (int[])", arr.line, arr.col));
            }

            String indexT = resolveType(arr.index, scope);
            if (!indexT.equals(Scope.INT) && !indexT.equals(Scope.UNKNOWN)) {
                errors.add(new SemanticError("Array index must be int", arr.line, arr.col));
            }
            resolveType = Scope.INT;
        }

        else if (expr instanceof FuncCallNode){
            FuncCallNode call = (FuncCallNode) expr;

            if (call.functionName.equals("length")) {
                if (call.args.size() != 1) {
                    errors.add(new SemanticError("length() takes exactly 1 argument", ((FuncCallNode)expr).line,((FuncCallNode)expr).col));
                }

                else {
                    String argT = resolveType(call.args.get(0), scope);
                    if (!argT.equals(Scope.INT_ARRAY) && !argT.equals(Scope.UNKNOWN)) {
                        errors.add(new SemanticError("length() takes only int[]", ((FuncCallNode)expr).line, ((FuncCallNode)expr).col));
                    }
                }
                resolveType = Scope.INT;
            }

            else {
                FunctionNode target_func = global_functs.get(call.functionName);

                if (target_func != null) {
                    if (call.args.size() != target_func.params.size()) {
                        errors.add(new SemanticError("Function '" + call.functionName + "' expects " + target_func.params.size() + " arguments, but " + call.args.size() + " were passed", ((FuncCallNode) expr).line, ((FuncCallNode) expr).col));
                    } else {
                        for (int i = 0; i < call.args.size(); i++) {
                            String argT = resolveType(call.args.get(i), scope);
                            String expecter_argT = target_func.params.get(i).type;

                            if (!argT.equals(expecter_argT) && !argT.equals(Scope.UNKNOWN)) {
                                errors.add(new SemanticError("Argument " + (i + 1) + " of function '" + call.functionName + "' expects " + expecter_argT + ", but " + argT + " was passed", ((FuncCallNode) expr).line, ((FuncCallNode) expr).col));
                            }
                        }
                    }

                    if (!target_func.returns.isEmpty()) {
                        resolveType = target_func.returns.get(0).type;
                    }
                } else {
                    errors.add(new SemanticError("Unknown function: '" + call.functionName + "'", ((FuncCallNode) expr).line, ((FuncCallNode) expr).col));
                }
            }
        }

        expr.setAnnType(resolveType);
        return resolveType;
    }
}