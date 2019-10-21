package assignment_2.Controller;

import assignment_2.Model.Terrain;
import assignment_2.View.TerrainView;

public class TerrainController {
    TerrainView view;
    Terrain model;

    public TerrainController(Terrain model,TerrainView view){
        this.view = view;
        this.model = model;
    }




}
