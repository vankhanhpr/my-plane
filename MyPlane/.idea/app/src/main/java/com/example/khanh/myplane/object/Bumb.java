package com.example.khanh.myplane.object;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.khanh.myplane.mygame.Animation;
import com.example.khanh.myplane.mygame.GameObject;

/**
 * Created by Khanh on 5/29/2017.
 */

public class Bumb extends GameObject {

   /* private int x;
    private int y;
    private int width;
    private int height;*/
    private int row;
    private Bitmap spritesheet;
    private Animation animation=new Animation();
    public Bumb(Bitmap res, int x, int y, int w, int h,int numFrames){
        this.x=x;
        this.y=y;
        this.width=w;
        this.height=h;
        spritesheet=res;

        Bitmap[] bitmaps=new Bitmap[numFrames];
        for (int i=0;i<bitmaps.length;i++){
            if(i%4==0&&i>0)row++;
            bitmaps[i]=Bitmap.createBitmap(spritesheet,(i-4*row)*width,row*height,width,height);
        }
        animation.setFrames(bitmaps);
        animation.setDelay(15);
    }
    public void update(){
        if(!animation.playedOnce()){
            animation.update();
        }
    }
    public void draw(Canvas canvas){
        if(!animation.playedOnce()){
            canvas.drawBitmap(animation.getImage(),x,y,null);
        }

    }
    public int getHeight(){
        return height;
    }
    public boolean isPlayedOnce(){
        return animation.playedOnce();
    }
}
