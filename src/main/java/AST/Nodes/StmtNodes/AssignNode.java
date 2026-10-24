package AST.Nodes.StmtNodes;

import AST.NodesInterfaces.ExprNode;
import AST.NodesInterfaces.StmtNode;


//x=expr
public class AssignNode implements StmtNode {
    public final String target;
    public final ExprNode value;
    public AssignNode(String target, ExprNode value) {
        this.target = target;
        this.value = value;
    }
}