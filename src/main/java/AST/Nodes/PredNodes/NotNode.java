package AST.Nodes.PredNodes;


import AST.NodesInterfaces.BasePredNode;
import AST.NodesInterfaces.PredNode;

//not A
public class NotNode extends BasePredNode {
    public final PredNode expr;
    public NotNode(PredNode expr) { this.expr = expr; }
}