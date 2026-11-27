package HW4.Optimizations;

import HW4.Blocks.Block;

import java.util.HashMap;
import java.util.Map;

public class FoldConst {
    public static boolean applyFoldConst(Block block){
        boolean changed = false;
        Map<String, Integer> constants = new HashMap<>();

        for(int i=0; i<block.label_body.size();i++){
            String instance = block.label_body.get(i).trim();
            String[] parts = instance.split("\\s+");

            if(parts.length == 3 && parts[1].equals("=")){
                String dest = parts[0];
                String value = parts[2];

                if(dest.contains("[")){
                    continue;
                }

                if(isInteger(value)){
                    constants.put(dest, Integer.parseInt(value));
                }
                else if(constants.containsKey(value)){
                    int constant = constants.get(value);
                    constants.put(dest, constant);
                    block.label_body.set(i, dest + " = " + constant);
                    changed = true;
                }
                else{
                    constants.remove(dest);
                }

                continue;
            }

            if(parts.length == 5 && parts[1].equals("=")){
                String dest = parts[0];

                if(dest.contains("[")){
                    continue;
                }

                String op1 = parts[2];
                String oper = parts[3];
                String op2 = parts[4];

                if(constants.containsKey(op1)){
                    op1 = constants.get(op1).toString();
                }

                if(constants.containsKey(op2)){
                    op2 = constants.get(op2).toString();
                }

                if(isInteger(op1) && isInteger(op2)){
                    int result = 0;
                    int val1 = Integer.parseInt(op1);
                    int val2 = Integer.parseInt(op2);
                    boolean validOperation = true;

                    switch(oper){
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
                            if(val2 == 0){
                                validOperation = false;
                            }
                            else{
                                result = val1 / val2;
                            }
                            break;
                        default:
                            validOperation = false;
                    }

                    if(validOperation){
                        block.label_body.set(i, dest + " = " + result);
                        constants.put(dest, result);
                        changed = true;
                    }
                    else{
                        constants.remove(dest);
                    }
                }
                else{
                    constants.remove(dest);
                }

                continue;
            }

            if(instance.contains("=") && !instance.startsWith("if ")){
                String dest = instance.split("=", 2)[0].trim();

                if(!dest.contains("[")){
                    constants.remove(dest);
                }
            }
        }

        return changed;
    }

    public static boolean isInteger(String str){
        if(str == null || str.isEmpty()){
            return false;
        }

        try{
            Integer.parseInt(str);
            return true;
        }
        catch(NumberFormatException e){
            return false;
        }
    }
}