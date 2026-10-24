package AST.Nodes.ExprNodes;
import AST.NodesInterfaces.ExprNode;
import java.util.List;


//add(x.y), length(str)
public class FuncCallNode implements ExprNode {
    public final String functionName;
    public final List<ExprNode> args;

    public FuncCallNode(String functionName, List<ExprNode> args) {
        this.functionName = functionName;
        this.args = args;
    }
}