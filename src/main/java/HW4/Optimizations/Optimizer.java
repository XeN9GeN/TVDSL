package HW4.Optimizations;

import HW4.Blocks.Block;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class Optimizer {
    private final Predicate<Block> applyFoldConst = FoldConst::applyFoldConst;
    private final Predicate<Block> applySimAlg = SimplifyAlgem::applySimAlg;
    private final BiPredicate<Block, Set<String>> applyDCE = LDCE::applyDCE;

    public Optimizer(){}

    public void optimize(List<Block> blockList, boolean foldConst, boolean simpleAlg, boolean DCE, Set<String> liveOutVars) {
        if (blockList == null || blockList.isEmpty()) {
            return;
        }
        int blockCount = blockList.size();
        List<Set<String>> liveIn = new java.util.ArrayList<>();
        List<Set<String>> liveOut = new java.util.ArrayList<>();


        for (int i = 0; i < blockCount; i++) {
            liveIn.add(new HashSet<>());
            liveOut.add(new HashSet<>());
        }
        boolean changed = true;

        while (changed) {
            changed = false;

            for (int i = blockCount - 1; i >= 0; i--) {
                Block block = blockList.get(i);

                Set<String> newLiveOut = new HashSet<>(liveOutVars);

                for (Block successor : block.blocks_to) {
                    int successorIndex = blockList.indexOf(successor);

                    if (successorIndex != -1) {
                        newLiveOut.addAll(liveIn.get(successorIndex));
                    }
                }

                Set<String> newLiveIn = new HashSet<>(newLiveOut);

                for (int j = block.label_body.size() - 1; j >= 0; j--) {
                    LDCE.updateLiveness(block.label_body.get(j), newLiveIn);
                }

                if (!newLiveOut.equals(liveOut.get(i))) {
                    liveOut.set(i, newLiveOut);
                    changed = true;
                }

                if (!newLiveIn.equals(liveIn.get(i))) {
                    liveIn.set(i, newLiveIn);
                    changed = true;
                }
            }
        }

        for (int i = 0; i < blockCount; i++) {
            Block block = blockList.get(i);

            boolean blockChanged = true;

            while (blockChanged) {
                blockChanged = false;

                if (foldConst && applyFoldConst.test(block)) {
                    blockChanged = true;
                }

                if (simpleAlg && applySimAlg.test(block)) {
                    blockChanged = true;
                }

                if (DCE && LDCE.applyDCE(block, liveOut.get(i))) {
                    blockChanged = true;
                }
            }
        }
    }
}
