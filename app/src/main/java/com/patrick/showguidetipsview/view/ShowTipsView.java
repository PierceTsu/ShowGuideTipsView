package com.patrick.showguidetipsview.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * @author Frederico Silva (fredericojssilva@gmail.com)
 * @date Oct 31, 2014
 */
public class ShowTipsView extends RelativeLayout {
	private Point showhintPoints;
	private int radius = 0;
	private int height = 0;

	//private String title, description, button_text;
	private int ivResId;
	private boolean custom, displayOneTime, isCirlcle;
	private int displayOneTimeID = 0;
	private int delay = 0;
	private float boundRadius = 0;

	private ShowTipsViewInterface callback;

	private View targetView;
	private int screenX, screenY;

	private int background_color, circleColor;
	private Drawable closeButtonDrawableBG;

	private int background_alpha = 220;

	private StoreUtils showTipsStore;
	
	private Bitmap bitmap;
	private Canvas temp;
	private Paint paint;
	private Paint bitmapPaint;
	private Paint circleline;
	private Paint transparentPaint;
	private PorterDuffXfermode porterDuffXfermode;
	private RectF mRect;

	public ShowTipsView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public ShowTipsView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ShowTipsView(Context context) {
		super(context);
		init();
	}

	private void init() {
		this.setVisibility(View.GONE);
		this.setBackgroundColor(Color.TRANSPARENT);

		this.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// DO NOTHING
				// HACK TO BLOCK CLICKS

			}
		});

		showTipsStore = new StoreUtils(getContext());
		
		paint = new Paint();
		bitmapPaint = new Paint();
		circleline = new Paint();
		transparentPaint = new Paint();
		porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		// Get screen dimensions
		screenX = w;
		screenY = h;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		/*
		 * Draw circle and transparency background
		 */
		
		/* 
		 * Since bitmap needs the canva's size, it wont be load at init() 
		 * To prevent the DrawAllocation issue on low memory devices, the bitmap will be instantiate only when its null
		 */
		if (bitmap == null) {
			bitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_4444);
			temp = new Canvas(bitmap);
		}
		
		if (background_color != 0)
			paint.setColor(background_color);
		else
			paint.setColor(Color.parseColor("#000000"));
		
		paint.setAlpha(background_alpha);
		temp.drawRect(0, 0, temp.getWidth(), temp.getHeight(), paint);

		transparentPaint.setColor(getResources().getColor(android.R.color.transparent));
		transparentPaint.setXfermode(porterDuffXfermode);

		int x = showhintPoints.x;
		int y = showhintPoints.y;

		if (isCirlcle) {
			temp.drawCircle(x, y, radius, transparentPaint);
		}else{
		/*------------------begin--------------------*/
			mRect = new RectF(x - radius,y-height/2,x+radius,y+height/2);
			temp.drawRoundRect(mRect,getRadius(),getRadius(),transparentPaint);
		/*------------------end--------------------*/
		}
		canvas.drawBitmap(bitmap, 0, 0, bitmapPaint);

		circleline.setStyle(Paint.Style.STROKE);
		if (circleColor != 0)
			circleline.setColor(circleColor);
		else
			circleline.setColor(Color.WHITE);
		
		circleline.setAntiAlias(true);
		circleline.setStrokeWidth(3);

		if(isCirlcle){
			canvas.drawCircle(x, y, radius, circleline);
		}else{
			/*------------------begin--------------------*/
			//canvas.drawRect(rect,circleline);
			canvas.drawRoundRect(mRect,getRadius(),getRadius(),circleline);
		/*------------------end--------------------*/
		}

	}

	boolean isMeasured;

	public void show(final Activity activity) {
		if (isDisplayOneTime() && showTipsStore.hasShown(getDisplayOneTimeID())) {
			setVisibility(View.GONE);
			((ViewGroup) ((Activity) getContext()).getWindow().getDecorView()).removeView(ShowTipsView.this);
			return;
		} else {
			if (isDisplayOneTime())
				showTipsStore.storeShownId(getDisplayOneTimeID());
		}

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				((ViewGroup) activity.getWindow().getDecorView()).addView(ShowTipsView.this);

				ShowTipsView.this.setVisibility(View.VISIBLE);
				//Animation fadeInAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
				//ShowTipsView.this.startAnimation(fadeInAnimation);
				final ViewTreeObserver observer = targetView.getViewTreeObserver();
				observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {

						if (isMeasured)
							return;

						if (targetView.getHeight() > 0 && targetView.getWidth() > 0) {
							isMeasured = true;

						}

						if (custom == false) {
							int[] location = new int[2];
							targetView.getLocationInWindow(location);
							int x = location[0] + targetView.getWidth() / 2;
							int y = location[1] + targetView.getHeight() / 2;
							// Log.d("FRED", "X:" + x + " Y: " + y);

							Point p = new Point(x, y);

							showhintPoints = p;
							radius = targetView.getWidth() / 2;
							height = targetView.getHeight();
						} else {
							int[] location = new int[2];
							targetView.getLocationInWindow(location);
							int x = location[0] + showhintPoints.x;
							int y = location[1] + showhintPoints.y;
							// Log.d("FRED", "X:" + x + " Y: " + y);

							Point p = new Point(x, y);

							showhintPoints = p;

						}

						invalidate();

						createViews();

					}
				});
			}
		}, getDelay());
	}

	/*
	 * Create text views and close button
	 * add ImageView
	 */
	private void createViews() {
		this.removeAllViews();

		//RelativeLayout texts_layout = new RelativeLayout(getContext());
		RelativeLayout guide_layout = new RelativeLayout(getContext());
		LayoutParams guideParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		/*------------------add ImageView--------------------*/
		ImageView guideIv = new ImageView(getContext());
		guideIv.setImageResource(getImageResource());
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.BELOW);
		guideIv.setLayoutParams(params);
		guide_layout.addView(guideIv);

		guideIv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (getCallback() != null)
					getCallback().gotItClicked();

				setVisibility(View.GONE);
				((ViewGroup) ((Activity) getContext()).getWindow().getDecorView())
						.removeView(ShowTipsView.this);

			}
		});
		/*--------------------------------------*/

		if (screenY / 2 > showhintPoints.y) {
			// textBlock under the highlight circle
			guideParams.height = (showhintPoints.y + radius) - screenY;
			guideParams.topMargin = (showhintPoints.y + radius);
			guide_layout.setGravity(Gravity.START | Gravity.TOP);

			guide_layout.setPadding(50, 50, 50, 50);
		} else {
			// textBlock above the highlight circle
			guideParams.height = showhintPoints.y - 80/* - radius*/;

			guide_layout.setGravity(Gravity.START | Gravity.BOTTOM);

			guide_layout.setPadding(50, 50, 50, 50);
		}

		guide_layout.setLayoutParams(guideParams);
		this.addView(guide_layout);

	}

	public void setTarget(View v) {
		targetView = v;
	}

	public void setTarget(View v, int x, int y, int radius) {
		custom = true;
		targetView = v;
		Point p = new Point(x, y);
		showhintPoints = p;
		this.radius = radius;
	}

	static Point getShowcasePointFromView(View view) {
		Point result = new Point();
		result.x = view.getLeft() + view.getWidth() / 2;
		result.y = view.getTop() + view.getHeight() / 2;
		return result;
	}

	public int getImageResource(){
		return ivResId;
	}

	public void setImageResource(int ivResId){
		this.ivResId = ivResId;
	}

	public boolean isDisplayOneTime() {
		return displayOneTime;
	}

	public void setDisplayOneTime(boolean displayOneTime) {
		this.displayOneTime = displayOneTime;
	}

	public boolean isCirlcle(){
		return isCirlcle;
	}

	public void setIsCircle(boolean isCircle){
		this.isCirlcle = isCircle;
	}

	public ShowTipsViewInterface getCallback() {
		return callback;
	}

	public void setCallback(ShowTipsViewInterface callback) {
		this.callback = callback;
	}

	public int getDelay() {
		return delay;
	}

	public float getRadius() {
		return boundRadius;
	}

	public void setRadius(float boundRadius) {
		this.boundRadius = boundRadius;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public int getDisplayOneTimeID() {
		return displayOneTimeID;
	}

	public void setDisplayOneTimeID(int displayOneTimeID) {
		this.displayOneTimeID = displayOneTimeID;
	}

	public int getBackground_color() {
		return background_color;
	}

	public void setBackground_color(int background_color) {
		this.background_color = background_color;
	}

	public int getCircleColor() {
		return circleColor;
	}

	public void setCircleColor(int circleColor) {
		this.circleColor = circleColor;
	}

	public int getBackground_alpha() {
		return background_alpha;
	}

	public void setBackground_alpha(int background_alpha) {
		if(background_alpha>255)
			this.background_alpha = 255;
		else if(background_alpha<0)
			this.background_alpha = 0;
		else
			this.background_alpha = background_alpha;

	}

	public Drawable getCloseButtonDrawableBG() {
		return closeButtonDrawableBG;
	}

	public void setCloseButtonDrawableBG(Drawable closeButtonDrawableBG) {
		this.closeButtonDrawableBG = closeButtonDrawableBG;
	}
}
