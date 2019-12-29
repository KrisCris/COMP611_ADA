package assignment_4.Model;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Observable;

public class Calculation extends Observable {

    private String formula;
    private ScriptEngine jse;
    private int errorIndex;

    public Calculation() {
        this.jse = new ScriptEngineManager().getEngineByName("JavaScript");
        clear();
    }

    public void clear() {
        this.formula = "0";
        this.errorIndex = -1;
        this.setChanged();
        this.notifyObservers();
    }

    public void addOperators(String op, int index) {
        this.errorIndex = -1;

        this.formula += op;


        this.setChanged();
        this.notifyObservers();
    }

    public void addDot(int index) {

    }

    public void addDigit(String digit, int index) {
        this.errorIndex = -1;
        this.formula += digit;


        this.setChanged();
        this.notifyObservers();
    }

    public void addBracket(int pos) {

    }

    public void getResult() {
        this.errorIndex = -1;

        String str = formula + "";
        str = str.replace('×', '*').replace('÷', '/');

        try {
            Object result = jse.eval(str);
            System.out.println(result);
            this.formula = result.toString();

        } catch (ScriptException e) {
            e.printStackTrace();
            this.errorIndex = e.getColumnNumber();
        } finally {
            this.setChanged();
            this.notifyObservers();
        }
    }

    public void validateChange(String str) {
        this.errorIndex = -1;
        String chars = "1234567890E+-×÷%.()";
        str = str.replace('/', '÷').replace('*', '×').replace('e', 'E').replace(" ","");
        System.out.println(str);
        for (int i = 0; i < str.length(); i++) {
            String c = str.charAt(i) + "";
            if (!chars.contains(c)) {
                str = str.substring(0, i) + str.substring(i + 1);
            }
        }
        while (str.contains("..")){
            str = str.replace("..",".");
        }
        while (str.contains("EE")){
            str = str.replace("EE","E");
        }
        while (str.contains("++")){
            str = str.replace("++","+");
        }
        while (str.contains("--")){
            str = str.replace("--","-");
        }
        while (str.contains("××")){
            str = str.replace("××","×");
        }
        while (str.contains("÷÷")){
            str = str.replace("÷÷","÷");
        }
        while (str.contains("%%")){
            str = str.replace("%%","%");
        }
        formula = str;

        this.setChanged();
        this.notifyObservers();

    }

    public String getFormula() {
        return formula;
    }

    public int getErrorIndex() {
        return errorIndex;
    }
}
