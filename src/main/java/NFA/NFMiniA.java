package NFA;

public class NFMiniA {

    //Каждый базовый элемент регулярного выражения заменяется на маленький шаблон автомата
    //с одним входом (начальным состоянием) и одним выходом (конечным состоянием)
    public NFAState start_state;
    public NFAState finish_state;

    public NFMiniA(NFAState s, NFAState f){
        this.start_state = s;
        this.finish_state = f;
    }
}
