package com.example.fujak.viciouscircle;

import java.util.ArrayList;

/**
 * Created by Fujak on 11/6/2017.
 */

public class DataManager {

    public static ArrayList<Obstacle> loadLevel(){
        ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();

        Obstacle o = new Obstacle(500,100, 20, -500);
        obstacles.add(o);
        return obstacles;
    }
}
