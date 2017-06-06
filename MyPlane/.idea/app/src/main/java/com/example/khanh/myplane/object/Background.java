package com.example.khanh.myplane.object;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.khanh.myplane.mygame.GamePanel;

/**
 * Created by Khanh on 4/27/2017.
 */

public class Background {
    private Bitmap image;
    private int x, y, dx,dy;


    public Background(Bitmap res)
    {
        image = res;
        dy= GamePanel.MOVESPEED;
    }
    public void update()
    {
        y-=dy;
        if(y>0){
            y=-GamePanel.HEIGHT;
        }
    }
    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(image, x, y,null);
        if(y>-GamePanel.HEIGHT)
        {
            canvas.drawBitmap(image, x, y+GamePanel.HEIGHT, null);
        }
    }
    public void setVector(int dy)
    {
        this.dy = dy;

    }
}
