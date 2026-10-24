package AST.Nodes.ExprNodes;
import AST.NodesInterfaces.ExprNode;


//x+y, a*b
public class BinaryOpNode implements ExprNode {
    public final String op; //"+", "-", "*", "/"
    public final ExprNode l;
    public final ExprNode r;

    public BinaryOpNode(String op, ExprNode left, ExprNode right){
        this.op=op;
        this.l = left;
        this.r = right;
    }
}
