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
import com.example.khanh.myplane.object.BomBoss;
import com.example.khanh.myplane.object.Boss;
import com.example.khanh.myplane.object.Bullet;
import com.example.khanh.myplane.object.Bumb;
import com.example.khanh.myplane.object.Heard;
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
    public  static  final int WIDTH =1080;
    public  static  final int HEIGHT =1920;
    public  static  final  int MOVESPEED=-5;
    public Player player;
    ArrayList<Bullet> arrbullet;
    ArrayList<Plane_Enemi>arrplane;
    ArrayList<Bom> arrbom;
    ArrayList<Bumb>arrbumb;
    ArrayList<Rain_Bom>arrRainBom;
    ArrayList<Heard>arrHeard;
    Boss boss;
    ArrayList<BomBoss>arrBomBoss;

    public  int point=0,pointmax=0;
    private  long timeBullet,timePlane,timeBom,timeBumb,timeBoss;
    private  Random random= new Random();
    public  int turn =2,hpboss=100;
    public  boolean win= false;

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

        bg= new Background(BitmapFactory.decodeResource(getResources(), R.drawable.bg7));
        player= new Player(BitmapFactory.decodeResource(getResources(),R.drawable.myplane),96,109,3);


        arrbullet= new ArrayList<>();
        arrplane= new ArrayList<>();
        arrbom= new ArrayList<>();
        arrbumb= new ArrayList<>();
        arrRainBom = new ArrayList<>();
        arrHeard= new ArrayList<>();
        arrBomBoss= new ArrayList<>();



        timeBullet= System.nanoTime();
        timePlane= System.nanoTime();
        timeBom= System.nanoTime();
        timeBumb= System.nanoTime();
        timeBoss= System.nanoTime();
        timeBoss=System.nanoTime();
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
            player.setPlaying(true);
            player.x= (int)event.getX()-100;
            player.y=(int)event.getY();
            arrbullet.add(new Bullet(BitmapFactory.decodeResource(getResources(), R.drawable.rocket2), player.getX() + 30, player.getY() - 30/*-82*/, 36, 81, 2));
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
        bg.update();
        player.update();

        if(player.getPlaying())
        {
            for (int i = 0; i < arrbullet.size(); i++) {
                arrbullet.get(i).update();
                if (arrbullet.get(i).getY() < -320)
                {
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
            boss=null;
            point=0;
            hpboss=100;
            turn=2;
        }
    }


    public  void run()
    {

        Plane();
        //ramdom dan
        Bom();

        Boss();


        HP();


        for(int i=0;i<arrBomBoss.size();i++)
        {
            arrBomBoss.get(i).update();
            if(arrBomBoss.get(i).getY()>1920)
            {
                arrBomBoss.remove(i);
            }
        }
        for (int i = 0; i < arrbom.size(); i++) {
            arrbom.get(i).update();

            //Va chạm máy bay dịch với máy bay ta
            if (collision(arrbom.get(i), player))
            {
                turn--;
                arrbumb.add(new Bumb(BitmapFactory.decodeResource(getResources(), R.drawable.bump), player.getX(), player.getY(), 64, 64, 16));
                arrbom.remove(i);
                if(turn==0)
                {
                    player.setPlaying(false);
                }
            }
            if (arrbom.get(i).getY() > 1920)
            {
                arrbom.remove(i);
            }
        }
        //Xử lý mưa bom chạy trên xuống
        for(int i=0;i<arrRainBom.size();i++)
        {
            arrRainBom.get(i).update();
            if(arrRainBom.get(i).getY()>1920)
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
                turn--;
                arrRainBom.clear();
                arrbumb.add(new Bumb(BitmapFactory.decodeResource(getResources(), R.drawable.bump),player.getX(),player.getY(), 64, 64, 16));
                if(turn==0) {
                    player.setPlaying(false);
                }
            }
        }
        //Xử lý đạn máy bay địch rớt trên xuống
        for (int i = 0; i < arrbumb.size(); i++)
        {
            arrbumb.get(i).update();

        }

        for(int i=0;i<arrBomBoss.size();i++)
        {
            arrBomBoss.get(i).update();
            if(arrBomBoss.get(i).getY()>1920|| arrBomBoss.get(i).getX()<0|| arrBomBoss.get(i).getX()>1080)
            {
                arrBomBoss.remove(i);
            }
            //va chạm người chơi với boss
            if(collision(player,boss))
            {
                turn--;
                arrbumb.add(new Bumb(BitmapFactory.decodeResource(getResources(), R.drawable.bump),player.getX(),player.getY(), 64, 64, 16));
                if(turn==0)
                {
                    arrBomBoss.clear();
                    player.setPlaying(false);
                }
            }
            //va chạm bom boss với người chơi
            if(collision(arrBomBoss.get(i),player))
            {
                arrBomBoss.remove(i);
                turn--;
                arrbumb.add(new Bumb(BitmapFactory.decodeResource(getResources(), R.drawable.bump),player.getX(),player.getY(), 64, 64, 16));
                if(turn==0)
                {
                    arrBomBoss.clear();
                    player.setPlaying(false);
                }
            }
        }
        if(boss!=null)
        {
            for (int k = 0; k < arrbullet.size(); k++) {
                //va chạm giữa boss với đạn
                if (collision(boss, arrbullet.get(k))) {
                    arrbullet.remove(k);
                    point++;
                    if(point>pointmax)
                    {
                        pointmax=point;
                    }
                    arrbumb.add(new Bumb(BitmapFactory.decodeResource(getResources(), R.drawable.bump), boss.getX() + 347, boss.getY() + 269, 64, 64, 16));
                    hpboss--;

                }
            }
        }
        if (hpboss==0)
        {
            hpboss=100;
            player.setPlaying(false);

        }
    }

    public  void  Plane()
    {
        //may bay dich bay va nha bom
        long plane = (System.nanoTime() - timePlane) / 1000000;
        if (plane > 500) {
            if (arrplane.size() < 6 && point<10 ) {
                arrplane.add(new Plane_Enemi(BitmapFactory.decodeResource(getResources(), R.drawable.plane_attact4), random.nextInt(100), random.nextInt(760) + 60, 95, 53, player.getScore(), 1));
            }
            /*else
                if(arrplane.size()<13&& point>=5&&point<=20) {
                    arrplane.add(new Plane_Enemi(BitmapFactory.decodeResource(getResources(), R.drawable.plane_attact4), random.nextInt(100), random.nextInt(760) + 60, 95, 53, player.getScore(), 1));
                }*/
            timePlane = System.nanoTime();
        }
        for (int i = 0; i < arrplane.size(); i++)
        {
            arrplane.get(i).update();
            //máy bay địch va chạm với máy bay ta
            if (collision(arrplane.get(i), player)) {
                turn--;
                arrplane.remove(i);
                arrbumb.add(new Bumb(BitmapFactory.decodeResource(getResources(), R.drawable.bump),player.getX(), player.getY(), 64, 64, 16));
                //kiểm tra còn mạng hay không
                if(turn==0)
                {
                    player.setPlaying(false);
                }
                break;
            }
            //Xủ lý va cham giữa đạn và máy bay địch
            for (int j = 0; j < arrbullet.size(); j++) {
                //va chạm giữa đạn và máy bay địch
                if (collision(arrbullet.get(j), arrplane.get(i))) {
                    point = point + 1;
                    if(pointmax<point)
                    {
                        pointmax = point;
                    }
                    arrbumb.add(new Bumb(BitmapFactory.decodeResource(getResources(), R.drawable.bump), arrbullet.get(j).getX(), arrbullet.get(j).getY(), 64, 64, 16));
                    arrbullet.remove(j);
                    arrplane.remove(i);
                }
            }
            if (arrplane.get(i).getX() > 1080) {
                arrplane.remove(i);
            }
        }
    }
    public  void Bom()
    {
        long bom = (System.nanoTime() - timeBom) / 1000000;
        if (bom > 1000)
        {
            if (arrbom.size() < 7 && point<10)
            {
                int temp = random.nextInt(5) + 1;
                arrbom.add(new Bom(BitmapFactory.decodeResource(getResources(), R.drawable.bomt), arrplane.get(temp).getX() + 59, arrplane.get(temp).getY(), 26, 81, 5));
            }
            else
            {
                /*if(arrbom.size()<15 && point>=10&&point<=20)
                {
                    int temp = random.nextInt(5) + 1;
                    arrbom.add(new Bom(BitmapFactory.decodeResource(getResources(), R.drawable.bomt1), arrplane.get(temp).getX() + 59, arrplane.get(temp).getY(), 26, 81, 1));
                }*/
                //vẽ bom rơi
                if (point >=10 && arrRainBom.size() < 7 && arrbom.size() == 0 && arrplane.size() == 0 && point <= 40)
                {
                    //vẽ mưa bom
                    int temp = random.nextInt(1080);
                    int temp2 = random.nextInt(960);
                    arrRainBom.add(new Rain_Bom(BitmapFactory.decodeResource(getResources(), R.drawable.bom3), temp, temp2, 80, 110, 3));

                }
                else
                {
                    if (point > 40 && arrRainBom.size() < 50 && arrbom.size() == 0 && arrplane.size() == 0 && point <= 60)
                    {
                        int temp = random.nextInt(1080);
                        int temp2 = random.nextInt(960);
                        arrRainBom.add(new Rain_Bom(BitmapFactory.decodeResource(getResources(), R.drawable.bom3), temp, temp2, 80, 110, 3));
                        arrRainBom.add(new Rain_Bom(BitmapFactory.decodeResource(getResources(), R.drawable.bom3), temp+300, temp2-100, 80, 110, 3));
                        arrRainBom.add(new Rain_Bom(BitmapFactory.decodeResource(getResources(), R.drawable.bom3), temp-400, temp2-40, 80, 110, 3));
                    }
                }
            }
            timeBom = System.nanoTime();
        }
    }
    public  void Boss() {
        if(point>60 && arrRainBom.size()==0) {
            long bosstime = (System.nanoTime() - timeBoss) / 1000000;
            if (bosstime > 2000 && point > 60) {

                if (boss == null)
                {
                    boss = (new Boss(BitmapFactory.decodeResource(getResources(), R.drawable.myboss), WIDTH / 2 - 347, 200, 695, 539, 11));
                } else {
                    if (arrBomBoss.size() < 6) {
                        arrBomBoss.add(new BomBoss(BitmapFactory.decodeResource(getResources(), R.drawable.bomboss), boss.getX() + 347, boss.getY() + 269, 46, 81, 3, 0));
                        arrBomBoss.add(new BomBoss(BitmapFactory.decodeResource(getResources(), R.drawable.bomboss), boss.getX() + 347, boss.getY() + 269, 46, 81, 3, 1));
                        arrBomBoss.add(new BomBoss(BitmapFactory.decodeResource(getResources(), R.drawable.bomboss), boss.getX() + 347, boss.getY() + 269, 46, 81, 3, 2));
                        arrBomBoss.add(new BomBoss(BitmapFactory.decodeResource(getResources(), R.drawable.bomboss), boss.getX() + 347, boss.getY() + 269, 46, 81, 3, 3));
                        arrBomBoss.add(new BomBoss(BitmapFactory.decodeResource(getResources(), R.drawable.bomboss), boss.getX() + 347, boss.getY() + 269, 46, 81, 3, 4));
                        arrBomBoss.add(new BomBoss(BitmapFactory.decodeResource(getResources(), R.drawable.bomboss), boss.getX() + 347, boss.getY() + 269, 46, 81, 3, 5));
                        arrBomBoss.add(new BomBoss(BitmapFactory.decodeResource(getResources(), R.drawable.bomboss), boss.getX() + 347, boss.getY() + 269, 46, 81, 3, 6));
                        arrBomBoss.add(new BomBoss(BitmapFactory.decodeResource(getResources(), R.drawable.bomboss), boss.getX() + 347, boss.getY() + 269, 46, 81, 3, 7));
                        arrBomBoss.add(new BomBoss(BitmapFactory.decodeResource(getResources(), R.drawable.bomboss), boss.getX() + 347, boss.getY() + 269, 46, 81, 3, 8));
                        arrBomBoss.add(new BomBoss(BitmapFactory.decodeResource(getResources(), R.drawable.bomboss), boss.getX() + 347, boss.getY() + 269, 46, 81, 3, 9));
                        arrBomBoss.add(new BomBoss(BitmapFactory.decodeResource(getResources(), R.drawable.bomboss), boss.getX() + 347, boss.getY() + 269, 46, 81, 3, 10));
                        arrBomBoss.add(new BomBoss(BitmapFactory.decodeResource(getResources(), R.drawable.bomboss), boss.getX() + 347, boss.getY() + 269, 46, 81, 3, 11));
                        arrBomBoss.add(new BomBoss(BitmapFactory.decodeResource(getResources(), R.drawable.bomboss), boss.getX() + 347, boss.getY() + 269, 46, 81, 3, 12));

                    }
                }

                boss.update();
                timeBoss = System.nanoTime();
            }
        }
    }

    public  void  HP()
    {
        //Vẽ thêm mạng vào
        if(point==10)
        {
            if(arrHeard.size()<1)
            {
                int temp = random.nextInt(1080);
                arrHeard.add(new Heard(BitmapFactory.decodeResource(getResources(), R.drawable.heard), temp, 0, 31, 31, 5));
            }
        }
        else
        if(point%30==0&&point!=0)
        {
            if(arrHeard.size()<1)
            {
                int temp = random.nextInt(1080);
                arrHeard.add(new Heard(BitmapFactory.decodeResource(getResources(), R.drawable.heard), temp, 0, 31, 31, 5));
            }
        }
        for(int i=0;i<arrHeard.size();i++)
        {
            arrHeard.get(i).update();
            if(arrHeard.get(i).getY()>1920) {
                arrHeard.remove(i);
            }
            if(collision(player,arrHeard.get(i)))
            {
                arrHeard.remove(i);
                turn++;
            }
        }
    }



    //Hàm xử lý va chạm
    public  boolean collision(GameObject a,GameObject b)
    {
        if(Rect.intersects(a.getRectangle(),b.getRectangle()))
        {
            return true;
        }
        return false;
    }

    //Vẽ cac thứ lên canvas
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
             for(Heard heard:arrHeard)
             {
                 heard.draw(canvas);
             }

              boss.draw(canvas);

            for(BomBoss bomBoss:arrBomBoss)
            {
                bomBoss.draw(canvas);
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
        canvas.drawText("Heard: " + turn, 5, 120, paint);
        if(hpboss==0)
        {
            canvas.drawText("You Win",WIDTH/2-200,HEIGHT/2-100,paint1);
        }

        canvas.drawText("Hp Boss: " + hpboss, 5, 160, paint);
        if(!player.getPlaying())
        {
            canvas.drawText("Please to start game",WIDTH/2-200,HEIGHT/2-50,paint1);
            canvas.drawText("Point for you:"+pointmax,WIDTH/2-200,HEIGHT/2+10,paint1);
        }
    }
}
