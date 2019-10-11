package assignment_2.Test;

import assignment_2.Controller.TerrainController;
import assignment_2.Model.Terrain;
import assignment_2.View.TerrainView;

public class TerrainTest {
    public static void main(String[] args) {
        Terrain terrain = new Terrain();
        TerrainView terrainView = new TerrainView();
        TerrainController terrainController = new TerrainController();

        terrainView.registerController(terrainController);
    }


}
