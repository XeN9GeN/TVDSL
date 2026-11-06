package AST.Nodes.ExprNodes;
import AST.NodesInterfaces.BaseExprNode;
import AST.NodesInterfaces.ExprNode;


//name of var
public class VarNode extends BaseExprNode {
    public final String name;
    public VarNode(String name) { this.name = name; }

}