package com.giaynhap.animation.animation;

import android.animation.ValueAnimator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewAnimator;

public class MainActivity extends AppCompatActivity {

    boolean drag = false;
    ViewAnimator animatorView;
    float start[]  ={0,0};
    float width = 0;
    SpringSystem spring ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        animatorView = (ViewAnimator)findViewById(R.id.viewAnimator);
        spring = new SpringSystem();

        spring.setOnEventUpdate(new SpringSystemUpdateEvent(){

            @Override
            public void onUpdate(float x, float y) {
                width = x;
                if (drag) return;
                if (animatorView==null) return;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (width<=0) width = 1;
                        animatorView.getLayoutParams().height = (int)width ;
                        animatorView.requestLayout();
                       }
                });


            }
            @Override
            public void onStop() {
                Log.i("Animation","Stop");
            }
        });


        animatorView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        drag = false;
                        spring.setPoint(200,0);
                        float size1 = event.getY() - start[1];
                        if (size1>=200) size1 =200;

                        spring.setRoot( (int)Math.floor(200+size1) ,0);
                        spring.start();
                        break;
                    case MotionEvent.ACTION_DOWN:
                        drag = true;
                        start[0] = event.getX();
                        start[1] = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float size = event.getY() - start[1];
                        if (size>=200) size =200;
                        animatorView.getLayoutParams().height = (int)Math.floor(200+size) ;
                        animatorView.requestLayout();


                        break;
                }
                return true;
            }
        });
    }
}
