package HW3;
import AST.Nodes.ExprNodes.*;
import AST.Nodes.FunctionNode;
import AST.Nodes.ParamNode;
import AST.NodesInterfaces.ExprNode;

import java.lang.reflect.Type;
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

            //l==r
            if ((!leftT.equals(Scope.INT) && !leftT.equals(Scope.UNKNOWN)) ||
                    (!rightT.equals(Scope.INT) && !rightT.equals(Scope.UNKNOWN))) {
                errors.add(new SemanticError("Оператор '" + ((BinaryOpNode) expr).op + "' требует int операнды", 0, 0));
            }
            resolveType = Scope.INT;
        }

        else if (expr instanceof UnaryOpNode){
            String operT = resolveType(((UnaryOpNode) expr).expr, scope);
            if (!operT.equals(Scope.INT) && !operT.equals(Scope.UNKNOWN)) {
                errors.add(new SemanticError("Унарный минус требует int", 0, 0));
            }
        }

        else if (expr instanceof ArrayAccessNode){
            ArrayAccessNode arr = (ArrayAccessNode) expr;


        }

    }

}
