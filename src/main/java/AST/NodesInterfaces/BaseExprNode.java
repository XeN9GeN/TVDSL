package AST.NodesInterfaces;

public abstract class BaseExprNode implements ExprNode {
    private String annType;

    @Override
    public String getAnnType(){
        return annType;
    }

    @Override
    public void setAnnType(String anT){
        this.annType = anT;
    }
}
