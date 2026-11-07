package AST.Nodes.StmtNodes;

import AST.NodesInterfaces.BaseStmtNode;
import AST.NodesInterfaces.ExprNode;
import AST.NodesInterfaces.StmtNode;


//x=expr
public class AssignNode extends BaseStmtNode {
    public final String target;
    public final ExprNode value;
    public AssignNode(String target, ExprNode value) {
        this.target = target;
        this.value = value;
    }
}