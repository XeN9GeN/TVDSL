package AST.Nodes.ExprNodes;
import AST.NodesInterfaces.ExprNode;


//name of var
public class VarNode implements ExprNode {
    public final String name;
    public VarNode(String name) { this.name = name; }
}