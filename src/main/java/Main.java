import DFA.DFABuild;
import DFA.DFAState;
import NFA.NFABuild;
import NFA.NFMiniA;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Set<Character> alph = new HashSet<>();

        for(int i=0;i<128;i++){
            alph.add((char) i);
        }

        NFABuild build = new NFABuild();

        NFMiniA nfMiniA = build.fromChar('0');
        nfMiniA.finish_state.is_final_state = true;
        nfMiniA.finish_state.token_final_type = "INT";

        List<DFAState> dfares = DFABuild.NFAConvertDFA(nfMiniA.start_state, alph);
        System.out.println("Determ is over\n");
        System.out.println("Count of dfa states: " + dfares.size());

    }
}
