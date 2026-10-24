package AST.Nodes.PredNodes;

import AST.Nodes.ParamNode;
import AST.NodesInterfaces.PredNode;


//requires forall (k: int | k >= 0 -> a[k] > 0)
public class QuantifierNode implements PredNode {
    public final String quantifier; //"forall" "exists"
    public final ParamNode variable; //переменная квантора (k: int)
    public final PredNode body; //условие

    public QuantifierNode(String quantifier, ParamNode variable, PredNode body) {
        this.quantifier = quantifier;
        this.variable = variable;
        this.body = body;
    }
}
