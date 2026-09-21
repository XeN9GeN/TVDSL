package NFA;

public class NFABuild {
    private int state_counter = 0;

    private NFAState createNewState(){
        return new NFAState(state_counter++);
    }

    public NFMiniA fromChar(char c){
        NFAState start = createNewState();
        NFAState accept = createNewState();

        start.addTransition(c,accept);
        return new NFMiniA(start,accept);
    }

    public NFMiniA concatenate(NFMiniA a, NFMiniA b){
        a.finish_state.addETransition(b.start_state);
        return new NFMiniA(a.start_state, b.finish_state);
    }

    public NFMiniA or(NFMiniA a, NFMiniA b){
        NFAState new_start = createNewState();
        NFAState new_finish = createNewState();

        new_start.addETransition(a.start_state);
        new_start.addETransition(b.start_state);

        a.finish_state.addETransition(new_finish);
        b.finish_state.addETransition(new_finish);

        return new NFMiniA(new_start, new_finish);
    }

    public NFMiniA cliniStar(NFMiniA a){
        NFAState new_start = createNewState();
        NFAState new_finish = createNewState();

        a.finish_state.addETransition(a.start_state);
        a.finish_state.addETransition(new_finish);
        new_start.addETransition(new_finish);
        new_start.addETransition(a.start_state);

        return new NFMiniA(new_start,new_finish);
    }

    public NFMiniA fromString(String s){
        NFMiniA result = fromChar(s.charAt(0));
        for (int i = 1; i < s.length(); i++){
            result = concatenate(result, fromChar(s.charAt(i)));
        }
        return result;
    }


}
