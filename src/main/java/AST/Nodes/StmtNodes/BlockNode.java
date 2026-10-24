package AST.Nodes.StmtNodes;
import AST.NodesInterfaces.StmtNode;

import java.util.List;

//блок кода вида {...}
public class BlockNode implements StmtNode {
    public final List<StmtNode> statements;

    public BlockNode(List<StmtNode> statements) {
        this.statements = statements;
    }
}