package HW4.Blocks;

import java.util.ArrayList;
import java.util.List;


public class Block {
    public String label;//block name
    public List<String> label_body = new ArrayList<>();

    public List<Block> blocks_to = new ArrayList<>();//куда идём из этого
    public List<Block> blocks_into = new ArrayList<>();//кто ведёт сюда

    public Block(String label){
        this.label = label;
    }

    public void addLBody(String inst){
        label_body.add(inst);
    }

    public void addBlockTo(Block go_to){
        if(!blocks_to.contains(go_to)){
            blocks_to.add(go_to);
            go_to.blocks_into.add(this);
        }
    }

    @Override
    public String toString() {
        return label;
    }
}