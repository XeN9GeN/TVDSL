package HW4.Optimizations;

import HW4.Blocks.Block;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class Optimizer {
    private final Predicate<Block> applyFoldConst = FoldConst::applyFoldConst;
    private final Predicate<Block> applySimAlg = SimplifyAlgem::applySimAlg;
    private final BiPredicate<Block, Set<String>> applyDCE = LDCE::applyDCE;

    public Optimizer(){}

    public void optimize(List<Block> blockList, boolean foldConst, boolean simpleAlg, boolean DCE){
        for(Block block : blockList){
            boolean changed = true;

            Set<String> liveOutVars = new HashSet<>(LDCE.getLiveOutVars());
            for (Block succ : block.blocks_to) {
                for (String inst : succ.label_body) {
                    LDCE.extractVars(inst, liveOutVars);
                }
            }

            while(changed){
                changed = false;
                if (foldConst){
                    if (applyFoldConst.test(block)) changed = true;
                }
                if(simpleAlg){
                    if(applySimAlg.test(block)) changed = true;
                }
                if(DCE){
                    if (applyDCE.test(block,liveOutVars)) changed = true;
                }
            }
        }
    }
}
