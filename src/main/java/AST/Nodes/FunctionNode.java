package AST.Nodes;
import AST.Nodes.StmtNodes.BlockNode;
import AST.NodesInterfaces.ASTNode;
import AST.NodesInterfaces.PredNode;

import java.util.List;

//класс узла функции
public class FunctionNode implements ASTNode {
    public final String name;
    public final List<ParamNode> params;
    public final List<ParamNode> returns;
    public final PredNode requires; // предикат (может быть null)
    public final PredNode ensures;  // предикат (может быть null)
    public final BlockNode body;

    public FunctionNode(String name, List<ParamNode> params, List<ParamNode> returns, PredNode  requires, PredNode ensures, BlockNode body) {
        this.name = name;
        this.params = params;
        this.returns = returns;
        this.requires = requires;
        this.ensures = ensures;
        this.body = body;
    }
}