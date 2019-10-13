package assignment_2.Test;

import assignment_2.Controller.TerrainController;
import assignment_2.Model.Terrain;
import assignment_2.View.TerrainView;

public class TerrainTest {
    public static void main(String[] args) {
        Terrain model = new Terrain();
        TerrainView view = new TerrainView();
        TerrainController controller = new TerrainController(model,view);

        view.registerController(controller);
    }
}
