package AST.Nodes.StmtNodes;
import AST.NodesInterfaces.BaseStmtNode;
import AST.NodesInterfaces.StmtNode;

import java.util.List;

//блок кода вида {...}
public class BlockNode extends BaseStmtNode {
    public final List<StmtNode> statements;

    public BlockNode(List<StmtNode> statements) {
        this.statements = statements;
    }
}