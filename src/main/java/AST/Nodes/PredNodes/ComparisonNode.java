package AST.Nodes.PredNodes;


import AST.NodesInterfaces.ExprNode;
import AST.NodesInterfaces.PredNode;

//x==y, r>=0
public class ComparisonNode implements PredNode {
    public final String op; //"==", "!="
    public final ExprNode left;
    public final ExprNode right;

    public ComparisonNode(String op, ExprNode left, ExprNode right) {
        this.op = op;
        this.left = left;
        this.right = right;
    }
}
