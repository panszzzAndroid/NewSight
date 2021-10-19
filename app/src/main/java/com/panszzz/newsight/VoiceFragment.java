package com.panszzz.newsight;

import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.panszzz.newsight.utils.PermissionUtils;
import com.panszzz.newsight.utils.SpeechUtil;

public class VoiceFragment extends Fragment {


    private SpeechUtil mSpeechUtil;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 初始化讯飞语音
        mSpeechUtil = SpeechUtil.getInstance(getContext());

        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,

        };
        String[] permissionsNeedToGrant = PermissionUtils.checkPermission((Activity) getContext(),
                permissions);
        if (permissionsNeedToGrant != null) {
            PermissionUtils.grantPermission(getActivity(), permissionsNeedToGrant,
                    PermissionUtils.REQUEST_GRANT_READ_AND_WRITE_EXTERNAL_STORAGE_PERMISSIONS);
        }
    }
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_voice, container, false);


            view.findViewById(R.id.voice_1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String str = "【独家】中国共产党的第一声广播从窑洞里发出"+"长按收听广播";
                    mSpeechUtil.speaking(str);
                }
            });

            view.findViewById(R.id.voice_1).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Intent intent = new Intent(getContext(),VoiceActivity.class);
                    intent.putExtra("position",1);
                    intent.putExtra("title","【独家】中国共产党的第一声广播从窑洞里发出");
                    startActivity(intent);
                    return false;
                }
            });




        view.findViewById(R.id.voice_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = "习近平：共产党人拥有人格力量，才能赢得民心。"+"长按收听广播";
                mSpeechUtil.speaking(str);
            }
        });

        view.findViewById(R.id.voice_2).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(getContext(),VoiceActivity.class);
                intent.putExtra("position",2);
                intent.putExtra("title","习近平：共产党人拥有人格力量，才能赢得民心。");
                startActivity(intent);
                return false;
            }
        });




        view.findViewById(R.id.voice_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = "杨利伟说当年为训练买了台挺贵的摄像机";
                mSpeechUtil.speaking(str);
            }
        });

        view.findViewById(R.id.voice_3).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(getContext(),VoiceActivity.class);
                intent.putExtra("position",3);
                intent.putExtra("title","杨利伟说当年为训练买了台挺贵的摄像机");
                startActivity(intent);
                return false;
            }
        });




        view.findViewById(R.id.voice_4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = "今天，这个“白鹤亮翅”不简单！";
                mSpeechUtil.speaking(str);
            }
        });

        view.findViewById(R.id.voice_4).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(getContext(),VoiceActivity.class);
                intent.putExtra("title","今天，这个“白鹤亮翅”不简单！");
                intent.putExtra("position",4);
                startActivity(intent);
                return false;
            }
        });




        view.findViewById(R.id.voice_5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = "红军上战场前会相互交代两句话";
                mSpeechUtil.speaking(str);
            }
        });

        view.findViewById(R.id.voice_5).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(getContext(),VoiceActivity.class);
                intent.putExtra("title","红军上战场前会相互交代两句话");
                intent.putExtra("position",5);
                startActivity(intent);
                return false;
            }
        });




        return view;

    }

}
