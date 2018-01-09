package com.example.administrator.classdesign;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

public class StartActivity extends AppCompatActivity {

    ImageView ivStart ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start);
        ivStart = ( ImageView ) findViewById( R.id.image_view ) ;
        InitImage();

    }

    private void InitImage () {
        ivStart.setImageResource( R.drawable.start_animation ) ;
        ScaleAnimation scaleAnimation = new ScaleAnimation( 1.4f , 1.0f , 1.4f , 1.0f , Animation.RELATIVE_TO_SELF , 0.5f ) ;
        scaleAnimation.setFillAfter( true ) ;
        scaleAnimation.setDuration(3000);//设置时间为3秒
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startMainActivity();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ivStart.startAnimation( scaleAnimation ) ;
    }

    private void startMainActivity () {
        Intent intent = new Intent( StartActivity.this , MainActivity.class ) ;
        startActivity( intent );
        overridePendingTransition( android.R.anim.fade_in , android.R.anim.fade_out ) ;//淡入淡出
    }

}
