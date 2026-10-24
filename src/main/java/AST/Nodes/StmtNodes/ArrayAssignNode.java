package AST.Nodes.StmtNodes;

import AST.NodesInterfaces.ExprNode;
import AST.NodesInterfaces.StmtNode;


//a[i] = expr
public class ArrayAssignNode implements StmtNode {
    public final String arrayName;
    public final ExprNode index;
    public final ExprNode value;

    public ArrayAssignNode(String arrayName, ExprNode index, ExprNode value) {
        this.arrayName = arrayName;
        this.index = index;
        this.value = value;
    }
}