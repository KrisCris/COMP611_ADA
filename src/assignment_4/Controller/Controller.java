package assignment_4.Controller;

import assignment_4.Model.Model;
import assignment_4.View.View;

import java.awt.event.ActionListener;

public interface Controller extends ActionListener {
    public void bindView(View view);

    public void bindModel(Model model);
}
