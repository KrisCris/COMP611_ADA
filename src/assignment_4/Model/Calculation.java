package assignment_4.Model;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Observable;
import java.util.Scanner;
import java.util.Stack;
import java.util.StringTokenizer;

/**
 * This class implements the Observer pattern and acts as a Model of Calculator, the view.
 * There will be no further explanation for each method since their name are straightforward.
 */
public class Calculation extends Observable {

    private String formula;
    private ScriptEngine jse;
    private int errorIndex;

    /**
     * We have already written a calculator in our first year and I do not want to write that again,
     * also algorithms of this assignment are not focusing on this part,
     * so the real calculation is handled by ScriptEngine.
     *
     */
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

    public void del() {
        if (formula.equals("Infinity") || formula.equals("NaN")){
            this.clear();
        }
        if (this.formula.length() > 0) {
            this.errorIndex = -1;
            this.formula = this.formula.substring(0, this.formula.length() - 1);
            if (this.formula.length() == 0) {
                clear();
            }
            this.setChanged();
            this.notifyObservers();
        }

    }

    public void addOperators(String op) {
        if (formula.equals("Infinity") || formula.equals("NaN")){
            return;
        }
        this.errorIndex = -1;

        if (!this.formula.equals("0")) {
            if (Character.isDigit(formula.charAt(formula.length() - 1)) ||
                    formula.charAt(formula.length() - 1) == ')') {
                this.formula += op;
                this.setChanged();
                this.notifyObservers();
            }
        }


    }

    public void addDot() {
        if (formula.equals("Infinity") || formula.equals("NaN")){
            return;
        }
        StringTokenizer st = new StringTokenizer(formula,"+-×÷()%");
        String str = "";
        while(st.hasMoreTokens()){
            str = st.nextToken();
            System.out.println(str);
        }
        if (Character.isDigit(formula.charAt(formula.length()-1))){
            if (!str.contains(".")){
                formula += ".";
            }
        }

        setChanged();
        notifyObservers();
    }

    public void addDigit(String digit) {
        if (formula.equals("Infinity") || formula.equals("NaN")){
            return;
        }
        this.errorIndex = -1;
        if (this.formula.equals("0")) {
            this.formula = digit;
        } else {
            this.formula += digit;
        }

        this.setChanged();
        this.notifyObservers();
    }

    public void addBracket(String str) {
        if (formula.equals("Infinity") || formula.equals("NaN")){
            return;
        }
        if (formula.equals("0")) {
            if (str.equals("("))
                formula = str;
        } else {
            Stack<Character> stk = new Stack<>();
            for (char c : formula.toCharArray()) {
                if (c == '(') {
                    stk.push(c);
                } else if (c == ')' && !stk.empty()) {
                    stk.pop();
                }
            }

            if (str.equals("(")) {
                if (!Character.isDigit(formula.charAt(formula.length() - 1)) && formula.charAt(formula.length() - 1) != '.') {
                    this.formula += str;
                }
            } else {
                if ((Character.isDigit(formula.charAt(formula.length() - 1)) || formula.charAt(formula.length() - 1) == ')') && !stk.empty()) {
                    this.formula += str;
                }
            }

        }

        this.setChanged();
        this.notifyObservers();
    }

    public void getResult() {
        this.errorIndex = -1;
        if (formula.equals("Infinity") || formula.equals("NaN")){
            return;
        }
        String str = formula + "";
        str = str.replace('×', '*').replace('÷', '/');

        try {
            Object result = jse.eval(str);
            System.out.println(result);
            this.formula = result.toString();

        } catch (ScriptException e) {
            this.errorIndex = e.getColumnNumber();
        } finally {
            this.setChanged();
            this.notifyObservers();
        }
    }

    public String getFormula() {
        return formula;
    }

    public int getErrorIndex() {
        return errorIndex;
    }
}
