package AST.Nodes;

import AST.NodesInterfaces.ASTNode;

//имя:тип
//variableDef = identifier ":" variableType;
//variableType = "int" | "int[]";
public class ParamNode implements ASTNode {
    public final String name;
    public final String type;

    public ParamNode(String name, String type) {
        this.name = name;
        this.type = type;
    }
}