package AST.Nodes.ExprNodes;
import AST.NodesInterfaces.ExprNode;


//-23, -(x+y)
public class UnaryOpNode implements ExprNode {
    public final String op;
    public final ExprNode expr;

    public UnaryOpNode(String op, ExprNode expr) {
        this.op = op;
        this.expr = expr;
    }
}