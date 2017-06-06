package com.example.khanh.myplane.object;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.khanh.myplane.mygame.Animation;
import com.example.khanh.myplane.mygame.GameObject;
import com.example.khanh.myplane.mygame.GamePanel;

import java.util.Random;

/**
 * Created by Khanh on 5/30/2017.
 */

public class Boss extends GameObject {
    private int row;
    private Animation animation = new Animation();
    private Bitmap spritesheet;
    boolean flag=true;
    Random random;
    int temp;

    public Boss(Bitmap res, int x, int y, int w, int h, int numFrames)
    {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        random= new Random();
        Bitmap[] image = new Bitmap[numFrames];
        x= GamePanel.WIDTH/2+100;
        y=400;
        spritesheet = res;

        for(int i = 0; i<image.length; i++)
        {
            image[i] = Bitmap.createBitmap(spritesheet, i*width, 0, width, height);
        }
        animation.setFrames(image);
        animation.setDelay(100);
    }
    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(animation.getImage(),x,y,null);
    }

    public void update()
    {

        temp=random.nextInt(8);
        switch (temp){
            case 0:
                if(x>50)
                    x-=50;
                break;
            case 1:
                if(x<570)
                    x+=50;
                break;
            case 2:
                if(y>150)
                    y-=50;
                break;
            case 3:
                if(y<440)
                    y+=50;
                break;
            case 4:
                if(x>50&&y>150){
                    x-=50;
                    y-=50;
                }
                break;
            case 5:
                if(x<570&&y>150){
                    x+=50;
                    y-=50;
                }
                break;
            case 6:
                if(x>50&&y<440){
                    x-=50;
                    y+=50;
                }
                break;
            case 7:
                if(x<570&&y<440){
                    x+=50;
                    y+=50;
                }
                break;
            default:
                break;
        }

        animation.update();
    }
    public int getHeight(){return height;}

    @Override
    public Rect getRectangle() {
        return  new Rect(x+115,y,x+width-115,y+height-127);
    }
}
