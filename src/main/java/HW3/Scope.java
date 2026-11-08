package HW3;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Scope {
    private final Map<String, String> vars = new HashMap<>();//int int[]
    private final Scope parent;

    //если имя переменной передаётся в функцию как параметр, то она read-only, т.е её НЕЛЬЗЯ переизменять в теле функции куда она идёт как параметр
    private final Set<String> readOnlyNames = new HashSet<>();


    public static final String UNKNOWN = "UNKNOWN";
    public static final String INT = "int";
    public static final String INT_ARRAY = "int[]";


    public Scope(Scope parent) {
        this.parent = parent;
    }

    public void declare(String name, String type){
        declare(name, type, false);
    }

    public void declare(String name, String type, boolean readOnly) {
        vars.put(name, type);
        if (readOnly) {
            readOnlyNames.add(name);
        } else {
            readOnlyNames.remove(name);
        }
    }

    public String getType(String name){
        if (vars.containsKey(name)){//local
            return vars.get(name);
        }
        if(parent!=null){//global
            return parent.getType(name);
        }
        return null;//если имени вообще нет в области видимости
    }

    public boolean isDeclared(String name) {
        if (vars.containsKey(name)) return true;
        return parent != null && parent.isDeclared(name);
    }

    public boolean isReadOnly(String name) {
        if (vars.containsKey(name)) return readOnlyNames.contains(name);
        return parent != null && parent.isReadOnly(name);
    }


    public Map<String, String> getAllVars() {
        return vars;
    }


}
