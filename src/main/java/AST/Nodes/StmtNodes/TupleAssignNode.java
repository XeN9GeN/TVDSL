package AST.Nodes.StmtNodes;


import AST.Nodes.ExprNodes.FuncCallNode;
import AST.NodesInterfaces.StmtNode;

import java.util.List;

//q, r = divmod(a, b);
public class TupleAssignNode implements StmtNode {
    public final List<String> targets; //["q", "r"]
    public final FuncCallNode call ;//divmod(a, b)

    public TupleAssignNode(List<String> targets, FuncCallNode call) {
        this.targets = targets;
        this.call = call;
    }
}