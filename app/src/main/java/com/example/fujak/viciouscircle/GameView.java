package com.example.fujak.viciouscircle;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
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
import java.util.TimerTask;

/**
 * Created by Fujak on 11/5/2017.
 */

public class GameView extends View {

    private int width;
    private int height;
    Context context;
    Paint obstaclePaint;
    Paint bigCirclePaint;
    Paint smallCirclePaint;
    Timer timer;
    ArrayList<Obstacle> obstacles;
    Circle circle;
    int bigRadius;
    int smallRadius = 35;
    int circleCenter_x;
    float circleCenter_y;
    double alpha = 0;
    double beta = Math.PI;
    float circle_x;
    float circle_y;

    ControlThread t;
    Object syncToken;
    float circle2_x;
    float circle2_y;
    double angularVelocity = 0.08;
    double upper_difference;
    double left_difference;

    int lastTouch = -1;
    int backupTouch = -1;
    Rotation rotation = Rotation.STATIONARY;

    MediaPlayer mediaPlayer;
    MediaPlayer mediaPlayer2;
    boolean paused = false;
    int level = 1;
    int songID;

    public GameView(Context context) {
        super(context);
        level = ((MainActivity) context).chosenLevel;
        songID = context.getResources().getIdentifier("level"  + level + "music","raw",context.getPackageName());
        mediaPlayer = MediaPlayer.create(context, songID);
        init(context);
    }


    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        level = ((MainActivity) context).chosenLevel;
        songID = context.getResources().getIdentifier("level"  + level + "music","raw",context.getPackageName());
        mediaPlayer = MediaPlayer.create(context, songID);
        init(context);
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    void init(Context context) {
        this.context = context;
        circle = new Circle(200);
        obstaclePaint = new Paint();
        obstaclePaint.setColor(Color.rgb(255, 255, 255));
        //obstaclePaint.setColor(Color.rgb(100, 200, 100));

        bigCirclePaint = new Paint();
        bigCirclePaint.setColor(Color.rgb(50, 50, 50));
        bigCirclePaint.setStyle(Paint.Style.STROKE);
        bigCirclePaint.setStrokeWidth(8);

        smallCirclePaint = new Paint();
        smallCirclePaint.setColor(Color.rgb(250, 50, 250));

        mediaPlayer.start();

        syncToken = new Object();
        t = new ControlThread(this, syncToken);
        t.start();
       // timer = new Timer();
       // timer.scheduleAtFixedRate(new Controller(this), 1000, tickRate);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.d("Size change","Size changed: from " + oldw + " " + oldh + " to " + w + " " + h);
        if(obstacles == null)
        obstacles = DataManager.loadLevel(this.getContext(), 1,w, h);
        else obstacles = Obstacle.adjustSize(obstacles, w, h);

        width = w;
        height = h;

        circleCenter_x = width / 2;
        circleCenter_y = (float) (height - (height / 6));
        bigRadius = width / 4 ;
        circle_x = circleCenter_x - bigRadius;
        circle2_x = circleCenter_x + bigRadius;
        circle_y = circleCenter_y;
        circle2_y = circleCenter_y;

        this.invalidate();
        super.onSizeChanged(w, h, oldw, oldh);

    }

    @Override
    protected void onDraw(Canvas canvas) {
      //  long time= System.currentTimeMillis();
      //  Log.d("time",Long.toString(time));
        canvas.drawCircle(circleCenter_x, circleCenter_y , bigRadius ,bigCirclePaint);
        canvas.drawCircle(circle_x  , circle_y, smallRadius,smallCirclePaint);
        canvas.drawCircle(circle2_x  , circle2_y, smallRadius,smallCirclePaint);

        if(obstacles == null) return;
        for (Obstacle o : obstacles) {
            canvas.drawRect(o.posx, o.posy, o.posx + o.width, o.posy+ o.height,obstaclePaint);
        }
        synchronized(syncToken)
        {
            syncToken.notify();
        }

       // long time2= System.currentTimeMillis();
      //  Log.d("timeFin",Long.toString(time2));
    }



    public boolean onTouchEvent(MotionEvent event)
    {

        if(event.getActionMasked() == MotionEvent.ACTION_UP)
            {
                if (backupTouch == event.getPointerId(event.getActionIndex()))
                {
                    backupTouch = -1;
                    return true;
                }
                if (lastTouch == event.getPointerId(event.getActionIndex()) && backupTouch == -1)
                {
                    lastTouch = -1;
                    rotation = Rotation.STATIONARY;
                    return true;
                }
                if (backupTouch != -1){
                    double x = event.getX(event.findPointerIndex(backupTouch));
                    if (x > width / 2)
                        rotation = Rotation.CLOCKWISE;
                    else
                        rotation = Rotation.COUNTERCLOCKWISE;

                    lastTouch = backupTouch;
                    backupTouch = -1;
                    return true;
                }
                return true;
            }
        else if(event.getActionMasked() == MotionEvent.ACTION_POINTER_UP)
        {
            if (backupTouch == event.getPointerId(event.getActionIndex()))
            {
                backupTouch = -1;
                return true;
            }
            if (lastTouch == event.getPointerId(event.getActionIndex()) && backupTouch == -1)
            {
                lastTouch = -1;
                rotation = Rotation.STATIONARY;
                return true;
            }
            if (backupTouch != -1){
                double x = event.getX(event.findPointerIndex(backupTouch));
                if (x > width / 2)
                    rotation = Rotation.CLOCKWISE;
                else
                    rotation = Rotation.COUNTERCLOCKWISE;

                lastTouch = backupTouch;
                backupTouch = -1;
                return true;
            }
        return true;
        }
        else if(event.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN)
        {
            if(lastTouch !=-1) backupTouch = lastTouch;

            lastTouch = event.getPointerId(event.getActionIndex());
            double x = event.getX(event.findPointerIndex(lastTouch));
            if (x > width / 2)
                rotation = Rotation.CLOCKWISE;
            else
                rotation = Rotation.COUNTERCLOCKWISE;
            return true;
        }

        else if(event.getActionMasked() == MotionEvent.ACTION_DOWN)
        {
            if(lastTouch !=-1) backupTouch = lastTouch;

            lastTouch = event.getPointerId(event.getActionIndex());
            double x = event.getX(event.findPointerIndex(lastTouch));
            if (x > width / 2)
                rotation = Rotation.CLOCKWISE;
            else
                rotation = Rotation.COUNTERCLOCKWISE;
            return true;
        }

        return true;
    }


     public void moveObstacles(){
        if(obstacles != null)
            obstacles = Obstacle.iLikeToMoveItMoveIt(obstacles);
    }

    public void moveCircles()
    {
        if(rotation == Rotation.STATIONARY) {
            return;
        }

        if(rotation == Rotation.CLOCKWISE)
        {
            alpha += angularVelocity;
            beta += angularVelocity;
        }
        else
        {
            alpha -= angularVelocity;
            beta -= angularVelocity;
        }
        circle_x = (float) (bigRadius*Math.cos(alpha) + circleCenter_x);
        circle_y = (float) (bigRadius*Math.sin(alpha) + circleCenter_y);
        circle2_x = (float) (bigRadius*Math.cos(beta) + circleCenter_x);
        circle2_y = (float) (bigRadius*Math.sin(beta) + circleCenter_y);
    }

    public boolean detectCollisions()
    {
    if(obstacles == null) return false;
        for(Obstacle o : obstacles)
        {
            upper_difference = circle_y - smallRadius  - o.posy;
            if(upper_difference + 10<  o.height && upper_difference > - (smallRadius*2)) // Y match?
            {
             left_difference = circle_x - smallRadius - o.posx;
                if(left_difference <  o.width && left_difference > - (smallRadius*2)) // X match too?
                {
                    gameOver(); //Collision with circle 1
                    return true;
                }

            }
            upper_difference = circle2_y - smallRadius  - o.posy;
            if(upper_difference + 10<  o.height && upper_difference > - (smallRadius*2)) // Y match?
            {
                left_difference = circle2_x - smallRadius - o.posx;
                if(left_difference <  o.width && left_difference > - (smallRadius*2)) // X match too?
                {
                    gameOver(); //Collision with circle 2
                    return true;
                }

            }

        }
        return false;
    }

    public void levelCompleted()
    {
        if(obstacles == null) return;

        for(Obstacle o : obstacles)
        {
            if(o.posy < height) return;
        }
        end();
        ((MainActivity)context).levelCompleted();
    }
    public void gameOver()
    {
        mediaPlayer.release();
        mediaPlayer = MediaPlayer.create(context, R.raw.gameover2);
        mediaPlayer.setLooping(false);
        mediaPlayer.start();

        Thread pause = new breakThread(this);
        pause.start();
    }

    public void restartLevel(){
        if(mediaPlayer == null) return;
        mediaPlayer.release();
        mediaPlayer = MediaPlayer.create(context, songID);
        mediaPlayer.start();
        obstacles = DataManager.loadLevel(this.getContext(), 1,width, height);
        t = new ControlThread(this, syncToken);
        t.start();
    }
    public void end()
    {
        if(mediaPlayer != null) mediaPlayer.release();
        mediaPlayer = null;
        t.interrupt();
    }


    @Override
    public void onDetachedFromWindow() {

       if(mediaPlayer != null) mediaPlayer.release();
        mediaPlayer = null;
        t.interrupt();
        super.onDetachedFromWindow();
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility){
        if (visibility == View.VISIBLE){
            if(paused){
                mediaPlayer.start();
                this.setWillNotDraw(false);
            }
        }
        else
        {
            this.setWillNotDraw(true);
            mediaPlayer.pause();
            paused = true;
        }
    }


}

