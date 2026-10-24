package AST.Nodes.ExprNodes;
import AST.NodesInterfaces.ExprNode;


//23
public class IntNode implements ExprNode {
    public final int value;
    public IntNode(int value) { this.value = value; }
}
