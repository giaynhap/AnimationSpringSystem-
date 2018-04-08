package com.giaynhap.animation.animation;

import android.util.Log;

/**
 * Created by GiayNhap on 5/4/2018.
 */

public class SpringSystem implements Runnable {
    private float []point ={0,0};
    private float []root = {0,0};
    private float []vector ={0,0};
    private float []current = {0,0};
    private float k = 0.4f;
    private float g = 0.1f;
    private boolean playing = false;

    SpringSystemUpdateEvent evet;
    Thread thread;
    public void setOnEventUpdate(SpringSystemUpdateEvent evet)
    {
            this.evet = evet;
    }
    SpringSystem(){
        thread = new Thread(this,"SpringSystem");
        thread.start();
    }
    public void setK(float k)
    {
        this.k = k;
    }
    public void setG(float g)
    {
        this.g = g;
    }
    public void setRoot(float x,float y){
        root[0]=x;
        root[1]=y;
    }
    public void setPoint(float x,float y){
        point[0]=x;
        point[1]=y;
    }
    float[] getVec(){
        float [] vec = {point[0]-root[0],point[1]-root[1]};
        return vec;
    }
    float calcDistance()
    {
        float [] vec =getVec();
        return (float)Math.sqrt( vec[0]*vec[0]+vec[1]*vec[1]);
    }
    float calcDistance(float []vc1,float [] vc2)
    {
        return (float)Math.sqrt( (vc1[0]-vc2[0])*(vc1[0]-vc2[0])+(vc1[1]-vc2[1])*(vc1[1]-vc2[1]));
    }

    float [] normalVec(){
        float [] vec =getVec();
        float length = calcDistance();
        vec[0]/=length;
        vec[1]/=length;
        return  vec;
    }

    public void start()
    {
        playing =true;
        current[0] = root[0];
        current[1] = root[1];
        vector[0] = 0;
        vector[1] = 0;

    }

    public void update(){
        if (!playing) return;
        float flx = (current[0] - point[0]);
        float fly = (current[1] - point[1]);
        vector[0]-=flx* k;
        vector[1]-=fly* k;
        current[0]+= vector[0];
        current[1]+= vector[1];
        vector[0]/=g;
        vector[1]/=g;
        //stop
        if ( Math.abs( vector[0])<0.01 && Math.abs( vector[1])<0.01 && Math.abs(flx) < 0.1&& Math.abs(fly) <0.1 )
        {
            //fix point
            playing = false;
            current[0] = point[0];
            current[1] = point[1];
            if (evet!=null)evet.onStop();
        }
        if (evet!=null)evet.onUpdate(current[0],current[1]);
    }
    @Override
    public void run() {
        while(true)
        try {
            Thread.sleep(50);
            update();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
