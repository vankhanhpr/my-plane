package com.example.khanh.myplane.object;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.khanh.myplane.mygame.Animation;
import com.example.khanh.myplane.mygame.GameObject;

import java.util.Random;

/**
 * Created by Khanh on 5/30/2017.
 */

public class BomBoss extends GameObject {
    private int row;
    private Animation animation = new Animation();
    private Bitmap spritesheet;
    boolean flag=true;
    public  int vector;
    Random random;

    public BomBoss(Bitmap res, int x, int y, int w, int h, int numFrames,int vector)
    {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.vector=vector;
        random= new Random();

        Bitmap[] image = new Bitmap[numFrames];

        spritesheet = res;

        for(int i = 0; i<image.length; i++)
        {
            image[i] = Bitmap.createBitmap(spritesheet, i*width, 0, width, height);
        }
        animation.setFrames(image);
        animation.setDelay(20);
    }
    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(animation.getImage(),x,y,null);
    }

    public void update(){
        switch (vector){
            case 0:
                x-=2;
                y+=7;
                break;
            case 1:
                y+=7;
                break;
            case 2:
                x+=2;
                y+=7;
                break;
            case 3:
                x-=3;
                y+=7;
                break;
            case 4:
                x+=3;
                y+=7;
                break;
            case 5:
                x-=1;
                y+=7;
                break;
            case 6:
                x+=1;
                y+=7;
                break;

            case 7:
                x+=9;
                y+=7;
                break;
            case 8:
                x-=9;
                y+=7;
                break;
            case 9:
                x+=11;
                y+=3;
                break;
            case 10:
                x-=11;
                y+=3;
                break;
            case 11:
                x+=13;
                y-=3;
                break;
            case 12:
                x-=13;
                y+=3;
                break;

            default:
                break;
        }
        animation.update();
    }
    public int getHeight(){return height;}
}
