package NFA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NFAState {
    public final int nfa_state_id;
    public final Map<Character, List<NFAState>> transitions = new HashMap<>();
    public final List<NFAState> e_transitions = new ArrayList<>();

    public boolean is_final_state = false;
    public String token_final_type = null;
    public int priority = 0;// IDENT - key words

    public NFAState(int id){
        this.nfa_state_id = id;
    }


    public void addTransition(char c, NFAState new_state){
        List<NFAState> state_list = transitions.get(c);

        if(state_list == null){
            state_list = new ArrayList<>();
            transitions.put(c,state_list);
        }
        state_list.add(new_state);
        //transitions.computeIfAbsent(c, k -> new ArrayList<>()).add(nextState);
    }

    public void addETransition(NFAState new_state){
        e_transitions.add(new_state);
    }
}
