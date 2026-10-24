package AST.Nodes.PredNodes;


import AST.NodesInterfaces.PredNode;

//not A
public class NotNode implements PredNode {
    public final PredNode expr;
    public NotNode(PredNode expr) { this.expr = expr; }
}