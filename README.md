### ShowGuideTipsView
* 基于Frederico Silva的ShowTipsView修改而来,[https://github.com/fredericojssilva/ShowTipsView](https://github.com/fredericojssilva/ShowTipsView)
* 将ShowTipsView中的TextView和Button替换为ImageView
* 设置了ImageView的点击事件
* 增加了`setRadius(float boundRadius)`方法,用于设置高亮矩形区域的圆角半径,默认高亮区域为矩形
* 增加了`.isCircle(boolean isCircle)`方法, 设置高亮区域为圆形

![](/screenshoots/demo.gif)

### 使用说明
* copy demo中的view文件夹至你的工程中
* 设置ImageView的src

	``` java
	ShowTipsView showMsgTips = new ShowTipsBuilder(this)
                .setBackgroundAlpha(110)    //设置背景的透明度:0~255
                .setRadius(8 * mDensity + .5f) //设置高亮矩形区域的圆角半径,默认为高亮区域为矩形
                .setTarget(mBtnOne) //需要高亮的view
                .setImageResource(R.mipmap.step_one)//设置高亮区域上面或者下面显示的图片
                .build();

        showMsgTips.show(this);
		
		//点击图片的回调
        showMsgTips.setCallback(new ShowTipsViewInterface(){
            @Override
            public void gotItClicked() {
                guideTwo();
            }
        });
	```