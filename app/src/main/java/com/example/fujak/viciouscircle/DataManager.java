package com.example.fujak.viciouscircle;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Fujak on 11/6/2017.
 */

public class DataManager {
    private static Gson gson;

    public static ArrayList<Obstacle> loadLevel(Context c, int level, int w, int h){
        ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();



        int resID = c.getResources().getIdentifier("level" + level,"raw",c.getPackageName());

        String json = null;
        try {
            InputStream is = c.getResources().openRawResource(resID);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();




            int obstacle_width = (int) ((w *0.40) + 0.5);
            Obstacle o = new Obstacle(obstacle_width,100, 20, -500);
            obstacles.add(o);
            return obstacles;
        }

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
        Obstacle[] obstacles_arr = gson.fromJson(json, Obstacle[].class);
        obstacles = new ArrayList<>(Arrays.asList(obstacles_arr));
        //String test = gson.toJson(obstacles);
        return obstacles;
    }
}
