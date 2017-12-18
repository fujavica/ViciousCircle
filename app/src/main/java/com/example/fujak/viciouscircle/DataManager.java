package com.example.fujak.viciouscircle;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Fujak on 11/6/2017.
 */

public class DataManager {

    public static ArrayList<Obstacle> loadLevel(int w, int h){
        ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();

        int obstacle_width = (int) ((w *0.40) + 0.5);
        Obstacle o = new Obstacle(obstacle_width,100, 20, -500);
        obstacles.add(o);
        return obstacles;

    }
}
