package com.dsy.control;

import java.lang.reflect.Array;
import java.util.Arrays;

import com.dsy.ActionBoneParse.UserData;
import com.dsy.ActionBoneParse.util.Graphics;
import com.dsy.ActionBoneParse.util.PengScan;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.MotionEvent;

public class TouchEvent {
	// 抬起按下时

	// 系统层
	private static boolean[] isTouchDown = {false, false}; // 按下
	private static boolean[] isTouchUp = {false, false}; // 抬起
	private static boolean[] isTouchMove = {false, false}; // 移动
	private static boolean[] isTouchHold = {false, false}; // 按住

	// 记录抬起落下
	private static int[] mouse_x_last = {0,0};
	private static int[] mouse_y_last = {0,0};
	private static int[] mouse_x_now = {0,0};
	private static int[] mouse_y_now = {0,0};

	// 记录经过的点
	private static int[] mouse_x_move1 = {0,0};
	private static int[] mouse_y_move1 = {0,0};
	private static int[] mouse_x_move2 = {0,0};
	private static int[] mouse_y_move2 = {0,0};

	// 逻辑层
	private static boolean[] logic_isTouchDown = {false, false}; // 按下
	private static boolean[] logic_isTouchUp = {false, false}; // 抬起
	private static boolean[] logic_isTouchMove = {false, false}; // 移动
	private static boolean[] logic_isTouchHold = {false, false}; // 按住

	private static int[] logic_mouse_x_last = {0,0};
	private static int[] logic_mouse_y_last = {0,0};
	private static int[] logic_mouse_x_now = {0,0};
	private static int[] logic_mouse_y_now = {0,0};

	private static int[] logic_mouse_x_move1 = {0,0};
	private static int[] logic_mouse_y_move1 = {0,0};
	private static int[] logic_mouse_x_move2 = {0,0};
	private static int[] logic_mouse_y_move2 = {0,0};

	private static double scaleTouchX;
	private static double scaleTouchY;

	// 设置缩放比例
	public static void init(int w, int h) {
		scaleTouchX = (double) UserData.screan_width / w;
		scaleTouchY = (double) UserData.screan_height / h;

		Arrays.fill(isTouchDown, false);
		Arrays.fill(isTouchUp, false);
		Arrays.fill(isTouchMove, false);
		Arrays.fill(isTouchHold, false);
	}
	
	public static int setScaleX(int x) {
		return (int) (x * scaleTouchX);
	}
	
	public static int setScaleY(int y) {
		return (int) (y * scaleTouchY);
	}

	public static boolean touch(MotionEvent event) {
		int pointerCount = event.getPointerCount(); 
		int pointerId = 0; 
		int action = (event.getAction()&MotionEvent.ACTION_MASK) % 5;//统一单点和多点 
		pointerId = (event.getAction()&MotionEvent.ACTION_POINTER_ID_MASK) >>> MotionEvent.ACTION_POINTER_ID_SHIFT;

		switch (action) {
		case MotionEvent.ACTION_MOVE:
			if (pointerCount == 1) {
				actionMoveLogic((int)event.getX(), (int)event.getY(), pointerId);
			}
			break;
		case MotionEvent.ACTION_DOWN:
			actionDownLogic((int)event.getX(pointerId), (int)event.getY(pointerId), pointerId);
			break;
		case MotionEvent.ACTION_UP:
			// 抬起时需做判断，一个点要判断哪个点需要抬起
			if (pointerCount == 1) {
				if (isTouchHold[0]) {
					actionUpLogic((int)event.getX(), (int)event.getY(), 0);
				} else if (isTouchHold[1]) {
					actionUpLogic((int)event.getX(), (int)event.getY(), 1);
				}
			} else {
				actionUpLogic((int)event.getX(pointerId), (int)event.getY(pointerId), pointerId);
			}
			break;

		default:
			break;
		}
		return true;
	}
	
	public static void actionMoveLogic(int x, int y, int pointerId) {
		x = setScaleX(x);
		y = setScaleY(y);
		
		mouse_x_move1[pointerId] = x;
		mouse_y_move1[pointerId] = y;
		isTouchMove[pointerId] = true;
	}
	
	public static void actionDownLogic(int x, int y, int pointerId) {
		x = setScaleX(x);
		y = setScaleY(y);
		
		mouse_x_last[pointerId] = x;
		mouse_y_last[pointerId] = y;
		mouse_x_now[pointerId] = x;
		mouse_y_now[pointerId] = y;

		mouse_x_move2[pointerId] = x;
		mouse_y_move2[pointerId] = y;
		mouse_x_move1[pointerId] = x;
		mouse_y_move1[pointerId] = y;

		isTouchDown[pointerId] = true;
		isTouchHold[pointerId] = true;
	}
	
	public static void actionUpLogic(int x, int y, int pointerId) {
		x = setScaleX(x);
		y = setScaleY(y);
		
		mouse_x_now[pointerId] = x;
		mouse_y_now[pointerId] = y;

		isTouchUp[pointerId] = true;
		isTouchHold[pointerId] = false;
	}

	public static boolean getIsTouchDown(int[] nowLastPointArray) {
		// 从第一个开始检测
		for (byte i = 0;i<2;i++) {
			if (logic_isTouchDown[i]) {
				getNowLastPointArray(nowLastPointArray, i);
				return true;
			}
		}		
		return false;
	}

	public static boolean getIsTouchDown() {
		return logic_isTouchDown[0] || logic_isTouchDown[1];
	}

	public static boolean getIsTouchUp(int[] nowLastPointArray) {
		
		// 从第一个开始检测
		for (byte i = 0;i<2;i++) {
			if (logic_isTouchUp[i]) {
				getNowLastPointArray(nowLastPointArray, i);
				return true;
			}
		}		
		return false;
	}

	public static boolean getIsTouchUpInRectAnchor(int x, int y, Bitmap image,
			byte anchor, int trim) {

		if ((anchor & Graphics.RIGHT) != 0) {
			x = x - image.getWidth();
		} else if ((anchor & Graphics.HCENTER) != 0) {
			x = x - image.getWidth() / 2;
		}
		if ((anchor & Graphics.BOTTOM) != 0) {
			y = y - image.getHeight();
		} else if ((anchor & Graphics.VCENTER) != 0) {
			y = y - image.getHeight() / 2;
		}

		tempRect[0] = x - trim;
		tempRect[1] = y - trim;
		tempRect[2] = image.getWidth() + x + trim;
		tempRect[3] = image.getHeight() + y + trim;

		for (byte i = 0;i<2;i++) {
			if (PengScan.isInRect(logic_mouse_x_now[i], logic_mouse_y_now[i], tempRect)
					&& logic_isTouchUp[i]) {
				return true;
			}
		}

		return false;
	}

	public static boolean getIsTouchDownInRectAnchor(int x, int y,
			Bitmap image, byte anchor, int trim) {

		if ((anchor & Graphics.RIGHT) != 0) {
			x = x - image.getWidth();
		} else if ((anchor & Graphics.HCENTER) != 0) {
			x = x - image.getWidth() / 2;
		}
		if ((anchor & Graphics.BOTTOM) != 0) {
			y = y - image.getHeight();
		} else if ((anchor & Graphics.VCENTER) != 0) {
			y = y - image.getHeight() / 2;
		}

		tempRect[0] = x - trim;
		tempRect[1] = y - trim;
		tempRect[2] = image.getWidth() + x + trim;
		tempRect[3] = image.getHeight() + y + trim;
		
		for (byte i = 0;i<2;i++) {
			if (PengScan.isInRect(logic_mouse_x_last[i], logic_mouse_y_last[i], tempRect)
					&& logic_isTouchDown[i]) {
				return true;
			}
		}
		return false;
	}

	public static boolean getIsTouchMove(int[] nowLastPointArray) {
		// last
		nowLastPointArray[0] = logic_mouse_x_move2[0];
		nowLastPointArray[1] = logic_mouse_y_move2[0];

		// now
		nowLastPointArray[2] = logic_mouse_x_move1[0];
		nowLastPointArray[3] = logic_mouse_y_move1[0];

		return logic_isTouchMove[0];
	}

	// 移动距离
	public static int getIsTouchMove(int index) {
		if (index == 0) {
			return logic_mouse_x_move1[0] - logic_mouse_x_move2[0];
		} else if (index == 1) {
			return logic_mouse_y_move1[0] - logic_mouse_y_move2[0];
		}
		return 0;
	}

	// 获取手指是否按住
//	public static boolean getIsTouchHold(int[] nowLastPointArray) {
//		// last
//		nowLastPointArray[0] = logic_mouse_x_move2;
//		nowLastPointArray[1] = logic_mouse_y_move2;
//
//		// now
//		nowLastPointArray[2] = logic_mouse_x_move1;
//		nowLastPointArray[3] = logic_mouse_y_move1;
//
//		return logic_isTouchHold;
//	}

	// 获取手指是否按住
	public static boolean getIsTouchHold() {
		return logic_isTouchHold[0] || logic_isTouchHold[1];
	}

	// 获取手指是否按住指定区域
	public static boolean getIsTouchHoldInRect(int[] rect) {
		for (byte i = 0;i<2;i++) {
			if (PengScan.isInRect(logic_mouse_x_move2[i], logic_mouse_y_move2[i], rect)
					&& logic_isTouchHold[i]) {
				return true;
			}
		}
		return false;
	}

	private static int[] tempRect = { 0, 0, 0, 0 };

	public static boolean getIsTouchHoldInRect(int x, int y, int[] rect) {

		tempRect[0] = rect[0] + x;
		tempRect[1] = rect[1] + y;
		tempRect[2] = rect[2] + x;
		tempRect[3] = rect[3] + y;
		
		for (byte i = 0;i<2;i++) {
			if (PengScan.isInRect(logic_mouse_x_move2[i], logic_mouse_y_move2[i], tempRect)
					&& logic_isTouchHold[i]) {
				return true;
			}
		}
		return false;
	}

	public static boolean getIsTouchDownInRect(int x, int y, int[] rect) {

		tempRect[0] = rect[0] + x;
		tempRect[1] = rect[1] + y;
		tempRect[2] = rect[2] + x;
		tempRect[3] = rect[3] + y;
		
		for (byte i = 0;i<2;i++) {
			if (PengScan.isInRect(logic_mouse_x_last[i], logic_mouse_y_last[i], tempRect)
					&& logic_isTouchDown[i]) {
				return true;
			}
		}
		return false;
	}

	public static boolean getIsTouchHoldInRect(int x, int y, int a, int b,
			int c, int d) {

		tempRect[0] = a + x;
		tempRect[1] = b + y;
		tempRect[2] = c + x;
		tempRect[3] = d + y;
		
		for (byte i = 0;i<2;i++) {
			if (PengScan.isInRect(logic_mouse_x_move2[i], logic_mouse_y_move2[i], tempRect)
					&& logic_isTouchHold[i]) {
				return true;
			}
		}
		return false;
	}

	public static boolean getIsTouchDownInRect(int x, int y, int a, int b,
			int c, int d) {

		tempRect[0] = a + x;
		tempRect[1] = b + y;
		tempRect[2] = c + x;
		tempRect[3] = d + y;
		
		for (byte i = 0;i<2;i++) {
			if (PengScan.isInRect(logic_mouse_x_last[i], logic_mouse_y_last[i], tempRect)
					&& logic_isTouchDown[i]) {
				return true;
			}
		}
		return false;
	}

	public static boolean getIsTouchHoldInRectAnchor(int x, int y,
			Bitmap image, byte anchor, int trim) {
		if ((anchor & Graphics.RIGHT) != 0) {
			x = x - image.getWidth();
		} else if ((anchor & Graphics.HCENTER) != 0) {
			x = x - image.getWidth() / 2;
		}
		if ((anchor & Graphics.BOTTOM) != 0) {
			y = y - image.getHeight();
		} else if ((anchor & Graphics.VCENTER) != 0) {
			y = y - image.getHeight() / 2;
		}

		tempRect[0] = x - trim;
		tempRect[1] = y - trim;
		tempRect[2] = image.getWidth() + x + trim;
		tempRect[3] = image.getHeight() + y + trim;
		
		for (byte i = 0;i<2;i++) {
			if (PengScan.isInRect(logic_mouse_x_move2[i], logic_mouse_y_move2[i], tempRect)
					&& logic_isTouchHold[i]) {
				return true;
			}
		}
		return false;
	}

	public static void getNowLastPointArray(int[] nowLastPointArray, int pointerId) {
		// last
		nowLastPointArray[0] = logic_mouse_x_last[pointerId];
		nowLastPointArray[1] = logic_mouse_y_last[pointerId];

		// now
		nowLastPointArray[2] = logic_mouse_x_now[pointerId];
		nowLastPointArray[3] = logic_mouse_y_now[pointerId];
	}

	public static void refresh() {
		for (byte i = 0;i<2;i++) {
			// 获取标志
			logic_isTouchDown[i] = isTouchDown[i];
			logic_isTouchMove[i] = isTouchMove[i];
			logic_isTouchUp[i] = isTouchUp[i];
			logic_isTouchHold[i] = isTouchHold[i];

			// 获取坐标
			logic_mouse_x_last[i] = mouse_x_last[i];
			logic_mouse_y_last[i] = mouse_y_last[i];
			logic_mouse_x_now[i] = mouse_x_now[i];
			logic_mouse_y_now[i] = mouse_y_now[i];

			logic_mouse_x_move1[i] = mouse_x_move1[i];
			logic_mouse_y_move1[i] = mouse_y_move1[i];
			logic_mouse_x_move2[i] = mouse_x_move2[i];
			logic_mouse_y_move2[i] = mouse_y_move2[i];

			// 更新触点移动的前一个点
			mouse_x_move2[i] = mouse_x_move1[i];
			mouse_y_move2[i] = mouse_y_move1[i];

			// 清楚标志
			isTouchDown[i] = false;
			isTouchMove[i] = false;
			isTouchUp[i] = false;
		}
	}
}
