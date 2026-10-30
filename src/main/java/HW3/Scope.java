package HW3;

import java.util.HashMap;
import java.util.Map;

public class Scope {
    private final Map<String, String> vars = new HashMap<>();//int int[]
    private final Scope parent;

    public static final String UNKNOWN = "UNKNOWN";
    public static final String INT = "int";
    public static final String INT_ARRAY = "int[]";

    public Scope(Scope parent) {
        this.parent = parent;
    }

    public void declare(String name, String type){
        vars.put(name, type);
    }

    public String getType(String name){
        if (vars.containsKey(name)){//local
            return vars.get(name);
        }
        if(parent!=null){//global
            return parent.getType(name);
        }
        return null;
    }

    public void updateType(String name, String newtype){
        if(vars.containsKey(name)){
            vars.put(name,newtype);
        }else if(parent!=null){
            parent.updateType(name,newtype);
        }
    }

    public boolean isDeclared(String name){
        return vars.containsKey(name);
    }

}
