package com.panszzz.newsight;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.panszzz.newsight.utils.PermissionUtils;
import com.panszzz.newsight.utils.SpeechUtil;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class InterestActivity extends AppCompatActivity {

    private SpeechUtil mSpeechUtil;


    private Button button0;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button button6;
    private Button button7;
    private Button button8;
    private Button button9;
    private Button button10;
    private Button button11;
    private Button button12;
    private Button button13;
    private Button button14;

    private Button btn_finish;

    private int num = 0;

    private UUID uuid;

    private String url = "http://1.116.213.5:8080/reader/init_interest";
    private String json;
    private String result;

    private boolean isC[] = new boolean[15];
    private int interests[] = new int[]{R.string.interest_0,
    R.string.interest_1,R.string.interest_2,R.string.interest_3,
    R.string.interest_4,R.string.interest_5,R.string.interest_6,
    R.string.interest_7,R.string.interest_8,R.string.interest_9,
    R.string.interest_10,R.string.interest_11,R.string.interest_12,
    R.string.interest_13,R.string.interest_14 };
    private JsonArray interestArray = new JsonArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest);

        uuid = UUID.randomUUID();

        // 初始化讯飞语音
        mSpeechUtil = SpeechUtil.getInstance(getApplicationContext());

        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,

        };
        String[] permissionsNeedToGrant = PermissionUtils.checkPermission(this,
                permissions);
        if (permissionsNeedToGrant != null) {
            PermissionUtils.grantPermission(this, permissionsNeedToGrant,
                    PermissionUtils.REQUEST_GRANT_READ_AND_WRITE_EXTERNAL_STORAGE_PERMISSIONS);
        }

        //播放标题
        mSpeechUtil.speaking(getString(R.string.interest_titile1)+getString(R.string.interest_titile2)+getString(R.string.interest_titile3));


        button0 = findViewById(R.id.btn_int0);
        button1 = findViewById(R.id.btn_int1);
        button2 = findViewById(R.id.btn_int2);
        button3 = findViewById(R.id.btn_int3);
        button4 = findViewById(R.id.btn_int4);
        button5 = findViewById(R.id.btn_int5);
        button6 = findViewById(R.id.btn_int6);
        button7 = findViewById(R.id.btn_int7);
        button8 = findViewById(R.id.btn_int8);
        button9 = findViewById(R.id.btn_int9);
        button10 = findViewById(R.id.btn_int10);
        button11 = findViewById(R.id.btn_int11);
        button12 = findViewById(R.id.btn_int12);
        button13 = findViewById(R.id.btn_int13);
        button14 = findViewById(R.id.btn_int14);
        btn_finish = findViewById(R.id.btn_finsh);

        for(int i=0;i<15;i++)
            isC[i] = false;


        findViewById(R.id.btn_finsh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(num==0)
                {
                    Toast.makeText(InterestActivity.this,"请选择至少一个兴趣！",Toast.LENGTH_SHORT).show();
                    mSpeechUtil.speaking("请选择至少一个兴趣！");
                }
                else
                {

                    for(int i=0;i<15;i++)
                    {
                        if(isC[i])
                        {
                            interestArray.add(getString(interests[i]));
                        }
                    }

                    //上传用户信息
                    HashMap<String,String> map = new HashMap<>();
                    map.put("userUid",uuid.toString());
                    map.put("labelList",interestArray.toString());
                    Gson gson = new Gson();
                    json = gson.toJson(map);

                    UserEntity user_interest = new UserEntity(uuid.toString(),interestArray);
                    Gson gson1 = new Gson();
                    final String userJson = gson1.toJson(user_interest);
                    RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8")
                            , userJson);

//                    RequestBody requestBody =  new FormBody.Builder()
//                        .add("userUid",uuid.toString())
//                        .add("labelList",interestArray)
//                        .build();
//                    OkHttpClient client = new OkHttpClient().newBuilder()
//                            .build();
//                    MediaType mediaType = MediaType.parse("application/json");
//                    RequestBody body = RequestBody.create(mediaType, "{\n  \"userUid\":\"1354235\",\n  \"labelList\":[\"教育\",\"交通\"]\n}");

                    OkHttpClient okHttpClient = new OkHttpClient();
                    final Request request = new Request.Builder()
                            .url(url)
                            .post(requestBody)
                            .build();

                    Call call = okHttpClient.newCall(request);

                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.d("@InterestActivity上传用户信息：", e.getMessage());
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {

                            result = response.body().string();
                            Log.d("@InterestActivity", "上传用户信息: " +userJson+"\n结果："+ result);
                        }
                    });

                    //创建一个SharedPreferences对象
                    SharedPreferences sharedPreferences= getSharedPreferences("user", Context.MODE_PRIVATE);
                    //实例化SharedPreferences.Editor对象
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isLogin",true);
                    editor.putString("userId",uuid+"");
                    editor.putString("inters",interestArray.toString());
                    editor.commit();
                    Log.d("UUID:",uuid+"");
                    startActivity(new Intent(InterestActivity.this,MainActivity.class));
                    mSpeechUtil.speaking("进入首页，正在为您加载个性化新闻");
                }

            }
        });


        /**
         * 选择按钮**/
        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isC[0])
                {
                    mSpeechUtil.speaking("您取消了"+getString(R.string.interest_0));
                    button0.setBackground(getDrawable(R.drawable.btn_k));
                    button0.setTextColor(getColor(R.color.grey_45));
                    isC[0] = false;num--;
                }
                else{
                    mSpeechUtil.speaking("您选择了"+getString(R.string.interest_0));
                    button0.setBackground(getDrawable(R.drawable.btn_c));
                    button0.setTextColor(getColor(R.color.white));
                    isC[0] = true;num++;
                }

                if(num==0)
                {
                    btn_finish.setBackground(getDrawable(R.drawable.btn_0));
                }
                else
                {
                    btn_finish.setBackground(getDrawable(R.drawable.btn_1));
                }
            }
        });


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isC[1])
                {
                    mSpeechUtil.speaking("您取消了"+getString(R.string.interest_1));
                    button1.setBackground(getDrawable(R.drawable.btn_k));
                    button1.setTextColor(getColor(R.color.grey_45));
                    isC[1] = false;num--;
                    num--;
                }
                else{
                    mSpeechUtil.speaking("您选择了"+getString(R.string.interest_1));
                    button1.setBackground(getDrawable(R.drawable.btn_c));
                    button1.setTextColor(getColor(R.color.white));
                    isC[1] = true;num++;
                    num++;
                }

                if(num==0)
                {
                    btn_finish.setBackground(getDrawable(R.drawable.btn_0));
                }
                else
                {
                    btn_finish.setBackground(getDrawable(R.drawable.btn_1));
                }
            }
        });


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isC[2])
                {
                    mSpeechUtil.speaking("您取消了"+getString(R.string.interest_2));
                    button2.setBackground(getDrawable(R.drawable.btn_k));
                    button2.setTextColor(getColor(R.color.grey_45));
                    isC[2] = false;num--;
                }
                else{
                    mSpeechUtil.speaking("您选择了"+getString(R.string.interest_2));
                    button2.setBackground(getDrawable(R.drawable.btn_c));
                    button2.setTextColor(getColor(R.color.white));
                    isC[2] = true;num++;
                }

                if(num==0)
                {
                    btn_finish.setBackground(getDrawable(R.drawable.btn_0));
                }
                else
                {
                    btn_finish.setBackground(getDrawable(R.drawable.btn_1));
                }
            }
        });


        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isC[3])
                {
                    mSpeechUtil.speaking("您取消了"+getString(R.string.interest_3));
                    button3.setBackground(getDrawable(R.drawable.btn_k));
                    button3.setTextColor(getColor(R.color.grey_45));
                    isC[3] = false;num--;
                }
                else{
                    mSpeechUtil.speaking("您选择了"+getString(R.string.interest_3));
                    button3.setBackground(getDrawable(R.drawable.btn_c));
                    button3.setTextColor(getColor(R.color.white));
                    isC[3] = true;num++;
                }

                if(num==0)
                {
                    btn_finish.setBackground(getDrawable(R.drawable.btn_0));
                }
                else
                {
                    btn_finish.setBackground(getDrawable(R.drawable.btn_1));
                }
            }
        });


        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isC[4])
                {
                    mSpeechUtil.speaking("您取消了"+getString(R.string.interest_4));
                    button4.setBackground(getDrawable(R.drawable.btn_k));
                    button4.setTextColor(getColor(R.color.grey_45));
                    isC[4] = false;num--;
                }
                else{
                    mSpeechUtil.speaking("您选择了"+getString(R.string.interest_4));
                    button4.setBackground(getDrawable(R.drawable.btn_c));
                    button4.setTextColor(getColor(R.color.white));
                    isC[4] = true;num++;
                }

                if(num==0)
                {
                    btn_finish.setBackground(getDrawable(R.drawable.btn_0));
                }
                else
                {
                    btn_finish.setBackground(getDrawable(R.drawable.btn_1));
                }
            }
        });


        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isC[5])
                {
                    mSpeechUtil.speaking("您取消了"+getString(R.string.interest_5));
                    button5.setBackground(getDrawable(R.drawable.btn_k));
                    button5.setTextColor(getColor(R.color.grey_45));
                    isC[5] = false;num--;
                }
                else{
                    mSpeechUtil.speaking("您选择了"+getString(R.string.interest_5));
                    button5.setBackground(getDrawable(R.drawable.btn_c));
                    button5.setTextColor(getColor(R.color.white));
                    isC[5] = true;num++;
                }

                if(num==0)
                {
                    btn_finish.setBackground(getDrawable(R.drawable.btn_0));
                }
                else
                {
                    btn_finish.setBackground(getDrawable(R.drawable.btn_1));
                }
            }
        });


        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isC[6])
                {
                    mSpeechUtil.speaking("您取消了"+getString(R.string.interest_6));
                    button6.setBackground(getDrawable(R.drawable.btn_k));
                    button6.setTextColor(getColor(R.color.grey_45));
                    isC[6] = false;num--;
                }
                else{
                    mSpeechUtil.speaking("您选择了"+getString(R.string.interest_6));
                    button6.setBackground(getDrawable(R.drawable.btn_c));
                    button6.setTextColor(getColor(R.color.white));
                    isC[6] = true;num++;
                }

                if(num==0)
                {
                    btn_finish.setBackground(getDrawable(R.drawable.btn_0));
                }
                else
                {
                    btn_finish.setBackground(getDrawable(R.drawable.btn_1));
                }
            }
        });


        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isC[7])
                {
                    mSpeechUtil.speaking("您取消了"+getString(R.string.interest_7));
                    button7.setBackground(getDrawable(R.drawable.btn_k));
                    button7.setTextColor(getColor(R.color.grey_45));
                    isC[7] = false;num--;
                }
                else{
                    mSpeechUtil.speaking("您选择了"+getString(R.string.interest_7));
                    button7.setBackground(getDrawable(R.drawable.btn_c));
                    button7.setTextColor(getColor(R.color.white));
                    isC[7] = true;num++;
                }

                if(num==0)
                {
                    btn_finish.setBackground(getDrawable(R.drawable.btn_0));
                }
                else
                {
                    btn_finish.setBackground(getDrawable(R.drawable.btn_1));
                }
            }
        });


        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isC[8])
                {
                    mSpeechUtil.speaking("您取消了"+getString(R.string.interest_8));
                    button8.setBackground(getDrawable(R.drawable.btn_k));
                    button8.setTextColor(getColor(R.color.grey_45));
                    isC[8] = false;num--;
                }
                else{
                    mSpeechUtil.speaking("您选择了"+getString(R.string.interest_8));
                    button8.setBackground(getDrawable(R.drawable.btn_c));
                    button8.setTextColor(getColor(R.color.white));
                    isC[8] = true;num++;
                }

                if(num==0)
                {
                    btn_finish.setBackground(getDrawable(R.drawable.btn_0));
                }
                else
                {
                    btn_finish.setBackground(getDrawable(R.drawable.btn_1));
                }
            }
        });


        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isC[9])
                {
                    mSpeechUtil.speaking("您取消了"+getString(R.string.interest_9));
                    button9.setBackground(getDrawable(R.drawable.btn_k));
                    button9.setTextColor(getColor(R.color.grey_45));
                    isC[9] = false;num--;
                }
                else{
                    mSpeechUtil.speaking("您选择了"+getString(R.string.interest_9));
                    button9.setBackground(getDrawable(R.drawable.btn_c));
                    button9.setTextColor(getColor(R.color.white));
                    isC[9] = true;num++;
                }

                if(num==0)
                {
                    btn_finish.setBackground(getDrawable(R.drawable.btn_0));
                }
                else
                {
                    btn_finish.setBackground(getDrawable(R.drawable.btn_1));
                }
            }
        });


        button10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isC[10])
                {
                    mSpeechUtil.speaking("您取消了"+getString(R.string.interest_10));
                    button10.setBackground(getDrawable(R.drawable.btn_k));
                    button10.setTextColor(getColor(R.color.grey_45));
                    isC[10] = false;num--;
                }
                else{
                    mSpeechUtil.speaking("您选择了"+getString(R.string.interest_10));
                    button10.setBackground(getDrawable(R.drawable.btn_c));
                    button10.setTextColor(getColor(R.color.white));
                    isC[10] = true;num++;
                }

                if(num==0)
                {
                    btn_finish.setBackground(getDrawable(R.drawable.btn_0));
                }
                else
                {
                    btn_finish.setBackground(getDrawable(R.drawable.btn_1));
                }
            }
        });


        button11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isC[11])
                {
                    mSpeechUtil.speaking("您取消了"+getString(R.string.interest_11));
                    button11.setBackground(getDrawable(R.drawable.btn_k));
                    button11.setTextColor(getColor(R.color.grey_45));
                    isC[11] = false;num--;
                }
                else{
                    mSpeechUtil.speaking("您选择了"+getString(R.string.interest_11));
                    button11.setBackground(getDrawable(R.drawable.btn_c));
                    button11.setTextColor(getColor(R.color.white));
                    isC[11] = true;num++;
                }

                if(num==0)
                {
                    btn_finish.setBackground(getDrawable(R.drawable.btn_0));
                }
                else
                {
                    btn_finish.setBackground(getDrawable(R.drawable.btn_1));
                }
            }
        });


        button12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isC[12])
                {
                    mSpeechUtil.speaking("您取消了"+getString(R.string.interest_12));
                    button12.setBackground(getDrawable(R.drawable.btn_k));
                    button12.setTextColor(getColor(R.color.grey_45));
                    isC[12] = false;num--;
                }
                else{
                    mSpeechUtil.speaking("您选择了"+getString(R.string.interest_12));
                    button12.setBackground(getDrawable(R.drawable.btn_c));
                    button12.setTextColor(getColor(R.color.white));
                    isC[12] = true;num++;
                }

                if(num==0)
                {
                    btn_finish.setBackground(getDrawable(R.drawable.btn_0));
                }
                else
                {
                    btn_finish.setBackground(getDrawable(R.drawable.btn_1));
                }
            }
        });

        button13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isC[13])
                {
                    mSpeechUtil.speaking("您取消了"+getString(R.string.interest_13));
                    button13.setBackground(getDrawable(R.drawable.btn_k));
                    button13.setTextColor(getColor(R.color.grey_45));
                    isC[13] = false;num--;
                }
                else{
                    mSpeechUtil.speaking("您选择了"+getString(R.string.interest_13));
                    button13.setBackground(getDrawable(R.drawable.btn_c));
                    button13.setTextColor(getColor(R.color.white));
                    isC[13] = true;num++;
                }

                if(num==0)
                {
                    btn_finish.setBackground(getDrawable(R.drawable.btn_0));
                }
                else
                {
                    btn_finish.setBackground(getDrawable(R.drawable.btn_1));
                }
            }
        });

        button14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isC[14])
                {
                    mSpeechUtil.speaking("您取消了"+getString(R.string.interest_14));
                    button14.setBackground(getDrawable(R.drawable.btn_k));
                    button14.setTextColor(getColor(R.color.grey_45));
                    isC[14] = false;num--;
                }
                else{
                    mSpeechUtil.speaking("您选择了"+getString(R.string.interest_14));
                    button14.setBackground(getDrawable(R.drawable.btn_c));
                    button14.setTextColor(getColor(R.color.white));
                    isC[14] = true;num++;
                }

                if(num==0)
                {
                    btn_finish.setBackground(getDrawable(R.drawable.btn_0));
                }
                else
                {
                    btn_finish.setBackground(getDrawable(R.drawable.btn_1));
                }
            }
        });
    }
}