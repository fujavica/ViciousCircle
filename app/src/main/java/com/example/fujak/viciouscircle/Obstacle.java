package com.example.fujak.viciouscircle;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Fujak on 11/6/2017.
 */

public class Obstacle implements Serializable{
    public int width;
    public int height;
    public int speed;
    public int posx;
    public int  posy;
    public boolean rotation = false;
    public boolean vanish = false;

    public static int defaultSpeed = 11;
    public static int screenHeight;
    public static int screenWidth;

 //   public static final double speedCoeficient = 0.005 ;  //           =   0.005% of screen height per frame

    public Obstacle(int w, int h, int x, int y) {

        posx=x;
        posy=y;
        width=w;
        height=h;
        speed = defaultSpeed;
    }

    public  static ArrayList<Obstacle> iLikeToMoveItMoveIt(ArrayList<Obstacle> obstacles){



        for(Obstacle o : obstacles){
            o.posy+= o.speed ;
        }
    return obstacles;
    }

    public static ArrayList<Obstacle> adjustSize(ArrayList<Obstacle> obstacles, int w, int h)
    {
    return obstacles;
    }
}
