package DFA;

import NFA.NFAState;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DFAState {
    public final int dfa_state_id;
    public final Set<NFAState> nfa_states;//дубликаты, для развилок при переходах из состояния куда-то
    public final Map<Character, DFAState> transitions = new HashMap<>();

    public boolean is_final_state = false;
    public String token_final_type  = null;


    public DFAState(int id, Set<NFAState> nfa_states){
        this.dfa_state_id = id;
        this.nfa_states = (nfa_states == null) ? new HashSet<>() : nfa_states;

        NFAState selected_final = null;
        for (NFAState nfa : this.nfa_states) {
            if (nfa.is_final_state && (selected_final == null || nfa.priority > selected_final.priority)) {
                selected_final = nfa;
            }
        }

        if (selected_final != null) {
            this.is_final_state = true;
            this.token_final_type = selected_final.token_final_type;
        }
    }

    public String toJson() {
        StringBuilder json = new StringBuilder();
        json.append("    \"").append(dfa_state_id).append("\": {\n");
        json.append("      \"is_final\": ").append(is_final_state).append(",\n");
        json.append("      \"token_type\": ").append(token_final_type == null ? "null" : "\"" + token_final_type + "\"").append(",\n");
        json.append("      \"transitions\": {\n");

        int i = 0;
        for (Map.Entry<Character, DFAState> entry : transitions.entrySet()) {
            char c = entry.getKey();
            String escapedChar;
            if (c == '\n') escapedChar = "\\n";
            else if (c == '\r') escapedChar = "\\r";
            else if (c == '\t') escapedChar = "\\t";
            else if (c == '\\') escapedChar = "\\\\";
            else if (c == '"') escapedChar = "\\\"";
            else if (c < 0x20 || c == 0x7F) escapedChar = String.format("\\u%04x", (int) c);
            else escapedChar = String.valueOf(c);

            json.append("        \"").append(escapedChar).append("\": ").append(entry.getValue().dfa_state_id);
            if (i < transitions.size() - 1) {
                json.append(",");
            }
            json.append("\n");
            i++;
        }
        json.append("      }\n");
        json.append("    }");
        return json.toString();
    }
}
