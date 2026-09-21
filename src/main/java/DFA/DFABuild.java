package DFA;

import NFA.NFAState;

import java.util.*;

public class DFABuild {

    //все состояния по eps-переходам
    public static Set<NFAState> epsClosure(Set<NFAState> states){
        Set<NFAState> closure = new HashSet<>(states);
        Stack<NFAState> stack = new Stack<>();

        for(NFAState nfa : states){
            stack.push(nfa);
        }

        while(!stack.isEmpty()){
            NFAState cur = stack.pop();
            for(NFAState next_eps_state : cur.e_transitions){
                if(!closure.contains(next_eps_state)){
                    closure.add(next_eps_state);
                    stack.push(next_eps_state);
                }
            }
        }
        return closure;
    }

    //куда идти по с без учёта eps-переходов
    public static Set<NFAState> moveWithСhar(Set<NFAState> states, char c){
        Set<NFAState> res = new HashSet<>();
        for(NFAState nfa : states){

            List<NFAState> next_states = nfa.transitions.get(c);
            if(next_states != null){
                res.addAll(next_states);
            }
        }
        return res;
    }

    public static List<DFAState> NFAConvertDFA(NFAState nfa_start, Set<Character> regular){
        int dfa_id_count = 0;
        List<DFAState> final_dfa_states = new ArrayList<>();
        Queue<DFAState> new_unmark_dfa_states = new LinkedList<>();//для новых состояний, для которых ещё не просчитали переходы

        //против дублей. Для набора НКА состояний А сделали ДКА состояние Б
        Map<Set<NFAState>,DFAState> nfa_dfa_builded_states = new HashMap<>();
        Set<NFAState> start_nfa_box = epsClosure(Collections.singleton(nfa_start));//мн-во НКА состояний, в которых сразу можем быть

        DFAState dfa_start = new DFAState(dfa_id_count++, start_nfa_box);
        final_dfa_states.add(dfa_start);
        new_unmark_dfa_states.add(dfa_start);
        nfa_dfa_builded_states.put(start_nfa_box, dfa_start);

        while(!new_unmark_dfa_states.isEmpty()){
            DFAState cur_dfa = new_unmark_dfa_states.poll();

            for(char c: regular){
                Set<NFAState> move_char_states = moveWithСhar(cur_dfa.nfa_states, c);
                if(move_char_states.isEmpty()){
                    continue;
                };

                Set<NFAState> target_closure = epsClosure(move_char_states);//добор eps-переходов после шага по букве
                DFAState next_dfa_check = nfa_dfa_builded_states.get(target_closure);

                if(next_dfa_check == null){
                    next_dfa_check = new DFAState(dfa_id_count++, target_closure);
                    final_dfa_states.add(next_dfa_check);
                    new_unmark_dfa_states.add(next_dfa_check);
                    nfa_dfa_builded_states.put(target_closure, next_dfa_check);
                }

                cur_dfa.transitions.put(c, next_dfa_check);
            }
        }
        return final_dfa_states;
    }



}
