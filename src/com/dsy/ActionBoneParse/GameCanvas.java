package com.dsy.ActionBoneParse;

import java.util.Arrays;

import com.dsy.ActionBoneParse.manager.AnimationManager;
import com.dsy.ActionBoneParse.manager.PlistManager;
import com.dsy.ActionBoneParse.util.BitmapManager;
import com.dsy.ActionBoneParse.util.Graphics;
import com.dsy.ActionBoneParse.view.MySurfaceView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.test.IsolatedContext;

public class GameCanvas implements UserData {

	public GameCanvas game;

	public Graphics g;

	public Context context;

	public MySurfaceView mySurfaceView;

	public Canvas canvas;

	public Paint paint;

	// 界面缩放比例
	public float scaleUi;
	
	public ArmatureObj armatureObj;
		
	public GameCanvas(Context context, MySurfaceView mySurfaceView,
			Canvas canvas, Paint paint) {

		this.mySurfaceView = mySurfaceView;
		this.canvas = canvas;
		this.paint = paint;

		game = this;

		this.context = context;

		g = new Graphics(canvas, paint, null);
		
		armatureObj = new ArmatureObj();
	}

	public void gameDestroy() {
		// saveGameData();

		mySurfaceView.exitGameCommand();
	}

	private PorterDuffXfermode porterDuffXfermodeClear = new PorterDuffXfermode(
			Mode.CLEAR);

	public void cleanScrean(Canvas canvas, Paint paint) {
		// 清屏
		paint.setXfermode(porterDuffXfermodeClear);
		canvas.drawPaint(paint);
		paint.setXfermode(null);
	}

	public void draw(Canvas canvas, Paint paint) {

		g.setCanvas(canvas);

		cleanScrean(canvas, paint);
		
		armatureObj.draw(g);
	}
	
	public void logic(int dt)
	{
		armatureObj.logic(dt);
	}
}
