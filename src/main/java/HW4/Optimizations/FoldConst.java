package HW4.Optimizations;


import HW4.Blocks.Block;

import java.util.Arrays;
import java.util.List;

public class FoldConst {
    public static boolean applyFoldConst(Block block){
        boolean changed = false;

        for(int i=0; i<block.label_body.size();i++){
            String instance = block.label_body.get(i);

            String[] parts = instance.split(" ");
            if(parts.length == 5 && parts[1].equals("=")){//res = op1 oper op2
                String op1 = parts[2];
                String oper = parts[3];
                String op2 = parts[4];

                if(isInteger(op1) && isInteger(op2)) {
                    int result = 0;
                    int val1 = Integer.parseInt(op1);
                    int val2 = Integer.parseInt(op2);
                    boolean validOperation = true;

                    switch (oper) {
                        case "+":
                            result = val1 + val2;
                            break;
                        case "-":
                            result = val1 - val2;
                            break;
                        case "*":
                            result = val1 * val2;
                            break;
                        case "/":
                            if (val2 == 0) {
                                validOperation = false;
                            } else {
                                result = val1 / val2;
                            }
                            break;
                        default:
                            validOperation = false;
                    }

                    if (validOperation) {
                        block.label_body.set(i, parts[0] + " = " + result);
                        changed = true;
                    }
                }
            }
        }
        return changed;
    }


    public static boolean isInteger(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
