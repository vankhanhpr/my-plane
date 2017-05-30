package com.example.khanh.myplane.mygame;

import android.graphics.Rect;

/**
 * Created by Khanh on 5/27/2017.
 */

public abstract  class GameObject {
    protected  int x;
    protected  int y;
    protected int dx;
    protected  int dy;
    protected  int height;
    protected  int width;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDx() {
        return dx;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public int getDy() {
        return dy;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
    public Rect getRectangle()
    {
        return  new Rect(x,y,x+width,y+height);
    }
}
