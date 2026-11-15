package HW4.Optimizations;

import HW4.Blocks.Block;

public class SimplifyAlgem {
    public static boolean applySimAlg(Block block){
        boolean changed = false;

        for(int i=0;i<block.label_body.size();i++){
            String instance = block.label_body.get(i);
            String[] parts = instance.split(" ");

            if(parts.length == 5 && parts[1].equals("=")){
                String dest = parts[0];
                String op1 = parts[2];
                String oper = parts[3];
                String op2 = parts[4];

                if(oper.equals("+") && op2.equals("0")){
                    String q = dest + " = " + op1;
                    block.label_body.set(i, q);
                    changed = true;
                }
                else if(oper.equals("+") && op1.equals("0")){
                    String q = dest + " = " + op2;
                    block.label_body.set(i, q);
                    changed = true;
                }
                else if (oper.equals("*") && op2.equals("1")) {
                    block.label_body.set(i, dest + " = " + op1);
                    changed = true;
                }
                else if (oper.equals("*") && op1.equals("1")) {
                    block.label_body.set(i, dest + " = " + op2);
                    changed = true;
                }
                else if (oper.equals("*") && (op1.equals("0") || op2.equals("0"))) {
                    block.label_body.set(i, dest + " = 0");
                    changed = true;
                }
                else if (oper.equals("-") && op2.equals("0")) {
                    block.label_body.set(i, dest + " = " + op1);
                    changed = true;
                }
                else if (oper.equals("/") && op2.equals("1")) {
                    block.label_body.set(i, dest + " = " + op1);
                    changed = true;
                }
            }
        }
        return changed;
    }
}
