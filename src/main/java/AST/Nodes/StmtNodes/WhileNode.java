package AST.Nodes.StmtNodes;

import AST.NodesInterfaces.PredNode;
import AST.NodesInterfaces.StmtNode;


//while (cond) invariant (Pred) stmt
public class WhileNode implements StmtNode {
    public final PredNode condition;
    public final PredNode invariant;//спросить нужно ли? То, что внутрянка цикла сама по себе работает правильно
    public final StmtNode body;

    public WhileNode(PredNode condition, PredNode invariant, StmtNode body) {
        this.condition = condition;
        this.invariant = invariant;
        this.body = body;
    }
}