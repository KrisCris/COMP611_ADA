package assignment_4.Model;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Observable;
import java.util.Scanner;
import java.util.Stack;
import java.util.StringTokenizer;

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

    public void del() {
        if (formula.equals("Infinity")){
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
        if (formula.equals("Infinity")){
            return;
        }
        this.errorIndex = -1;

        if (!this.formula.equals("0")) {
            if (Character.isDigit(formula.charAt(formula.length() - 1)) ||
                    formula.charAt(formula.length() - 1) == '(' ||
                    formula.charAt(formula.length() - 1) == ')') {
                this.formula += op;
                this.setChanged();
                this.notifyObservers();
            }
        }


    }

    public void addDot() {
        if (formula.equals("Infinity")){
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
        if (formula.equals("Infinity")){
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
        if (formula.equals("Infinity")){
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
        if (formula.equals("Infinity")){
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
