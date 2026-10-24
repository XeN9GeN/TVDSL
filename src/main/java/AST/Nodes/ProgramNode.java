package AST.Nodes;
import AST.NodesInterfaces.ASTNode;

import java.util.List;

//весь код, внутри лежат функции
public class ProgramNode implements ASTNode {
    public final List<FunctionNode> functions;
    public ProgramNode(List<FunctionNode> functions) { this.functions = functions; }
}