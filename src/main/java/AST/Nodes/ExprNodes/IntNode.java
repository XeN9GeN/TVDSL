package AST.Nodes.ExprNodes;
import AST.NodesInterfaces.BaseExprNode;
import AST.NodesInterfaces.ExprNode;


//23
public class IntNode extends BaseExprNode {
    public final int value;
    public IntNode(int value) { this.value = value; }
}
