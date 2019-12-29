package assignment_4;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class test {
    public static void main(String[] args) throws ScriptException {
        ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript");

        String indexFormula = "2E *9";
        Object aa = jse.eval(indexFormula);
        System.out.println(aa);

    }
}
