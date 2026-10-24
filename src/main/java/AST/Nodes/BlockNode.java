package AST.Nodes;
import AST.NodesInterfaces.ASTNode;

import java.util.List;

//блок кода вида {...}
public class BlockNode implements ASTNode {
    public final List<ASTNode> statements;

    public BlockNode(List<ASTNode> statements) {
        this.statements = statements;
    }
}