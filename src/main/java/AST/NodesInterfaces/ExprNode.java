package AST.NodesInterfaces;

//return only numbers and things with them
public interface ExprNode extends ASTNode {
    String getAnnType();
    void setAnnType(String anT);
}
