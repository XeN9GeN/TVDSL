
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

        List<Set<String>> liveIn = new ArrayList<>();
        List<Set<String>> liveOut = new ArrayList<>();

        for (int i = 0; i < blockCount; i++) {
            liveIn.add(new HashSet<>());
            liveOut.add(new HashSet<>());
        }

        boolean changed = true;

        while (changed) {
            changed = false;

            for (Block block : blockList) {
                if (foldConst && applyFoldConst.test(block)) {
                    changed = true;
                }

                if (simpleAlg && applySimAlg.test(block)) {
                    changed = true;
                }
            }

            if (DCE) {
                boolean livenessChanged = true;

                while (livenessChanged) {
                    livenessChanged = false;

                    for (int i = blockCount - 1; i >= 0; i--) {
                        Block block = blockList.get(i);

                        Set<String> newLiveOut = new HashSet<>();

                        if (block.blocks_to.isEmpty() && liveOutVars != null) {
                            newLiveOut.addAll(liveOutVars);
                        }

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
                            livenessChanged = true;
                        }

                        if (!newLiveIn.equals(liveIn.get(i))) {
                            liveIn.set(i, newLiveIn);
                            livenessChanged = true;
                        }
                    }
                }

                for (int i = blockCount - 1; i >= 0; i--) {
                    if (applyDCE.test(blockList.get(i), liveOut.get(i))) {
                        changed = true;
                    }
                }
            }
        }
    }
}
