package HW4.Optimizations;

import HW4.Blocks.Block;

import java.util.List;
import java.util.function.Predicate;

public class Optimizer {
    private final Predicate<Block> applyFoldConst = FoldConst::applyFoldConst;
    private final Predicate<Block> applySimAlg = SimplifyAlgem::applySimAlg;
    private final Predicate<Block> applyDCE = LDCE::applyDCE;

    public Optimizer(){}

    public void optimize(List<Block> blockList, boolean foldConst, boolean simpleAlg, boolean DCE){
        for(Block block : blockList){
            boolean changed = true;

            while(changed){
                changed = false;
                if (foldConst){
                    if (applyFoldConst.test(block)) changed = true;
                }
                if(simpleAlg){
                    if(applySimAlg.test(block)) changed = true;
                }
                if(DCE){
                    if (applyDCE.test(block)) changed = true;
                }
            }
        }
    }
}
