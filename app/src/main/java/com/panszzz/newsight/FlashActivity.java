package com.panszzz.newsight;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;

public class FlashActivity extends AppCompatActivity {


    private boolean isLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);

        LinearLayout layout=findViewById(R.id.activity_flash);
        AlphaAnimation alphaAnimation=new AlphaAnimation(0.1f,1.0f);
        alphaAnimation.setDuration(3000);//设置动画播放时长1000毫秒（1秒）
        layout.startAnimation(alphaAnimation);

        //设置动画监听
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            //动画结束
            @Override
            public void onAnimationEnd(Animation animation) {


                //创建一个SharedPreferences对象
                SharedPreferences sharedPreferences= getSharedPreferences("user", Context.MODE_PRIVATE);
                //实例化SharedPreferences.Editor对象
                SharedPreferences.Editor editor = sharedPreferences.edit();

                File f = new File(
                        "/data/data/com.xgh.newsight/shared_prefs/user.xml");
                if (f.exists())
                {
                    isLogin = sharedPreferences.getBoolean("isLogin",false);
                    Intent intent;
                    if(isLogin)
                    {
                        //页面的跳转
                        intent = new Intent(FlashActivity.this, MainActivity.class);
//                        Toast.makeText(FlashActivity.this,"已初始化",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        //页面的跳转
                        intent = new Intent(FlashActivity.this, InterestActivity.class);
//                        Toast.makeText(FlashActivity.this,"未初始化",Toast.LENGTH_SHORT).show();

                    }
                    startActivity(intent);
                }
                else
                {
                    //将获取过来的值放入文件
                    editor.putBoolean("isLogin",false);
                    //提交
                    editor.commit();
                    //页面的跳转
                    Intent intent=new Intent(FlashActivity.this,InterestActivity.class);
                   startActivity(intent);
//                    Toast.makeText(FlashActivity.this,"3",Toast.LENGTH_SHORT).show();

                }

            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

    }
}