package HW4.IRCode;

import AST.Nodes.ProgramNode;

import java.util.ArrayList;
import java.util.List;

//result = operand1 oper operand2
//result = oper operand1

public class BasedInstructions {
    private final ProgramNode annotated_AST;
    private final List<String> code;
    public int temp_counter =0;//t1 t2
    public int label_counter =0;//L1 L2


    public BasedInstructions(ProgramNode annotatedAst) {
        annotated_AST = annotatedAst;
        this.code = new ArrayList<>();
    }

    public String newTemp(){
        this.temp_counter++;
        return "t" + temp_counter;
    }

    public String newLabel(){
        this.label_counter++;
        return "L" + label_counter;
    }

    public void addToCode(String row_code){
        code.add(row_code);
    }


    public List<String> getCode() {
        return code;
    }
}
