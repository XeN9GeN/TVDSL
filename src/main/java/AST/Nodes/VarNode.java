package AST.Nodes;

import AST.NodesInterfaces.ExprNode;

public class VarNode implements ExprNode {
    public final String name;
    public VarNode(String name) { this.name = name; }
}