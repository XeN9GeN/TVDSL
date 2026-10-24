package AST.Nodes.StmtNodes;

import AST.NodesInterfaces.PredNode;
import AST.NodesInterfaces.StmtNode;

public class AssertNode implements StmtNode {
    public final PredNode predicate;
    public AssertNode(PredNode predicate) { this.predicate = predicate; }
}