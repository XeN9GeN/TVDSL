package DFA;
import java.util.HashMap;
import java.util.Map;


public class DFAInterpreter {

    public static class Result {
        public final boolean success;
        public final String token_type;

        public Result(boolean success, String token_type) {
            this.success = success;
            this.token_type = token_type;
        }
    }


    public static Result validateString(String input, DFAAutomaton automaton) {
        Map<Integer, DFAState> states_map = new HashMap<>();
        for (DFAState s : automaton.states) {
            states_map.put(s.dfa_state_id, s);
        }
        int current_id = automaton.start_state_id;


        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c > 127) {
                current_id = automaton.trap_state_id;
                break;
            }

            DFAState cur_state = states_map.get(current_id);
            if (cur_state != null && cur_state.transitions.containsKey(c)) {
                current_id = cur_state.transitions.get(c).dfa_state_id;
            }
            else {
                current_id = automaton.trap_state_id;
                break;
            }
        }

        DFAState final_state = states_map.get(current_id);
        if (final_state != null && final_state.is_final_state && current_id != automaton.trap_state_id) {
            return new Result(true, final_state.token_final_type);
        }
        else {
            return new Result(false, null);
        }
    }
}
