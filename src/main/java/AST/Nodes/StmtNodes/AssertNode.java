package AST.Nodes.StmtNodes;

import AST.NodesInterfaces.BaseStmtNode;
import AST.NodesInterfaces.PredNode;
import AST.NodesInterfaces.StmtNode;

public class AssertNode extends BaseStmtNode {
    public final PredNode predicate;
    public AssertNode(PredNode predicate) { this.predicate = predicate; }
}