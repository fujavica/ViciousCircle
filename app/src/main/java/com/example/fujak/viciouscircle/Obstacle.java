package com.example.fujak.viciouscircle;

import java.util.ArrayList;

/**
 * Created by Fujak on 11/6/2017.
 */

public class Obstacle {
    public int width;
    public int height;
    public int speed;
    public int posx;
    public int posy;
    public boolean rotation = false;
    public boolean vanish = false;

    public final int defaultSpeed = 5;

    public Obstacle(int w, int h, int x, int y){
        width = w;
        height = h;
        posx=x;
        posy=y;
        speed = defaultSpeed;
    }

    public static ArrayList<Obstacle> iLikeToMoveItMoveIt(ArrayList<Obstacle> obstacles){
        for(Obstacle o : obstacles){
            o.posy+= o.speed;
        }
    return obstacles;
    }
}
