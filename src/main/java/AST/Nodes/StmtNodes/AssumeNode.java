package AST.Nodes.StmtNodes;

import AST.NodesInterfaces.PredNode;
import AST.NodesInterfaces.StmtNode;

public class AssumeNode implements StmtNode {
    public final PredNode predicate;
    public AssumeNode(PredNode predicate) { this.predicate = predicate; }
}