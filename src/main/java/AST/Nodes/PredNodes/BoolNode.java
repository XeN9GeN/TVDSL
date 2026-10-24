package AST.Nodes.PredNodes;


import AST.NodesInterfaces.PredNode;

//true false
public class BoolNode implements PredNode {
    public final boolean value;
    public BoolNode(boolean value) { this.value = value; }
}
