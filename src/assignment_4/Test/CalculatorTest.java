package assignment_4.Test;

import assignment_4.Controller.CalculatorController;
import assignment_4.Model.Calculation;
import assignment_4.View.Windows.Calculator;

public class CalculatorTest {

    public static void main(String[] args) {
        /**
         * Instantiate MVC objects.
         */
        Calculator view = new Calculator();
        Calculation model = new Calculation();
        CalculatorController controller = new CalculatorController(view,model);

        /**
         * Bind the Controller to View.
         * And make View as the Observer of Model.
         */
        view.registerController(controller);
        model.addObserver(view);

        /**
         * Run the application.
         */
        controller.start();
    }
}
