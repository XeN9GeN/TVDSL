package AST.Nodes.ExprNodes;
import AST.NodesInterfaces.BaseExprNode;
import AST.NodesInterfaces.ExprNode;


//a[i]
public class ArrayAccessNode  extends BaseExprNode {
    public final String arrayName;
    public final ExprNode index;

    public ArrayAccessNode(String arrayName, ExprNode index) {
        this.arrayName = arrayName;
        this.index = index;
    }
}