package com.dsy.ActionBoneParse.view;

import com.dsy.ActionBoneParse.ArmatureActivity;
import com.dsy.ActionBoneParse.GameCanvas;
import com.dsy.ActionBoneParse.UserData;
import com.dsy.control.TouchEvent;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

public class MySurfaceView extends SurfaceView implements
		android.view.SurfaceHolder.Callback, Runnable
		{
	
	// 屏幕实际宽高
	public int screan_real_width;
	public int screan_real_height;

	// 线程,画布
	private Canvas canvas;
	private android.graphics.Canvas bufferCanvas = null; //绘制用的canvas
	private Bitmap bitmap_buffer = null;  //双缓冲图片
	private Paint paint = new Paint(); //用来绘制bit_buffer的paint, 主要用来对图片过滤和去毛边
		
	private SurfaceHolder sfh;
	private boolean flag;
	private Thread th;
	private Context context;
	
	public ArmatureActivity armatureActivity;
    
	// 屏幕原始矩形区域
//	public Rect screanRect;

	// Game
	public GameCanvas game;
		
	// 延时时间
	private int frameDelay;
	
	private boolean surfaceCreated, isAttached;
	
	public MySurfaceView(Context context, WindowManager windowManager, 
			ArmatureActivity armatureActivity) {
		super(context);
				
		sfh = this.getHolder();
		sfh.addCallback(this);
		setFocusable(true);
		setFocusableInTouchMode(true);
		setLongClickable(true);

		// 此处用于硬件加速
		try {
			// 适用于DMA(Direct memory access )引擎和硬件加速的Surface
			sfh.setType(SurfaceHolder.SURFACE_TYPE_HARDWARE);
		} catch (Exception e) {
			try {
				// 适用于GPU加速的Surface
				sfh.setType(SurfaceHolder.SURFACE_TYPE_GPU);
			} catch (Exception ee) {
				// 用RAM缓存原生数据的普通Surface
				sfh.setType(SurfaceHolder.SURFACE_TYPE_NORMAL);
			}
		}
        this.context = context;
        this.armatureActivity = armatureActivity;

		// 获取屏幕大小
		DisplayMetrics dm = new DisplayMetrics();
		// 取得窗口属性
		windowManager.getDefaultDisplay().getMetrics(dm);
		// float scaleDensity = dm.scaledDensity;
		// 窗口的宽度
		screan_real_width = dm.widthPixels;
		// 窗口高度
		screan_real_height = dm.heightPixels;
		
		TouchEvent.init(screan_real_width, screan_real_height);

		// 屏幕区域
//		screanRect = new Rect(0, 0, UserData.screan_width,
//				UserData.screan_height);
		
//		ALPHA_8        代表8位Alpha位图
//		ARGB_4444      代表16位ARGB位图
//		ARGB_8888     代表32位ARGB位图
//		RGB_565         代表8位RGB位图
		bitmap_buffer = Bitmap.createBitmap(800, 480, Config.RGB_565);
		bufferCanvas = new android.graphics.Canvas(bitmap_buffer);
		//设置去锯齿和过滤
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);

	}

	public void myDraw(Rect rect) {
		if (surfaceCreated && isAttached) {
		synchronized (sfh) {
			canvas = sfh.lockCanvas(rect);
			if(canvas != null){
				game.draw(bufferCanvas, paint);
//				screan_real_width, screan_real_height
				canvas.drawBitmap(bitmap_buffer, null, new Rect(0, 0, screan_real_width, screan_real_height), paint);
				
				sfh.unlockCanvasAndPost(canvas);
			}
		}
		}
	}

	public void logic(int dt) {
		TouchEvent.refresh();
		game.logic(dt);
	}

//	@Override
//	public boolean onTouchEvent(MotionEvent event) {	
//		return TouchEvent.touch(event);
//	}
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		myDraw(null);
	}
	
	public void surfaceCreated(SurfaceHolder holder) {
		surfaceCreated = true;
	}
	
	public void gameCreated() {
		game = new GameCanvas(context, this, bufferCanvas, paint);
//		game.initGameData();
				
		flag = true;
		th = new Thread(this);
		th.start();	
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		surfaceCreated = false;
	}
	
	/**
	 * 视图附在窗体时被调用，此时开始绘制
	 */
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		sfh.addCallback(this);
		isAttached = true;
		myDraw(null);
	}

	/**
	 * 视图从窗体移除时被调用，此时窗体不再绘制视图
	 */
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		sfh.removeCallback(this);
		isAttached = false;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {	
		return TouchEvent.touch(event);
	}
	
	// 离开游戏
	public void exitGameCommand() {
		flag = false;
	}
	
	// 销毁游戏
	public void destoryGame() {
		// 关闭音乐
		
		ArmatureActivity.getInstance().finish();
	}
	
	private long start;
	
	private long time2;
	
	public int timeDelta;
		
	public void run() {
		while (flag) {
			start = System.currentTimeMillis();
//			time2 = System.currentTimeMillis();
			myDraw(null);
			logic(timeDelta);
//			time2 = System.currentTimeMillis() - time2;
//			Log.e("timeDelta logic", "" + time2);
			start = System.currentTimeMillis() - start;
//			Log.e("delay time", "" + timeDelta);
			frameDelay = UserData.FRAME_DELAY;
			if (start < frameDelay) {
				timeDelta = frameDelay;
				try {
					Thread.sleep(frameDelay - start);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				timeDelta = (int) start;
			}
		}
		destoryGame();
	}
}
