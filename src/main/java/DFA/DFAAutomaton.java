package DFA;

import DFA.DFAState;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DFAAutomaton {
    public final List<DFAState> states;
    public final int start_state_id = 0;
    public final int trap_state_id = 999;

    public DFAAutomaton(List<DFAState> minimized_states, Set<Character> regular_alphabet) {
        this.states = minimized_states;

        DFAState trap_state = new DFAState(trap_state_id, new HashSet<>());
        trap_state.is_final_state = false;
        trap_state.token_final_type = "ERROR_TRAP";

        for (char c : regular_alphabet) {
            trap_state.transitions.put(c, trap_state);
        }

        for (DFAState state : states) {
            for (char c : regular_alphabet) {
                if (!state.transitions.containsKey(c)) {
                    state.transitions.put(c, trap_state);
                }
            }
        }
        this.states.add(trap_state);
    }


    public void saveToJsonFile(String filepath) throws IOException {
        try (FileWriter writer = new FileWriter(filepath)) {
            StringBuilder json = new StringBuilder();
            json.append("{\n");
            json.append("  \"start_state_id\": ").append(start_state_id).append(",\n");
            json.append("  \"trap_state_id\": ").append(trap_state_id).append(",\n");
            json.append("  \"states\": {\n");

            for (int i = 0; i < states.size(); i++) {
                json.append(states.get(i).toJson());
                if (i < states.size() - 1) {
                    json.append(",");
                }
                json.append("\n");
            }

            json.append("  }\n");
            json.append("}\n");
            writer.write(json.toString());
        }
    }
}
