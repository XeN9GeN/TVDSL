package HW4.Blocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CFGBlocks {

    public static List<Block> buildCFG(List<String> IRcode){
        List<Block> blocks = new ArrayList<>();
        Map<String, Block> labelToBlock = new HashMap<>();

        if (IRcode == null || IRcode.isEmpty()) return blocks;

        boolean[] is_leader = new boolean[IRcode.size()];
        is_leader[0] = true;


        for (int i=0; i<IRcode.size(); i++){
            String instance = IRcode.get(i);

            if (instance.startsWith("L ") || instance.startsWith("Function ")){
                is_leader[i] = true;
            }

            if(instance.startsWith("goto") || instance.startsWith("if ")){
                if(i+1<IRcode.size()) is_leader[i+1] = true;
            }
        }

        Block cur_block = null;
        for(int i=0; i<IRcode.size();i++){
            String instance = IRcode.get(i);//строка

            if(is_leader[i]){
                String label = "";

                if(instance.startsWith("L "))
                    label = instance.substring(2);
                else
                    label = "B" + blocks.size();

                blocks.add(cur_block = new Block(label));

                if(instance.startsWith("L ")){
                    labelToBlock.put(label, cur_block);
                }
            }
            cur_block.addLBody(instance);
        }


        for(int i=0; i<blocks.size();i++){
            Block block = blocks.get(i);
            String last_instance = block.label_body.getLast();//last sentence in this block

            if(last_instance.startsWith("goto ")){
                String target_label = last_instance.substring(5).trim();//to which label goto
                Block target_block = labelToBlock.get(target_label);

                if(target_block!=null){
                    block.addBlockTo(target_block);
                }
            }

            if (last_instance.startsWith("Return")) {
                continue;
            }

            else if (last_instance.startsWith("goto ")) {
                String target_label = last_instance.substring(5).trim();
                Block target_block = labelToBlock.get(target_label);

                if (target_block == null) {
                    throw new IllegalStateException(
                            "Unknown label: " + target_label);
                }

                block.addBlockTo(target_block);
            }

            else if(last_instance.startsWith("if ")){//if a>b goto L?
                int gotoIdx = last_instance.indexOf("goto ");
                String target_label = last_instance.substring(gotoIdx + 5).trim();
                Block target_block = labelToBlock.get(target_label);

                if(target_block!=null){//true
                    block.addBlockTo(target_block);
                }
                if(i+1<blocks.size()){//false
                    block.addBlockTo(blocks.get(i+1));
                }

            }

            else{
                if(i+1<blocks.size()){
                    block.addBlockTo(blocks.get(i+1));//just go to the next
                }
            }
        }
        return blocks;
    }
}
