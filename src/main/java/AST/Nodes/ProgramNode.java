package AST.Nodes;
import AST.NodesInterfaces.ASTNode;
import AST.NodesInterfaces.BaseStmtNode;

import java.util.List;

//весь код, внутри лежат функции
public class ProgramNode extends BaseStmtNode {
    public final List<FunctionNode> functions;
    public ProgramNode(List<FunctionNode> functions) { this.functions = functions; }
}