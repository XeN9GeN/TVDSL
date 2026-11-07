package AST.Nodes.StmtNodes;

import AST.NodesInterfaces.BaseStmtNode;
import AST.NodesInterfaces.PredNode;
import AST.NodesInterfaces.StmtNode;

public class AssumeNode extends BaseStmtNode {
    public final PredNode predicate;
    public AssumeNode(PredNode predicate) { this.predicate = predicate; }
}