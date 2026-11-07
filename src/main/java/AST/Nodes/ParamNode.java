package AST.Nodes;

import AST.NodesInterfaces.ASTNode;
import AST.NodesInterfaces.BaseStmtNode;

//имя:тип
//variableDef = identifier ":" variableType;
//variableType = "int" | "int[]";
public class ParamNode extends BaseStmtNode {
    public final String name;
    public final String type;

    public ParamNode(String name, String type) {
        this.name = name;
        this.type = type;
    }
}