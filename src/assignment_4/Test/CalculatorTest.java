package assignment_4.Test;

import assignment_4.Controller.CalculatorController;
import assignment_4.Model.Calculation;
import assignment_4.View.Windows.Calculator;

public class CalculatorTest {

    public static void main(String[] args) {
        Calculator view = new Calculator();
        Calculation model = new Calculation();
        CalculatorController controller = new CalculatorController(view,model);

        view.registerController(controller);
        model.addObserver(view);

        controller.start();
    }
}
