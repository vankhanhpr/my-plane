package com.example.khanh.myplane.mygame;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.khanh.myplane.R;
import com.example.khanh.myplane.object.Background;
import com.example.khanh.myplane.object.Bom;
import com.example.khanh.myplane.object.Bullet;
import com.example.khanh.myplane.object.Bumb;
import com.example.khanh.myplane.object.Plane_Enemi;
import com.example.khanh.myplane.object.Player;
import com.example.khanh.myplane.object.Rain_Bom;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;

/**
 * Created by Khanh on 5/27/2017.
 */

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread;
    private Background bg;
    public  static  final int WIDTH =768;
    public  static  final int HEIGHT =1280;
    public  static  final  int MOVESPEED=-5;
    public Player player;
    ArrayList<Bullet> arrbullet;
    ArrayList<Plane_Enemi>arrplane;
    ArrayList<Bom> arrbom;
    ArrayList<Bumb>arrbumb;
    ArrayList<Rain_Bom>arrRainBom;
    public  int point=0,pointmax=0;
    private  long timeBullet,timePlane,timeBom,timeBumb;
    private  Random random= new Random();

    public GamePanel(Context context)
    {
        super(context);
        //add the callback to the surfaceholder to intercept events
        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);

        //make gamePanel focusable so it can handle events
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = true;
        while(retry)
        {
            try{thread.setRunning(false);
                thread.join();

            }catch(InterruptedException e){e.printStackTrace();}
            retry = false;
        }
    }
    //Hàm khai báo các đối tượng
    @Override
    public void surfaceCreated(SurfaceHolder holder){

        bg= new Background(BitmapFactory.decodeResource(getResources(), R.drawable.bg));
        player= new Player(BitmapFactory.decodeResource(getResources(),R.drawable.myplane),96,109,3);
        arrbullet= new ArrayList<>();
        arrplane= new ArrayList<>();
        arrbom= new ArrayList<>();
        arrbumb= new ArrayList<>();
        arrRainBom = new ArrayList<>();



        timeBullet= System.nanoTime();
        timePlane= System.nanoTime();
        timeBom= System.nanoTime();
        timeBumb= System.nanoTime();
        player.setPlaying(true);

        bg.setVector(-1);

        //we can safely start
        // the game loop
        thread.setRunning(true);
        thread.start();

    }
    //các sự kiện kéo chuột và nhả đạn bằng cách bấm chuột
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {

        if(event.getAction()==MotionEvent.ACTION_DOWN)
        {
            player.x= (int)event.getX()-100;
            player.y=(int)event.getY();
            arrbullet.add(new Bullet(BitmapFactory.decodeResource(getResources(),R.drawable.rocket2),player.getX()+30/*+83*/,player.getY()-30/*-82*/,36,81,2));
            player.setPlaying(true);

            return true;
        }

        if(event.getAction()==MotionEvent.ACTION_MOVE){
            player.x=(int) event.getX()-100;
            player.y=(int) event.getY();
            //stateDan=true;
            return true;
        }


        return super.onTouchEvent(event);
    }
    public void update()
    {
        //bg.update();
        player.update();

        if(player.getPlaying()) {
            for (int i = 0; i < arrbullet.size(); i++) {
                arrbullet.get(i).update();

                if (arrbullet.get(i).getY() < -320) {
                    arrbullet.remove(i);
                }
            }

            run();
        }
        else
        {
            arrbom.clear();
            arrbullet.clear();
            arrplane.clear();
            player.resetScore();
            arrbumb.clear();
            point=0;

            //run();
            //player.setPlaying(true);
        }

    }


    public  void run()
    {

        //may bay dich bay va nha bom
        long plane = (System.nanoTime() - timePlane) / 1000000;
        if (plane > 500) {
            if (arrplane.size() < 6 && point<5 ) {
                arrplane.add(new Plane_Enemi(BitmapFactory.decodeResource(getResources(), R.drawable.plane_attact4), 0, random.nextInt(520) + 60, 95, 53, player.getScore(), 1));
            }
            timePlane = System.nanoTime();
        }
        for (int i = 0; i < arrplane.size(); i++) {
            arrplane.get(i).update();
            //máy bay địch va chạm với máy bay ta
            if (collision(arrplane.get(i), player)) {
                arrplane.remove(i);
                player.setPlaying(false);
                break;
            }
            //Xủ lý va cham giữa đạn và máy bay địch
            for (int j = 0; j < arrbullet.size(); j++) {

                //va chạm giữa đạn và máy bay địch
                if (collision(arrbullet.get(j), arrplane.get(i))) {
                    point = point + 1;
                    if(pointmax<point) {
                        pointmax = point;
                    }
                    arrbumb.add(new Bumb(BitmapFactory.decodeResource(getResources(), R.drawable.bump), arrbullet.get(j).getX(), arrbullet.get(j).getY(), 64, 64, 16));
                    arrbullet.remove(j);
                    arrplane.remove(i);
                }
            }
            if (arrplane.get(i).getX() > 800) {
                arrplane.remove(i);
            }
        }

        //ramdom dan
        long bom = (System.nanoTime() - timeBom) / 1000000;
        if (bom > 100)
        {
            if (arrbom.size() < 5 && point<5)
            {
                int temp = random.nextInt(5) + 1;
                arrbom.add(new Bom(BitmapFactory.decodeResource(getResources(), R.drawable.bomt1), arrplane.get(temp).getX() + 59, arrplane.get(temp).getY(), 26, 81, 1));
            }
            else
            if (point>=5 && arrRainBom.size()<7 && arrbom.size()==0 && arrplane.size()==0)
            {
                //vẽ mưa bom
                int temp = random.nextInt(800) + 1;
                int temp2= random.nextInt(560);
                arrRainBom.add(new Rain_Bom(BitmapFactory.decodeResource(getResources(), R.drawable.bom3),temp, temp2, 80, 110, 3));

            }
            timeBom = System.nanoTime();
        }
        //Vẽ đạn máy bay địch
        for (int i = 0; i < arrbom.size(); i++) {
            arrbom.get(i).update();
            if (collision(arrbom.get(i), player)) {
                arrbumb.add(new Bumb(BitmapFactory.decodeResource(getResources(), R.drawable.bump), player.getX(), player.getY(), 64, 64, 16));
                arrbom.remove(i);
                player.setPlaying(false);
            }
            if (arrbom.get(i).getY() > 1300) {
                arrbom.remove(i);
            }
        }
        //Xử lý mưa bom chạy trên xuống
        for(int i=0;i<arrRainBom.size();i++)
        {
            arrRainBom.get(i).update();
            if(arrRainBom.get(i).getY()>1300)
            {
                arrRainBom.remove(i);
            }
            //xử lý va chạm với bom
            for(int j=0;j<arrbullet.size();j++)
            {
                if(collision(arrbullet.get(j),arrRainBom.get(i)))
                {
                    point++;
                    if(point>pointmax)
                    {
                        pointmax=point;
                    }
                    arrbumb.add(new Bumb(BitmapFactory.decodeResource(getResources(), R.drawable.bump),arrRainBom.get(i).getX(),arrRainBom.get(i).getY(), 64, 64, 16));
                    arrbullet.remove(j);
                    arrRainBom.remove(i);
                }
            }
            //va chạm giữa mưa bom  và người chơi
            if(collision(player,arrRainBom.get(i)))
            {
                arrRainBom.clear();
                arrbumb.add(new Bumb(BitmapFactory.decodeResource(getResources(), R.drawable.bump),player.getX(),player.getY(), 64, 64, 16));
                player.setPlaying(false);
            }
        }
        //Xử lý đạn máy bay địch rớt trên xuống
        for (int i = 0; i < arrbumb.size(); i++)
        {
            arrbumb.get(i).update();

        }
    }


    public  boolean collision(GameObject a,GameObject b)
    {
        if(Rect.intersects(a.getRectangle(),b.getRectangle()))
        {
            return true;
        }
        return false;
    }
    public void draw(Canvas canvas)
    {
        final float scaleFactorX = getWidth()/(float)WIDTH;
        final float scaleFactorY = getHeight()/(float)HEIGHT;
        if(canvas!=null) {
            final int savedState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorY);
            bg.draw(canvas);
            for (Plane_Enemi plane_enemi:arrplane) {
                plane_enemi.draw(canvas);

            }
            player.draw(canvas);
            drawText(canvas);
            for(Bumb bumb:arrbumb)
            {
                bumb.draw(canvas);
                if(bumb.isPlayedOnce())
                {
                    arrbumb.remove(bumb);
                }
            }
            for(Bom  bom:arrbom)
            {
                bom.draw(canvas);
            }
            for (Bullet bullet:arrbullet)
             {
                 bullet.draw(canvas);

             }
             for(Rain_Bom rainbom: arrRainBom)
             {
                 rainbom.draw(canvas);
             }
            canvas.restoreToCount(savedState);

        }
    }
    //hien diem
    public void drawText(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(25);


        Paint paint1 = new Paint();
        paint1.setColor(Color.WHITE);
        paint1.setTextSize(50);

        canvas.drawText("Point: " + point, 5, 40, paint);
        canvas.drawText("Point max: " + pointmax, 5, 80, paint);
        if(!player.getPlaying())
        {
            canvas.drawText("Please to start game",WIDTH/2-200,HEIGHT/2-50,paint1);
            canvas.drawText("Point for you:"+pointmax,WIDTH/2-200,HEIGHT/2+10,paint1);

        }
    }
}
