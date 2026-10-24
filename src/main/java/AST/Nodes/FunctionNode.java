package AST.Nodes;
import AST.NodesInterfaces.ASTNode;
import AST.NodesInterfaces.ExprNode;

import java.util.List;

//класс узла функции
public class FunctionNode implements ASTNode {
    public final String name;
    public final List<ParamNode> params;
    public final List<ParamNode> returns;
    public final ExprNode requires; // предикат (может быть null)
    public final ExprNode ensures;  // предикат (может быть null)
    public final BlockNode body;

    public FunctionNode(String name, List<ParamNode> params, List<ParamNode> returns, ExprNode requires, ExprNode ensures, BlockNode body) {
        this.name = name;
        this.params = params;
        this.returns = returns;
        this.requires = requires;
        this.ensures = ensures;
        this.body = body;
    }
}