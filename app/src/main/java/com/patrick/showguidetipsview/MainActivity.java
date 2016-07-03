package com.patrick.showguidetipsview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.patrick.showguidetipsview.view.ShowTipsBuilder;
import com.patrick.showguidetipsview.view.ShowTipsView;
import com.patrick.showguidetipsview.view.ShowTipsViewInterface;

public class MainActivity extends AppCompatActivity {

    private float mDensity;
    private Button mBtnOne;
    private TextView mTvTwo;
    private Button mBtnThree;
    private ScrollView mScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        iniView();
        guideOne();
    }

    private void iniView() {
        mScrollView = (ScrollView) findViewById(R.id.sv);
        mBtnOne = (Button) findViewById(R.id.btn_step_one);
        mTvTwo = (TextView) findViewById(R.id.tv_step_two);
        mBtnThree = (Button) findViewById(R.id.btn_step_three);
    }

    private void initData() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        mDensity = dm.density;
    }

    private void guideOne() {
        ShowTipsView showMsgTips = new ShowTipsBuilder(this)
                .setBackgroundAlpha(110)    //设置背景的透明度:0~255
                .setRadius(8 * mDensity + .5f) //设置高亮矩形区域的圆角半径,默认为高亮区域为矩形
                .setTarget(mBtnOne) //需要高亮的view
                .setImageResource(R.mipmap.step_one)//设置高亮区域上面或者下面显示的图片
                .build();

        showMsgTips.show(this);

        showMsgTips.setCallback(new ShowTipsViewInterface(){
            @Override
            public void gotItClicked() {
                guideTwo();
            }
        });
    }

    private void guideTwo() {
        ShowTipsView showMsgTips = new ShowTipsBuilder(this)
                .setBackgroundAlpha(110)    //设置背景的透明度:0~255
                .isCircle(true) //高亮区域为圆形
                .setTarget(mTvTwo) //需要高亮的view
                .setImageResource(R.mipmap.step_two)//设置高亮区域上面或者下面显示的图片
                .build();

        showMsgTips.show(this);

        showMsgTips.setCallback(new ShowTipsViewInterface() {
            @Override
            public void gotItClicked() {
                mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                guideThree();
            }
        });
    }

    private void guideThree() {
        ShowTipsView showMsgTips = new ShowTipsBuilder(this)
                .setBackgroundAlpha(110)    //设置背景的透明度:0~255
                .setTarget(mBtnThree) //需要高亮的view
                .setDelay(500)
                .setRadius(5 * mDensity + .5f)
                .setImageResource(R.mipmap.step_three)//设置高亮区域上面显示的图片
                .build();

        showMsgTips.show(this);
    }

}
