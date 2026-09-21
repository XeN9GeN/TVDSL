package DFA;
import NFA.NFAState;

import java.util.*;

public class MinimizeDFA {
    //1.разделить состояния на принимающий и промежуточный классы
    //2.каждый класс проверить на раскол(по букве можно переходить только по стейтам того же класса) пока это возможно
    //3.каждый класс становится одним единственным состоянием нового мин. ДКА


    public static boolean checkStatesEquivalence(DFAState s1, DFAState s2,Set<Character> regular, List<Set<DFAState>> eqal_classes){
        if(s1.is_final_state != s2.is_final_state) return false;
        if(s1.is_final_state && !Objects.equals(s1.token_final_type,s2.token_final_type)) return false;

        for(char c : regular){
            DFAState next_state_1 = s1.transitions.get(c);
            DFAState next_state_2 = s2.transitions.get(c);

            int class_ind1 = findClassInd(next_state_1,eqal_classes);
            int class_ind2 = findClassInd(next_state_2, eqal_classes);
            if(class_ind2!= class_ind1) return false;
        }
        return true;
    }

    public static int findClassInd(DFAState target_state, List<Set<DFAState>>eqal_classes){
        if (target_state == null) return -1;
        for(int i=0;i<eqal_classes.size();i++){
            if(eqal_classes.get(i).contains(target_state)){
                return i;
            }
        }
        return -1;
    }


    public static List<DFAState> minimizeDFA(List<DFAState> final_dfa_states, Set<Character> regular){
        if(final_dfa_states.isEmpty())
            return final_dfa_states;

        List<Set<DFAState>> eqal_classes = new ArrayList<>();
        Set<DFAState> final_accept_states = new HashSet<>();
        Set<DFAState> intermediate_non_accepting_states = new HashSet<>();


        for(DFAState cur_dfa : final_dfa_states){
            if(cur_dfa.is_final_state){
                final_accept_states.add(cur_dfa);
            }
            else{
                intermediate_non_accepting_states.add(cur_dfa);
            }
        }
        if(!final_accept_states.isEmpty())
            eqal_classes.add(final_accept_states);
        if(!intermediate_non_accepting_states.isEmpty())
            eqal_classes.add(intermediate_non_accepting_states);


        boolean is_class_changed = true;
        while(is_class_changed){
            is_class_changed = false;
            List<Set<DFAState>> refined_classes = new ArrayList<>();

            for(Set<DFAState> cur_class : eqal_classes){
                if(cur_class.size()<=1){
                    refined_classes.add(cur_class);
                    continue;
                }

                DFAState class_representative = cur_class.iterator().next();
                Set<DFAState> stays_with_representative = new HashSet<>();
                Set<DFAState> splits_away_from_representative = new HashSet<>();
                stays_with_representative.add(class_representative);

                for(DFAState target_dfa : cur_class){
                    if(target_dfa == class_representative)
                        continue;

                    if(checkStatesEquivalence(target_dfa,class_representative,regular,eqal_classes)){
                        stays_with_representative.add(target_dfa);
                    }
                    else{
                        splits_away_from_representative.add(target_dfa);
                    }
                }
                refined_classes.add(stays_with_representative);
                if(!splits_away_from_representative.isEmpty()){//раскол класса cur_class на два
                    refined_classes.add(splits_away_from_representative);
                    is_class_changed = true;
                }
            }
            eqal_classes = refined_classes;
        }
        DFAState orig_start_state = final_dfa_states.get(0);
        return rebuildMinimizedDFA(eqal_classes, orig_start_state, regular);
    }

    public static List<DFAState> rebuildMinimizedDFA(List<Set<DFAState>> final_classes, DFAState original_start, Set<Character> regular){
        List<DFAState> mini_dfa_states = new ArrayList<>();
        Map<Set<DFAState>, DFAState> class_to_new_dfa_map = new HashMap<>();
        int dfa_id_count = 0;


        for(Set<DFAState> cur_class : final_classes){
            if(cur_class.contains(original_start)){

                Set<NFAState> comb_nfa_states = new HashSet<>();
                for(DFAState dfa_s : cur_class){
                    comb_nfa_states.addAll(dfa_s.nfa_states);
                }

                DFAState dfa_mini_start = new DFAState(dfa_id_count++, comb_nfa_states);
                DFAState represent = cur_class.iterator().next();
                dfa_mini_start.is_final_state = represent.is_final_state;//дфа состояние из одинаковых
                dfa_mini_start.token_final_type = represent.token_final_type;

                mini_dfa_states.add(dfa_mini_start);
                class_to_new_dfa_map.put(cur_class, dfa_mini_start);
                break;
            }
        }


        for(Set<DFAState> cur_class : final_classes) {
            if(cur_class.contains(original_start)) continue;

            Set<NFAState> comb_nfa_states = new HashSet<>();
            for(DFAState dfa_s : cur_class){
                comb_nfa_states.addAll(dfa_s.nfa_states);
            }

            DFAState dfa_mini_state = new DFAState(dfa_id_count++, comb_nfa_states);
            DFAState represent = cur_class.iterator().next();
            dfa_mini_state.is_final_state = represent.is_final_state;
            dfa_mini_state.token_final_type = represent.token_final_type;

            mini_dfa_states.add(dfa_mini_state);
            class_to_new_dfa_map.put(cur_class, dfa_mini_state);
        }


        for(Set<DFAState> cur_class : final_classes){
            DFAState root_dfa = class_to_new_dfa_map.get(cur_class);
            DFAState represent = cur_class.iterator().next();

            for(char c : regular){
                DFAState old_target_state = represent.transitions.get(c);
                    if(old_target_state!= null){
                        int target_class_ind = findClassInd(old_target_state, final_classes);
                        Set<DFAState> target_class  = final_classes.get(target_class_ind);
                        DFAState new_target_dfa = class_to_new_dfa_map.get(target_class);

                        root_dfa.transitions.put(c,new_target_dfa);
                    }
            }
        }
        return mini_dfa_states;
    }
}
