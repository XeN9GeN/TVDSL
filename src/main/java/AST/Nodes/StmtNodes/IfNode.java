package AST.Nodes.StmtNodes;

import AST.NodesInterfaces.BaseStmtNode;
import AST.NodesInterfaces.PredNode;
import AST.NodesInterfaces.StmtNode;


//if cond stmt else stmt
public class IfNode extends BaseStmtNode {
    public final PredNode condition;
    public final StmtNode thenBranch;
    public final StmtNode elseBranch;

    public IfNode(PredNode condition, StmtNode thenBranch, StmtNode elseBranch) {
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }
}