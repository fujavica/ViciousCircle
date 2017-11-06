package com.example.fujak.viciouscircle;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;

/**
 * Created by Fujak on 11/5/2017.
 */

public class GameView extends View {

    int width;
    int height;
    int tickRate = 10;
    Paint myPaint;
    ArrayList<Obstacle> obstacles;


    public GameView(Context context) {
        super(context);
        init(context);
        // this.setOnTouchListener(this);
    }


    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    void init(Context context) {
        obstacles = DataManager.loadLevel();

        myPaint = new Paint();
        myPaint.setColor(Color.rgb(255, 255, 255));
        //myPaint.setStrokeWidth(10);

        Timer timer = new Timer();

        timer.schedule(new Controller(this), 2000, tickRate);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w;
        height = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (Obstacle o : obstacles) {

            myPaint.setColor(Color.rgb(100, 200, 100));
            canvas.drawRect(o.posx, o.posy, o.posx + o.width, o.posy+ o.height,myPaint);

        }
    }



    public boolean onTouchEvent(MotionEvent event) {

    return true;

    }

    public void moveObstacles(){
        obstacles = Obstacle.iLikeToMoveItMoveIt(obstacles);
    }

}

