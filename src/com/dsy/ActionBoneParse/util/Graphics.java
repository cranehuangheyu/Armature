package com.dsy.ActionBoneParse.util;

import com.dsy.ActionBoneParse.UserData;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Region.Op;
import android.util.Log;

public final class Graphics implements UserData {
	public static final int HCENTER = 1;
	public static final int VCENTER = 2;
	public static final int LEFT = 4;
	public static final int RIGHT = 8;
	public static final int TOP = 16;
	public static final int BOTTOM = 32;
	public static final int BASELINE = 64;
	public static final int SOLID = 0;
	public static final int DOTTED = 1;
	// private Font font = Font.getDefaultFont();
	private int strokeStyle = SOLID;
	private static final DashPathEffect dashPathEffect = new DashPathEffect(
			new float[] { 5, 5 }, 0);
	private int translateX = 0, translateY = 0;
	private int clipx, clipy, clipw, cliph;

	private android.graphics.Canvas canvas;
	private boolean isResetPainter = false;

	private Bitmap graphicbitmap; // copyarea时使用
	private Paint paint;

	// private Camera c;

	// private Camera c0;
	//
	// private Camera c1;
	//
	// private Camera tempC;

	// public void setCamera(Camera c) {
	// this.c = c;
	// this.c0 = c;
	// }

	// public void setCamera1(Camera c1) {
	// this.c1 = c1;
	// }

	// 暂存摄像机
	// public void saveCamera() {
	// tempC = c;
	// }

	// 恢复摄像机
	// public void restoreCamera() {
	// c = tempC;
	// }

	// 切换第一个摄像机
	// public void setCamera1() {
	// c = c0;
	// }

	// 切换第二个摄像机
	// public void setCamera2() {
	// c = c1;
	// }

	/* other 初始化Paint */
	private void initPaint() {
		paint.setAntiAlias(true);
	}

	/* 构造 */
	public Graphics() {
		initPaint();
	}

	/* 构造 */
	public Graphics(android.graphics.Canvas canvas) {
		this.canvas = canvas;
		initPaint();
	}

	/* Canvas 类需要用到 */
	public void setCanvas(android.graphics.Canvas canvas) {
		this.canvas = canvas;
		initPaint();
	}

	public Graphics(android.graphics.Canvas canvas, Paint painter, Bitmap bitmap) { // 创建一种图片的graphics
		this.canvas = canvas;
		this.graphicbitmap = bitmap;
		this.paint = painter;
		initPaint();
		fontMetrics = paint.getFontMetrics();
	}

	/* clipRect之前剪裁区与现在剪裁区的交集作为新的剪裁区 */
	public void clipRect(int x, int y, int width, int height) {
		canvas.clipRect(x, y, x + width, y + height, Op.INTERSECT);
		Rect rect = canvas.getClipBounds();
		clipx = rect.left;
		clipy = rect.top;
		clipw = rect.right - rect.left;
		cliph = rect.bottom - rect.top;
	}

	/* copyArea 必须使用 Image 的getGraphics 获取的Graphics才可以使用此方法 */
	private Bitmap area;
	private int rgb[];

	public void copyArea(int x_src, int y_src, int width, int height,
			int x_dest, int y_dest, int anchor) {
		if (graphicbitmap != null) {

			boolean anchorError = false;

			if (anchor == 0) {
				anchor = TOP | LEFT;
			}

			if ((anchor & BASELINE) != 0) {
				anchorError = true;
			}

			if ((anchor & TOP) != 0) {
				if ((anchor & (VCENTER | BOTTOM)) != 0) { // VCENTER水平
					anchorError = true;
				}
			} else if ((anchor & BOTTOM) != 0) {
				if ((anchor & VCENTER) != 0) {
					anchorError = true;
				} else {
					y_dest -= height - 1;
				}
			} else if ((anchor & VCENTER) != 0) {
				y_dest -= (height - 1) >>> 1;
			} else {
				anchorError = true;
			}

			if ((anchor & LEFT) != 0) {
				if ((anchor & (HCENTER | RIGHT)) != 0) { // HCENTER 垂直
					anchorError = true;
				}
			} else if ((anchor & RIGHT) != 0) {
				if ((anchor & HCENTER) != 0) {
					anchorError = true;
				} else {
					x_dest -= width - 1;
				}
			} else if ((anchor & HCENTER) != 0) {
				x_dest -= (width - 1) >>> 1;
			} else {
				anchorError = true;
			}

			if (anchorError) {
				throw new IllegalArgumentException("anchor error");
			}

			if (area != null && area.getWidth() == width
					&& area.getHeight() == height) {
				if (rgb == null
						|| (rgb != null && rgb.length != width * height)) {
					rgb = new int[width * height];
				}
				graphicbitmap.getPixels(rgb, 0, width, x_src, y_src, width,
						height);
				area.setPixels(rgb, 0, width, 0, 0, width, height);
			} else {
				area = Bitmap.createBitmap(graphicbitmap, x_src, y_src, width,
						height);
			}

			paint.setStyle(Style.STROKE);
			canvas.drawBitmap(area, x_dest, y_dest, paint);
			paint.setStyle(Style.FILL);

		}
	}

	/* drawArc绘制空心弧形 */
	public void drawArc(int x, int y, int width, int height, int startAngle,
			int arcAngle) {
		paint.setStyle(Style.STROKE);
		canvas.drawArc(new RectF(x, y, x + width, y + height), startAngle,
				-arcAngle, false, paint);
		paint.setStyle(Style.FILL);
	}

	/* 设置Font */
	// public void setFont(Font font){
	// this.font = font;
	// }

	/* 绘制字符 */
	private static final StringBuffer sb = new StringBuffer();

	public void drawChar(char character, int x, int y, int anchor) {
		sb.delete(0, sb.length());
		sb.append(character);

		drawString(sb.toString(), x, y, anchor);
	}

	/* 绘制字符串 */
	public void drawString(String str, int x, int y, int anchor) {
		if (anchor == 0) {
			anchor = TOP | LEFT;
		}
		if ((anchor & TOP) != 0) {
			y -= paint.getFontMetricsInt().top;
		} else if ((anchor & BOTTOM) != 0) {
			y -= paint.getFontMetricsInt().bottom;
		} else if ((anchor & VCENTER) != 0) {
			y += ((paint.getFontMetricsInt().descent - paint
					.getFontMetricsInt().ascent) / 2 - paint
					.getFontMetricsInt().descent);
		}

		if ((anchor & HCENTER) != 0) {
			paint.setTextAlign(Align.CENTER);
		} else if ((anchor & RIGHT) != 0) {
			paint.setTextAlign(Align.RIGHT);
		} else if ((anchor & LEFT) != 0) {
			paint.setTextAlign(Align.LEFT);
		}

		paint.setAntiAlias(true);
		canvas.drawText(str, x, y, paint);
		// float a = paint.getTextSize();
		// a++;
	}

	/* 绘制字符数组 */
	public void drawChars(char[] data, int offset, int length, int x, int y,
			int anchor) {
		sb.delete(0, sb.length());
		sb.append(data, offset, length);

		drawString(sb.toString(), x, y, anchor);
	}

	/* 绘制空心矩形 */
	public void drawRect(int x, int y, int width, int height) {
		paint.setStyle(Style.STROKE);

		canvas.drawRect(x, y, x + width, y + height, paint);
		paint.setStyle(Style.FILL);
	}

	// 绘制矩形
	public void drawRect(int x, int y, int width, int height, int color,
			int trans, Style style) {
		int oldColor = paint.getColor();
		int oldTrans = paint.getAlpha();

		paint.setStyle(style);
		paint.setColor(color);
		paint.setAlpha(trans);
		canvas.drawRect(x, y, x + width, y + height, paint);

		paint.setStyle(Style.FILL);
		paint.setColor(oldColor);
		paint.setAlpha(oldTrans);
	}

	public RectF rectf = new RectF();

	// 绘制椭圆
	public void drawOval(int x, int y, int width, int height, int color,
			int alpha) {
		int oldColor = paint.getColor();
		int oldTrans = paint.getAlpha();

		paint.setColor(color);
		paint.setAlpha(alpha);

		x -= width / 2;
		y -= height / 2;
		rectf.set(x, y, x + width, y + height);

		canvas.drawOval(rectf, paint);

		paint.setColor(oldColor);
		paint.setAlpha(oldTrans);
	}

	// 缩放相关
	float scale_x = 1, scale_y = 1;

	public void getXY(float x, float y) {
		scale_x = x;
		scale_y = y;
	}

	protected static final float tTrans[][] = { {// TRANS_NONE 0
			1, 0, 0, 0, 1, 0, 0, 0, 1 }, {// TRANS_MIRROR_ROT180 1
			1, 0, 0, 0, -1, 0, 0, 0, 1 }, {// TRANS_MIRROR 2
			-1, 0, 0, 0, 1, 0, 0, 0, 1 }, {// TRANS_ROT180 3
			-1, 0, 0, 0, -1, 0, 0, 0, 1 }, {// TRANS_MIRROR_ROT270 4
			0, 1, 0, 1, 0, 0, 0, 0, 1 }, {// TRANS_ROT90 5
			0, -1, 0, 1, 0, 0, 0, 0, 1 }, {// TRANS_ROT270 6
			0, 1, 0, -1, 0, 0, 0, 0, 1 }, {// TRANS_MIRROR_ROT90 7
			0, -1, 0, -1, 0, 0, 0, 0, 1 }

	};

	private Matrix tmpMatrix = new Matrix();

	/* drawRGB */
	public void drawRGB(int[] rgbData, int offset, int scanlength, int x,
			int y, int width, int height, boolean processAlpha) {
		if (rgbData == null) {
			throw new NullPointerException();
		}
		if (width <= 0 || height <= 0) {
			return;
		}

		int l = rgbData.length;
		if (width < 0
				|| height < 0
				|| offset < 0
				|| offset >= l
				|| (scanlength < 0 && scanlength * (height - 1) < 0)
				|| (scanlength >= 0 && scanlength * (height - 1) + width - 1 >= l)) {
			throw new ArrayIndexOutOfBoundsException();
		}

		int rgb1 = rgbData[0];
		int rgb2 = rgbData[l / 2];
		int rgb3 = rgbData[l / 3];
		int rgb4 = rgbData[l / 4];
		int rgb5 = rgbData[l * 5 / 8];
		int rgb6 = rgbData[l * 4 / 5];
		if (rgb1 == rgb2 && rgb2 == rgb3 && rgb3 == rgb4 && rgb4 == rgb5
				&& rgb5 == rgb6) {
			paint.setColor(rgb1);
			fillRect(x, y, width, height);
		} else {

			if (scanlength < width) {
				scanlength = width;
			}
			paint.setStyle(Style.STROKE);
			canvas.drawBitmap(rgbData, offset, scanlength, x, y, width, height,
					processAlpha, paint);
			paint.setStyle(Style.FILL);
		}
	}

	/* drawRoundRect */
	public void drawRoundRect(int x, int y, int width, int height,
			int arcWidth, int arcHeight) {
		paint.setStyle(Style.STROKE);

		RectF rectF = new RectF(x, y, x + width, y + height);
		canvas.drawRoundRect(rectF, (float) arcWidth, (float) arcHeight, paint);
		paint.setStyle(Style.FILL);
	}

	/* 绘制子字符串 */
	public void drawSubstring(String str, int offset, int len, int x, int y,
			int anchor) {
		if (anchor == 0) {
			anchor = TOP | LEFT;
		}

		if ((anchor & TOP) != 0) {
			y -= paint.getFontMetricsInt().top;
		} else if ((anchor & BOTTOM) != 0) {
			y -= paint.getFontMetricsInt().bottom;
		} else if ((anchor & VCENTER) != 0) {
			y += ((paint.getFontMetricsInt().descent - paint
					.getFontMetricsInt().ascent) / 2 - paint
					.getFontMetricsInt().descent);
		}

		if ((anchor & HCENTER) != 0) {
			paint.setTextAlign(Align.CENTER);
		} else if ((anchor & RIGHT) != 0) {
			paint.setTextAlign(Align.RIGHT);
		} else if ((anchor & LEFT) != 0) {
			paint.setTextAlign(Align.LEFT);
		}

		paint.setAntiAlias(true);
		canvas.drawText(str, offset, len + offset, x, y, paint);
	}

	/* fillArc */
	public void fillArc(int x, int y, int width, int height, int startAngle,
			int arcAngle) {
		canvas.drawArc(new RectF(x, y, x + width, y + height), -startAngle,
				-arcAngle, true, paint);
	}

	/* fillRect */
	public void fillRect(int x, int y, int width, int height) {

		canvas.drawRect(x, y, x + width, y + height, paint);
	}

	/* fillRoundRect */
	public void fillRoundRect(int x, int y, int width, int height,
			int arcWidth, int arcHeight) {

		canvas.drawRoundRect(new RectF(x, y, x + width, y + height), arcWidth,
				arcHeight, paint);
	}

	/* fillTriangle */
	public void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3) {

		Path path = new Path();
		path.moveTo(x1, y1);
		path.lineTo(x2, y2);
		path.lineTo(x3, y3);
		path.lineTo(x1, y1);

		canvas.drawPath(path, paint);
	}

	/* translate */
	public void translate(int x, int y) {
		canvas.translate(x, y);
		translateX += x;
		translateY += y;
		clipx -= x;
		clipy -= y;
	}

	/* getBlueComponent */
	public int getBlueComponent() {
		return paint.getColor() & 255;
	}

	/* getGreenComponent */
	public int getGreenComponent() {
		return (paint.getColor() >> 8) & 255;
	}

	/* getRedComponent */
	public int getRedComponent() {
		return (paint.getColor() >> 16) & 255;
	}

	/* getClipHeight */
	public int getClipHeight() {
		return cliph;
	}

	/* getClipWidth */
	public int getClipWidth() {
		return clipw;
	}

	/* getClipX */
	public int getClipX() {
		return clipx;
	}

	/* getClipY */
	public int getClipY() {
		return clipy;
	}

	/* getColor */
	public int getColor() {
		return paint.getColor();
	}

	/* getDisplayColor */
	public int getDisplayColor(int color) {
		return color;
	}

	/* getFont */
	// public Font getFont(){
	// return font;
	// }

	/* getGrayScale */
	public int getGrayScale() {
		return (getRedComponent() + getGreenComponent() + getBlueComponent()) / 3;
	}

	/* getStrokeStyle */
	public int getStrokeStyle() {
		return strokeStyle;
	}

	/* setStrokeStyle */
	public void setStrokeStyle(int style) {
		if (style != SOLID || style != DOTTED) {
			throw new IllegalArgumentException();
		}
		this.strokeStyle = style;

		if (style == SOLID)
			paint.setPathEffect(null);
		else if (style == DOTTED) {
			paint.setPathEffect(dashPathEffect);
		}
	}

	/* getTranslateX */
	public int getTranslateX() {
		return translateX;
	}

	/* getTranslateY */
	public int getTranslateY() {
		return translateY;
	}

	/* setClip */
	public void setClip(int x, int y, int width, int height) {
		clipx = x;
		clipy = y;
		clipw = width;
		cliph = height;

		if (width < 0 || height < 0) {
			return;
		}
		canvas.clipRect(x, y, x + width, y + height, Region.Op.REPLACE);
	}

	/* 设置颜色 */
	public void setColor(int RGB) {
		if ((RGB & 0xff000000) != 0) { // 有alpha值的颜色
			paint.setColor(RGB);
		} else {
			paint.setColor(0xff000000 | RGB);
		}
	}

	/* 设置颜色 */
	public void setColor(int red, int green, int blue) {
		setColor(Color.rgb(red, green, blue));
	}

	/* setGrayScale */
	public void setGrayScale(int grey) {
		if (grey < 0 || grey > 255) {
			throw new IllegalArgumentException();
		}
		setColor(grey, grey, grey);
	}

	/* drawLine */
	public void drawLine(int x1, int y1, int x2, int y2) {
		canvas.drawLine(x1, y1, x2, y2, paint);
	}

	/**
	 * 在绘制开始之前自动重置painter
	 * 
	 * @param flag
	 */
	public void painterAutoReset(boolean flag) {
		isResetPainter = flag;
	}

	public boolean isAutoResetPainter() {
		return isResetPainter;
	}

	/**
	 * 默认的不重置
	 */
	public void painterReset() {
		paint.reset();
	}

	/* getCanvas */
	public android.graphics.Canvas getCanvas() throws NullPointerException {
		if (canvas == null)
			throw new NullPointerException();
		return canvas;
	}

	private FontMetrics fontMetrics;

	/* 获取单个字符宽度 */
	char[] cHarr = new char[1];

	public int charWidth(char ch) {
		cHarr[0] = ch;
		return (int) paint.measureText(cHarr, 0, 1);
	}

	/* 获取字符串宽度 */
	public int stringWidth(String str) {
		return (int) paint.measureText(str);
	}

	/* 获取高度 */
	public float getHeight() {
		paint.getFontMetrics(fontMetrics);
		return fontMetrics.bottom - fontMetrics.top;
	}

	public void setTextSize(float textSize) {
		paint.setTextSize(textSize);
	}

	public void setAlpha(int alpha) {
		paint.setAlpha(alpha);
	}

	// new
	// 绘制图片区域
	public void drawRegionAndroid(Bitmap bitmap, int x_src, int y_src,
			int width, int height, int x_dst, int y_dst, float scale,
			int scaleX, int scaleY, float rotate, int rotateX, int rotateY,
			int trans) {

		tmpMatrix.reset();

		int dW = width, dH = height;
		int x_offset = 0, y_offset = 0;

		switch (trans) {
		case TRANS_NONE: {
			x_offset = -x_src;
			y_offset = -y_src;
			break;
		}
		case TRANS_ROT90: {
			tmpMatrix.preRotate(90);
			dW = height;
			dH = width;
			x_offset = dW + y_src;
			y_offset = -x_src;
			break;
		}
		case TRANS_ROT180: {
			tmpMatrix.preRotate(180);
			x_offset = dW + x_src;
			y_offset = dH + y_src;
			break;
		}
		case UserData.TRANS_ROT270: {
			tmpMatrix.preRotate(270);
			dW = height;
			dH = width;
			x_offset = -y_src;
			y_offset = x_src;
			dH = -dH;
			break;
		}
		case UserData.TRANS_MIRROR: {
			tmpMatrix.preScale(-1, 1);
			x_offset = dW + x_src;
			y_offset = -y_src;
			break;
		}
		case UserData.TRANS_MIRROR_ROT90: {
			tmpMatrix.preScale(-1, 1);
			tmpMatrix.preRotate(-90);
			dW = height;
			dH = width;
			x_offset = dW + y_src;
			y_offset = dH + x_src;
			break;
		}
		case UserData.TRANS_MIRROR_ROT180: {
			tmpMatrix.preScale(-1, 1);
			tmpMatrix.preRotate(-180);
			x_offset = -x_src;
			y_offset = dH + y_src;
			break;
		}
		case UserData.TRANS_MIRROR_ROT270: {
			tmpMatrix.preScale(-1, 1);
			tmpMatrix.preRotate(-270);
			dW = height;
			dH = width;
			x_offset = -y_src;
			y_offset = -x_src;
			break;
		}
		default:
			throw new IllegalArgumentException("Bad transform");

		}

		// tmpMatrix.postTranslate(x_offset, y_offset);

		canvas.save();
		canvas.scale(scale, scale, scaleX, scaleY);
		canvas.rotate(rotate, rotateX, rotateY);
		canvas.clipRect(x_dst, y_dst, x_dst + dW, y_dst + dH);
		canvas.translate(x_dst + x_offset, y_dst + y_offset);
		canvas.drawBitmap(bitmap, tmpMatrix, paint);
		canvas.restore();
	}

	// new
	// 有锚点的绘图,可旋转缩放
	public void drawBitmapAndroid(Bitmap bitmap, int xPoint, int yPoint,
			float scale1, float scale2, int scaleX, int scaleY, float rotate,
			int rotateX, int rotateY, int anchor, float scale, byte scaleType) {

		if ((anchor & RIGHT) != 0) {
			xPoint = xPoint - bitmap.getWidth();
		} else if ((anchor & HCENTER) != 0) {
			xPoint = xPoint - bitmap.getWidth() / 2;
		}
		if ((anchor & BOTTOM) != 0) {
			yPoint = yPoint - bitmap.getHeight();
		} else if ((anchor & VCENTER) != 0) {
			yPoint = yPoint - bitmap.getHeight() / 2;
		}

		// 保存状态
		tmpMatrix.reset();
		// matrix.postTranslate(translateX, translateY);
		tmpMatrix.postTranslate(xPoint, yPoint);
		tmpMatrix.postScale(scale1, scale2, scaleX, scaleY);
		tmpMatrix.postRotate(rotate, rotateX, rotateY);

		if (scale != 1) {
			switch (scaleType) {
			case 0:
				canvas.save();
				canvas.clipRect(xPoint + bitmap.getWidth() * (1 - scale),
						yPoint, xPoint + bitmap.getWidth(),
						yPoint + bitmap.getHeight());
				break;
			case 1:
				canvas.save();
				canvas.clipRect(xPoint, yPoint + bitmap.getHeight()
						* (1 - scale), xPoint + bitmap.getWidth(), yPoint
						+ bitmap.getHeight());
				break;

			default:
				break;
			}
		}

		// 显示
		canvas.drawBitmap(bitmap, tmpMatrix, paint);

		if (scale != 1) {
			canvas.restore();
		}
	}

	// new
	// 有锚点的绘图,可旋转缩放，简单接口
	public void drawBitmapAndroid(Bitmap bitmap, int xPoint, int yPoint,
			int anchor) {
		drawBitmapAndroid(bitmap, xPoint, yPoint, 1, 1, xPoint, yPoint, 0,
				xPoint, yPoint, anchor, 1, (byte) 0);
	}

	// 显示按钮
	public void drawBitmapAndroid(Bitmap bitmap, int xPoint, int yPoint,
			float scale, int anchor) {
		drawBitmapAndroid(bitmap, xPoint, yPoint, scale, scale, xPoint, yPoint,
				0, xPoint, yPoint, anchor, 1, (byte) 0);
	}

	// new
	// 有锚点的绘图,可旋转缩放，简单接口
	public void drawBitmapAndroid(Bitmap bitmap, int xPoint, int yPoint,
			int anchor, float scale, byte scaleType) {
		drawBitmapAndroid(bitmap, xPoint, yPoint, 1, 1, xPoint, yPoint, 0,
				xPoint, yPoint, anchor, scale, scaleType);
	}

	// 显示按钮用，可做进度条,可缩放
	public void drawBitmapAndroid(Bitmap bitmap, int xPoint, int yPoint,
			float sclaeButton, int anchor, float scale, byte scaleType) {
		drawBitmapAndroid(bitmap, xPoint, yPoint, sclaeButton, sclaeButton,
				xPoint, yPoint, 0, xPoint, yPoint, anchor, scale, scaleType);
	}

	// new
	public void drawBitmapAndroidInCamera(Camera c, Bitmap bitmap, int xPoint,
			int yPoint, int anchor) {
		drawBitmapAndroidInCamera(c, bitmap, xPoint, yPoint, 1, 0, 0, 0, 0, 0,
				anchor);
	}

	public void drawBitmapAndroidInCamera(Camera c, Bitmap bitmap, int xPoint,
			int yPoint, float scale, int anchor) {
		drawBitmapAndroidInCamera(c, bitmap, xPoint, yPoint, scale, 0, 0, 0, 0,
				0, anchor);
	}

	public int getAlpha() {
		return paint.getAlpha();
	}

	// new
	public void drawBitmapAndroidInCamera(Camera c, Bitmap bitmap, int xPoint,
			int yPoint, int anchor, int alpha) {
		int oldAlpha = getAlpha();
		setAlpha(alpha);
		drawBitmapAndroidInCamera(c, bitmap, xPoint, yPoint, 1, 0, 0, 0, 0, 0,
				anchor);
		setAlpha(oldAlpha);
	}

	// new
	public void drawBitmapAndroidInCamera(Camera c, Bitmap image, int xPoint,
			int yPoint, float scale1, float scale2, int scaleX, int scaleY,
			float rotate, int rotateX, int rotateY, int anchor) {

		int xTemp = xPoint;
		int yTemp = yPoint;
		if ((anchor & RIGHT) != 0) {
			xPoint = xPoint - image.getWidth();
		} else if ((anchor & HCENTER) != 0) {
			xPoint = xPoint - image.getWidth() / 2;
		}
		if ((anchor & BOTTOM) != 0) {
			yPoint = yPoint - image.getHeight();
		} else if ((anchor & VCENTER) != 0) {
			yPoint = yPoint - image.getHeight() / 2;
		}

		boolean drawAble = false;
		if (Util.crashAble(xPoint, yPoint, xPoint + image.getWidth(), yPoint
				+ image.getHeight(), c.CameraX, c.CameraY, c.CameraX
				+ c.CameraW, c.CameraY + c.CameraH)) {
			drawAble = true;
		}

		xPoint = xTemp;
		yPoint = yTemp;

		if (drawAble) {
			drawBitmapAndroid(image, c.getX(xPoint), c.getY(yPoint), scale1,
					scale2, c.getX(xPoint + scaleX), c.getY(yPoint + scaleY),
					rotate, c.getX(xPoint + rotateX), c.getY(yPoint + rotateY),
					anchor, 1, (byte) 0);
		}
	}

	// new
	public void drawBitmapAndroidInCamera(Camera c, Bitmap image, int xPoint,
			int yPoint, float scale, int scaleX, int scaleY, float rotate,
			int rotateX, int rotateY, int anchor) {
		drawBitmapAndroidInCamera(c, image, xPoint, yPoint, scale, scale,
				scaleX, scaleY, rotate, rotateX, rotateY, anchor);
	}

	// new
	public void drawBitmapAndroidInCameraAlpha(Camera c, Bitmap image,
			int xPoint, int yPoint, float scale, int scaleX, int scaleY,
			float rotate, int rotateX, int rotateY, int anchor, int alpha) {
		int oldAlpha = getAlpha();
		setAlpha(alpha);
		drawBitmapAndroidInCamera(c, image, xPoint, yPoint, scale, scale,
				scaleX, scaleY, rotate, rotateX, rotateY, anchor);
		setAlpha(oldAlpha);
	}

	// new
	public void drawRegionAndroidInCamera(Camera c, Bitmap image, int x, int y,
			int x_src, int y_src, int width, int height, int x_dst, int y_dst,
			float scale, int scaleX, int scaleY, int rotate, int rotateX,
			int rotateY, int trans) {

		boolean drawAble = false;
		byte anchor = 0;
		if (trans < 4) {
			if (Util.crashAble(x_dst, y_dst, x_dst + width, y_dst + height,
					c.CameraX, c.CameraY, c.CameraX + c.CameraW, c.CameraY
							+ c.CameraH)) {
				drawAble = true;
			}
		} else if (trans == TRANS_ROT90 && anchor == 10) {
			if (Util.crashAble(x_dst - width, y_dst - (height >> 1), x_dst,
					y_dst + (height >> 1), c.CameraX, c.CameraY, c.CameraX
							+ c.CameraW, c.CameraY + c.CameraH)) {
				drawAble = true;
			}
		} else {
			if (Util.crashAble(x_dst, y_dst - width, x_dst + height, y_dst
					+ width, c.CameraX, c.CameraY, c.CameraX + c.CameraW,
					c.CameraY + c.CameraH)) {
				drawAble = true;
			}
		}
		if (drawAble) {
			drawRegionAndroid(image, x_src, y_src, width, height,
					c.getX(x_dst), c.getY(y_dst), scale, c.getX(x + scaleX),
					c.getY(y + scaleY), rotate, c.getX(x + rotateX),
					c.getY(y + rotateY), trans);
		}
	}

	// old
	/* 绘制图片 */
	public void drawImage(Bitmap img, int x, int y, int anchor) {
		if ((anchor & Graphics.RIGHT) != 0) {
			x = x - img.getWidth();
		} else if ((anchor & Graphics.HCENTER) != 0) {
			x = x - img.getWidth() / 2;
		}
		if ((anchor & Graphics.BOTTOM) != 0) {
			y = y - img.getHeight();
		} else if ((anchor & Graphics.VCENTER) != 0) {
			y = y - img.getHeight() / 2;
		}

		canvas.drawBitmap(img, x, y, paint);
	}

	// old
	public void drawRegion(Bitmap img, int x_src, int y_src, int width,
			int height, int transform, int x_dst, int y_dst, int anchor) {
		if (width <= 0 || height <= 0)
			return;

		if (img == null)
			throw new NullPointerException();
		if (x_src < 0 || y_src < 0)
			throw new IllegalArgumentException("Area out of Image: x" + x_src
					+ " y:" + y_src);
		// if (src.isMutable() && src.getGraphics() == this)
		// throw new IllegalArgumentException("Image is source and target");

		// Bitmap img = src.getBitMapInpackage();

		tmpMatrix.reset();
		int dW = width, dH = height;
		int x_offset = 0, y_offset = 0;

		switch (transform) {
		case TRANS_NONE: {
			x_offset = -x_src;
			y_offset = -y_src;
			break;
		}
		case TRANS_ROT90: {
			tmpMatrix.preRotate(90);
			dW = height;
			dH = width;
			x_offset = dW + y_src;
			y_offset = -x_src;
			break;
		}
		case TRANS_ROT180: {
			tmpMatrix.preRotate(180);
			x_offset = dW + x_src;
			y_offset = dH + y_src;
			break;
		}
		case UserData.TRANS_ROT270: {
			tmpMatrix.preRotate(270);
			dW = height;
			dH = width;
			x_offset = -y_src;
			y_offset = dH + x_src;
			break;
		}
		case UserData.TRANS_MIRROR: {
			tmpMatrix.preScale(-1, 1);
			x_offset = dW + x_src;
			y_offset = -y_src;
			break;
		}
		case UserData.TRANS_MIRROR_ROT90: {
			tmpMatrix.preScale(-1, 1);
			tmpMatrix.preRotate(-90);
			dW = height;
			dH = width;
			x_offset = dW + y_src;
			y_offset = dH + x_src;
			break;
		}
		case UserData.TRANS_MIRROR_ROT180: {
			tmpMatrix.preScale(-1, 1);
			tmpMatrix.preRotate(-180);
			x_offset = -x_src;
			y_offset = dH + y_src;
			break;
		}
		case UserData.TRANS_MIRROR_ROT270: {
			tmpMatrix.preScale(-1, 1);
			tmpMatrix.preRotate(-270);
			dW = height;
			dH = width;
			x_offset = -y_src;
			y_offset = -x_src;
			break;
		}
		default:
			throw new IllegalArgumentException("Bad transform");

		}
		boolean badAnchor = false;

		if (anchor == 0) {
			anchor = TOP | LEFT;
		}

		if ((anchor & BASELINE) != 0)
			badAnchor = true;

		if ((anchor & TOP) != 0) {
			if ((anchor & (VCENTER | BOTTOM)) != 0)
				badAnchor = true;
		} else if ((anchor & BOTTOM) != 0) {
			if ((anchor & VCENTER) != 0)
				badAnchor = true;
			else {
				y_dst -= dH;
			}
		} else if ((anchor & VCENTER) != 0) {
			y_dst -= (dH - 1) >>> 1;
		} else {
			// no vertical anchor
			badAnchor = true;
		}

		// horizontal
		if ((anchor & LEFT) != 0) {
			if ((anchor & (HCENTER | RIGHT)) != 0)
				badAnchor = true;
		} else if ((anchor & RIGHT) != 0) {
			if ((anchor & HCENTER) != 0)
				badAnchor = true;
			else {
				x_dst -= dW;
			}
		} else if ((anchor & HCENTER) != 0) {
			x_dst -= (dW - 1) >>> 1;
		} else {
			// no horizontal anchor
			badAnchor = true;
		}

		if (badAnchor) {
			throw new IllegalArgumentException("Bad Anchor");
		}

		canvas.save();
		canvas.clipRect(x_dst, y_dst, x_dst + dW, y_dst + dH, Op.INTERSECT);
		canvas.translate(x_dst + x_offset, y_dst + y_offset);
		canvas.drawBitmap(img, tmpMatrix, null);
		canvas.restore();
	}

	public void save() {
		canvas.save();
	}

	public void restore() {
		canvas.restore();
	}

	public void setScale(float scale, float x, float y) {
		canvas.scale(scale, scale, x, y);
	}

	// old
	public void drawImage(Bitmap image, int x, int y, int trans, int anchor) {
		if (trans == TRANS_NONE) {
			drawImage(image, x, y, anchor);
		} else {
			drawRegion(image, 0, 0, image.getWidth(), image.getHeight(), trans,
					x, y, anchor);
		}
	}

	// old
	public void drawRegionInCamera(Camera c, Bitmap image, int x, int y, int w,
			int h, int trans, int PX, int PY, int anchor) {
		boolean drawAble = false;
		if (trans < 4) {
			if (Util.crashAble(PX, PY, PX + w, PY + h, c.CameraX, c.CameraY,
					c.CameraX + c.CameraW, c.CameraY + c.CameraH)) {
				drawAble = true;
			}
		} else if (trans == TRANS_ROT90 && anchor == 10) {
			if (Util.crashAble(PX - w, y - (h >> 1), PX, PY + (h >> 1),
					c.CameraX, c.CameraY, c.CameraX + c.CameraW, c.CameraY
							+ c.CameraH)) {
				drawAble = true;
			}
		} else {
			if (Util.crashAble(PX, PY, PX + h, PY + w, c.CameraX, c.CameraY,
					c.CameraX + c.CameraW, c.CameraY + c.CameraH)) {
				drawAble = true;
			}
		}
		if (drawAble) {
			drawRegion(image, x, y, w, h, trans, c.getX(PX), c.getY(PY), anchor);
		}
	}

	// old
	public void drawImageInCamera(Camera c, Bitmap image, int x, int y,
			int trans, int anchor) {
		boolean drawAble = false;
		if (trans < 4) {
			if (Util.crashAble(x, y, x + image.getWidth(),
					y + image.getHeight(), c.CameraX, c.CameraY, c.CameraX
							+ c.CameraW, c.CameraY + c.CameraH)) {
				drawAble = true;
			}
		} else if (trans == TRANS_ROT90 && anchor == 10) {
			if (Util.crashAble(x - image.getWidth(), y
					- (image.getHeight() >> 1), x,
					y + (image.getHeight() >> 1), c.CameraX, c.CameraY,
					c.CameraX + c.CameraW, c.CameraY + c.CameraH)) {
				drawAble = true;
			}
		} else {
			if (Util.crashAble(x, y, x + image.getHeight(),
					y + image.getWidth(), c.CameraX, c.CameraY, c.CameraX
							+ c.CameraW, c.CameraY + c.CameraH)) {
				drawAble = true;
			}
		}
		if (drawAble) {
			drawImage(image, c.getX(x), c.getY(y), trans, anchor);
		}
	}

	// 显示文字
	public void drawText(String stringTemp, int x, int y, int colorShadow,
			int color, int size, int anchor) {
		drawText(stringTemp, x, y, colorShadow, color, size, 255, anchor);
	}

	// 显示文字
	public void drawText(String stringTemp, int x, int y, int colorShadow,
			int color, int size, int transparency, int anchor) {
		int oldColor = paint.getColor();
		float oldSize = paint.getTextSize();
		int oldTrans = paint.getAlpha();

		paint.setTextSize(size);
		paint.setAlpha(transparency);

		paint.setColor(colorShadow);
		drawString(stringTemp, x + 1, y + 1, anchor);
		paint.setColor(color);
		drawString(stringTemp, x, y, anchor);

		// 恢复
		paint.setColor(oldColor);
		paint.setTextSize(oldSize);
		paint.setAlpha(oldTrans);
	}

	public void drawDialogStr(String str, int index, int x, int y, int width,
			int color1, int color2, int color3, boolean isCenter) {// color1为#颜色
																	// color2为&颜色
		// color3为底色
		StringBuffer sb = new StringBuffer(str);
		int w0 = 0, h0 = 0, endIndex = 0;// 位移 、当前行数、下一个索引
		setColor(color1);
		if (index != 0) {
			for (int j = index; j > 0; j--) {
				char tempS1 = sb.charAt(j);
				if (tempS1 == '#') {
					setColor(color1);
					break;
				} else if (tempS1 == '&') {
					setColor(color2);
					break;
				}
			}
		}

		// 计算整个文件块有多宽
		for (int i = index; i < sb.length(); i++) {
			char tempS = sb.charAt(i);
			if (tempS == '#') {
				continue;
			} else if (tempS == '&') {
				continue;
			} else if (tempS == '|') {
				w0 = 0;
				h0++;
				continue;
			}

			if (i < sb.length() - 1) {
				w0 += charWidth(tempS);
				char tempS2 = sb.charAt(i + 1);
				int tempWidth = 0;
				if (tempS2 == '&' || tempS2 == '#') {
					if (i + 2 >= sb.length()) {
						tempWidth = charWidth(sb.charAt(i + 1));
					} else {
						tempWidth = charWidth(sb.charAt(i + 2));
					}
				} else if (tempS2 != '|') {
					tempWidth = charWidth(tempS2);
				}
				if (w0 + tempWidth > width) {
					w0 = 0;
					h0++;
				}
			}
		}

		// 居中显示
		if (isCenter) {
			// 如果不足一行
			if (h0 == 0) {
				x -= w0 / 2;
			} else {
				x -= width / 2;
			}

			y -= (h0 + 1) * ((int) getHeight() + 4) / 2;
		}

		w0 = 0;
		h0 = 0;

		for (int i = index; i < sb.length(); i++) {
			char tempS = sb.charAt(i);
			if (tempS == '#') {
				setColor(color1);
				continue;
			} else if (tempS == '&') {
				setColor(color2);
				continue;
			} else if (tempS == '|') {
				w0 = 0;
				h0++;
				// if (h0 >= 2) {
				// endIndex = i + 1;
				// break;
				// }
				continue;
			}
			int oldColor = getColor();
			setColor(color3);
			drawChar(tempS, x + w0, y + h0 * ((int) getHeight() + 4) + 1, 20);
			setColor(oldColor);
			drawChar(tempS, x + w0, y + h0 * ((int) getHeight() + 4), 20);

			if (i < sb.length() - 1) {
				w0 += charWidth(tempS);
				char tempS2 = sb.charAt(i + 1);
				int tempWidth = 0;
				if (tempS2 == '&' || tempS2 == '#') {
					if (i + 2 >= sb.length()) {
						tempWidth = charWidth(sb.charAt(i + 1));
					} else {
						tempWidth = charWidth(sb.charAt(i + 2));
					}
				} else if (tempS2 != '|') {
					tempWidth = charWidth(tempS2);
				}
				if (w0 + tempWidth > width) {
					w0 = 0;
					h0++;
					// if (h0 >= 2) {
					// endIndex = i + 1;
					//
					// break;
					// }
				}
			}
		}
	}

	public void drawBitmapBone(Bitmap bitmap, float xPoint, float yPoint,
			float scaleX, float scaleY, float skewX, float skewY,
			float anchorX, float anchorY) {		
		tmpMatrix.reset();
		        				
		tmpMatrix.postTranslate(xPoint - anchorX * bitmap.getWidth(), yPoint - anchorY * bitmap.getHeight());
						
		tmpMatrix.postScale(scaleX, scaleY, xPoint, yPoint);
		
		mySkewTranslation(tmpMatrix, skewX, skewY, xPoint, yPoint);
        	
		// 显示
		canvas.drawBitmap(bitmap, tmpMatrix, paint);
	}
	
	public void drawBitmapBone(Bitmap bitmap, Matrix matrix) {
		canvas.drawBitmap(bitmap, matrix, paint);
	}
	
	public static Matrix matrix2 = new Matrix();
	
	public static void mySkewTranslation(Matrix matrix, float rotationX, float ratationY, float xPoint, float yPoint)
	{
		matrix2.reset();
		float value[] = {(float) Math.cos(rotationX),(float) -Math.sin(ratationY),0,
				(float) Math.sin(rotationX),(float) Math.cos(ratationY),0,
				0,0,1};
		matrix2.setValues(value);
		matrix.postTranslate(-xPoint, -yPoint);
		matrix.postConcat(matrix2);
		matrix.postTranslate(xPoint, yPoint);		
	}
}
