package AST.NodesInterfaces;

public abstract class BaseExprNode implements ExprNode {
    private String annType;
    public int line =0;
    public int col =0;

    @Override
    public String getAnnType(){
        return annType;
    }

    @Override
    public void setAnnType(String anT){
        this.annType = anT;
    }
}
