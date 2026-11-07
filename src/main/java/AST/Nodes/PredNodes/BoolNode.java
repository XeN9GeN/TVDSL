package AST.Nodes.PredNodes;


import AST.NodesInterfaces.BasePredNode;
import AST.NodesInterfaces.PredNode;

//true false
public class BoolNode extends BasePredNode {
    public final boolean value;
    public BoolNode(boolean value) { this.value = value; }
}
