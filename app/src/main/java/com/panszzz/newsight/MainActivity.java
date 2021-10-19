package com.panszzz.newsight;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.panszzz.newsight.news.NewsFragment;
import com.panszzz.newsight.utils.PermissionUtils;
import com.panszzz.newsight.utils.SpeechUtil;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "Main";
    public static String curCategory = "top";

    private FragmentManager mFragmentManager;
    private Fragment mFragment = null;
    private Fragment curFragment = null;

    private SpeechUtil mSpeechUtil;
    private TextView search, help, cate_1, cate_2, cate_3, cate_4, cate_5, cate_6;

    private ViewPager mViewPager;

    private int is_init=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = findViewById(R.id.viewPager);

        mFragmentManager = getSupportFragmentManager();

        // 加载界面
        initView();

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
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case PermissionUtils.REQUEST_GRANT_READ_AND_WRITE_EXTERNAL_STORAGE_PERMISSIONS:
                if (PermissionUtils.isGrantedAllPermissions(permissions,
                        grantResults)) {
                    Toast.makeText(this, "你允许了全部授权", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this,
                            "你拒绝了部分权限，可能造成程序运行不正常",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }


    }

    private void initView() {

        //创建一个SharedPreferences对象
        SharedPreferences sharedPreferences= getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String userId = sharedPreferences.getString("userId",null);
//        Toast.makeText(MainActivity.this,"用户id:"+userId,Toast.LENGTH_SHORT).show();

        RadioButton mItemBtn1 = findViewById(R.id.item_btn_1);
        RadioButton mItemBtn2 = findViewById(R.id.item_btn_2);
        RadioButton mItemBtn3 = findViewById(R.id.item_btn_3);
        RadioButton mItemBtn4 = findViewById(R.id.item_btn_4);

        initBtn(mItemBtn1, R.drawable.ic_1_n, R.drawable.ic_1);
        initBtn(mItemBtn2, R.drawable.ic_2_n, R.drawable.ic_2);
        initBtn(mItemBtn3, R.drawable.ic_3_n, R.drawable.ic_3);
        initBtn(mItemBtn4, R.drawable.ic_4_n, R.drawable.ic_4);

        final RadioGroup mRadioGroup = findViewById(R.id.navigation);

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                radioGroup.check(i);

                switch (i) {
                    case R.id.item_btn_1:
                        mViewPager.setCurrentItem(0, true);
                        if(is_init!=0){
                            mSpeechUtil.speaking(getString(R.string.main_n1));
                        }
                        break;
                    case R.id.item_btn_2:
                        mViewPager.setCurrentItem(1, true);
                        mSpeechUtil.speaking(getString(R.string.main_n2));
                        is_init++;
                        break;
                    case R.id.item_btn_3:
                        mViewPager.setCurrentItem(2, true);
                        mSpeechUtil.speaking(getString(R.string.main_n3));
                        is_init++;
                        break;
                    case R.id.item_btn_4:
                        mViewPager.setCurrentItem(3, true);
                        mSpeechUtil.speaking(getString(R.string.main_n4));
                        is_init++;
                        break;

                }
            }
        });
        mItemBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        final ArrayList<Fragment> fgLists = new ArrayList<>();
        fgLists.add(new NewsFragment());
        fgLists.add(new VedioFragment());
        fgLists.add(new VoiceFragment());
        fgLists.add(new CollectionFragment());

        FragmentPagerAdapter mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fgLists.get(position);
            }

            @Override
            public int getCount() {
                return fgLists.size();
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                //super.destroyItem(container, position, object);
            }
        };
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(5);
        mViewPager.setCurrentItem(1);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mRadioGroup.check(R.id.item_btn_1);
                        break;
                    case 1:
                        mRadioGroup.check(R.id.item_btn_2);
                        break;
                    case 2:
                        mRadioGroup.check(R.id.item_btn_3);
                        break;
                    case 3:
                        mRadioGroup.check(R.id.item_btn_4);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        mItemBtn1.setChecked(true);


//        newFragment(mFragment);
//        search = findViewById(R.id.search);
//        search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mSpeechUtil.speaking(getString(R.string.long_click_to_search));
//            }
//        });
//        search.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                Intent i = new Intent(MainActivity.this, SearchActivity.class);
//                startActivity(i);
//                return true;
//            }
//        });
//
//        help = findViewById(R.id.help);
//        help.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mSpeechUtil.speaking(help.getText().toString());
//            }
//        });
//        help.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                Intent i = new Intent(MainActivity.this, HelpActivity.class);
//                startActivity(i);
//                return true;
//            }
//        });
//
//        cate_1 = findViewById(R.id.category_1);
//        cate_1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mSpeechUtil.speaking(cate_1.getText().toString());
//            }
//        });
//        cate_1.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                curCategory = "top";
//                newFragment(mFragment);
//                return true;
//            }
//        });
//
//        cate_2 = findViewById(R.id.category_2);
//        cate_2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mSpeechUtil.speaking(cate_2.getText().toString());
//            }
//        });
//        cate_2.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                curCategory = "guonei";
//                newFragment(mFragment);
//                return true;
//            }
//        });
//
//        cate_3 = findViewById(R.id.category_3);
//        cate_3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mSpeechUtil.speaking(cate_3.getText().toString());
//            }
//        });
//        cate_3.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                curCategory = "guoji";
//                newFragment(mFragment);
//                return true;
//            }
//        });
//
//        cate_4 = findViewById(R.id.category_4);
//        cate_4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mSpeechUtil.speaking(cate_4.getText().toString());
//            }
//        });
//        cate_4.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                curCategory = "tiyu";
//                newFragment(mFragment);
//                return true;
//            }
//        });
//
//        cate_5 = findViewById(R.id.category_5);
//        cate_5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mSpeechUtil.speaking(cate_5.getText().toString());
//            }
//        });
//        cate_5.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                curCategory = "junshi";
//                newFragment(mFragment);
//                return true;
//            }
//        });
//
//        cate_5 = findViewById(R.id.category_5);
//        cate_5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mSpeechUtil.speaking(cate_5.getText().toString());
//            }
//        });
//
//        cate_6 = findViewById(R.id.category_6);
//        cate_6.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mSpeechUtil.speaking(cate_6.getText().toString());
//            }
//        });
//        cate_6.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                curCategory = "keji";
//                newFragment(mFragment);
//                return true;
//            }
//        });
//    }
//
//    private void newFragment(Fragment fragment) {
//        FragmentTransaction transaction = mFragmentManager.beginTransaction();
//
//        fragment = new NewsFragment();
//        transaction.add(R.id.container, fragment);
//
//        // 隐藏当前的fragment
//        if (curFragment != null) {
//            transaction.hide(curFragment);
//        }
//
//        // 显示指定的fragment
//        transaction.show(fragment);
//
//        // 提交一个事务
//        transaction.commitAllowingStateLoss();
//
//        // 记录当前显示的fragment
//        curFragment = fragment;

    }

    private void initBtn(RadioButton btn, int checked, int unchecked) {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{android.R.attr.state_checked},
                getApplicationContext().getResources().getDrawable(checked));
        drawable.addState(new int[]{-android.R.attr.state_checked},
                getApplicationContext().getResources().getDrawable(unchecked));
        drawable.setBounds(0, 0, 90, 70);
        btn.setCompoundDrawables(null, drawable, null, null);
    }

}
