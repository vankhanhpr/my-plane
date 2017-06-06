package com.example.khanh.myplane.object;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.khanh.myplane.mygame.Animation;
import com.example.khanh.myplane.mygame.GameObject;

import java.util.Random;

/**
 * Created by Khanh on 5/27/2017.
 */

public class Plane_Enemi extends GameObject {

   /* private int x;
    private int y;*/
  /*  private int width;
    private int height;*/
    private int row;
    private Animation animation = new Animation();
    private Bitmap spritesheet;
    private Random random;


    public Plane_Enemi(Bitmap res, int x, int y, int w, int h,int s, int numFrames)
    {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;

        Bitmap[] image = new Bitmap[numFrames];

        spritesheet = res;

        for(int i = 0; i<image.length; i++)
        {
            image[i] = Bitmap.createBitmap(spritesheet, i*width, 0, width, height);
        }
        animation.setFrames(image);
        animation.setDelay(10);
    }
    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(animation.getImage(),x,y,null);
    }
    /*public int getX()
    {
        return x;
    }*/
    public void update()
    {
        x+=5;
        animation.update();
    }
    /*public int getY()
    {
        return y;
    }*/
    public int getHeight()
    {
        return height-10;
    }
}
