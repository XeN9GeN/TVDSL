package AST.Nodes.PredNodes;

import AST.NodesInterfaces.PredNode;


//A or B, C and T
public class LogicalOpNode implements PredNode {
    public final String op; // "and", "or", "->"
    public final PredNode left;
    public final PredNode right;

    public LogicalOpNode(String op, PredNode left, PredNode right) {
        this.op = op;
        this.left = left;
        this.right = right;
    }
}