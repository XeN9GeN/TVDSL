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
        }

        else if (expr instanceof BinaryOpNode){
            String leftT = resolveType(((BinaryOpNode) expr).l, scope);
            String rightT = resolveType(((BinaryOpNode) expr).r, scope);

            if ((!leftT.equals(Scope.INT) && !leftT.equals(Scope.UNKNOWN)) ||
                    (!rightT.equals(Scope.INT) && !rightT.equals(Scope.UNKNOWN))) {
                errors.add(new SemanticError("Оператор '" + ((BinaryOpNode) expr).op + "' требует int операнды", ((BinaryOpNode) expr).line,((BinaryOpNode) expr).col));
            }
            resolveType = Scope.INT;
        }

        else if (expr instanceof UnaryOpNode){
            String operT = resolveType(((UnaryOpNode) expr).expr, scope);
            if (!operT.equals(Scope.INT) && !operT.equals(Scope.UNKNOWN)) {
                errors.add(new SemanticError("Унарный минус требует int", ((UnaryOpNode)expr).line, ((UnaryOpNode)expr).col));
            }
            resolveType = Scope.INT;
        }

        else if (expr instanceof ArrayAccessNode){
            ArrayAccessNode arr = (ArrayAccessNode) expr;
            String arrayT = scope.getType(arr.arrayName);

            if (arrayT != null && !arrayT.equals(Scope.INT_ARRAY) && !arrayT.equals(Scope.UNKNOWN)) {
                errors.add(new SemanticError("Переменная '" + arr.arrayName + "' не является массивом (int[])", ((ArrayAccessNode)expr).line, ((ArrayAccessNode)expr).col));
            }

            String indexT = resolveType(arr.index, scope);
            if (!indexT.equals(Scope.INT) && !indexT.equals(Scope.UNKNOWN)) {
                errors.add(new SemanticError("Индекс массива должен быть int", ((ArrayAccessNode)expr).line,((ArrayAccessNode)expr).col));
            }
            resolveType = Scope.INT;
        }

        else if (expr instanceof FuncCallNode){
            FuncCallNode call = (FuncCallNode) expr;

            if (call.functionName.equals("length")) {
                if (call.args.size() != 1) {
                    errors.add(new SemanticError("length() принимает ровно 1 аргумент", ((FuncCallNode)expr).line,((FuncCallNode)expr).col));
                }

                else {
                    String argT = resolveType(call.args.get(0), scope);
                    if (!argT.equals(Scope.INT_ARRAY) && !argT.equals(Scope.UNKNOWN)) {
                        errors.add(new SemanticError("length() принимает только int[]", ((FuncCallNode)expr).line, ((FuncCallNode)expr).col));
                    }
                }
                resolveType = Scope.INT;
            }

            else {
                FunctionNode target_func = global_functs.get(call.functionName);

                if (target_func != null) {
                    if (call.args.size() != target_func.params.size()) {
                        errors.add(new SemanticError("Функция '" + call.functionName + "' ожидает " + target_func.params.size() + " аргументов, передано " + call.args.size(), ((FuncCallNode) expr).line, ((FuncCallNode) expr).col));
                    } else {
                        for (int i = 0; i < call.args.size(); i++) {
                            String argT = resolveType(call.args.get(i), scope);
                            String expecter_argT = target_func.params.get(i).type;

                            if (!argT.equals(expecter_argT) && !argT.equals(Scope.UNKNOWN)) {
                                errors.add(new SemanticError("Аргумент " + (i + 1) + " функции '" + call.functionName + "' ожидает " + expecter_argT + ", передан " + argT, ((FuncCallNode) expr).line, ((FuncCallNode) expr).col));
                            }
                        }
                    }

                    if (!target_func.returns.isEmpty()) {
                        resolveType = target_func.returns.get(0).type;
                    }
                } else {
                    errors.add(new SemanticError("Неизвестная функция: '" + call.functionName + "'", ((FuncCallNode) expr).line, ((FuncCallNode) expr).col));
                }
            }
        }

        expr.setAnnType(resolveType);
        return resolveType;
    }
}
