package com.dsy.ActionBoneParse.util;

import java.util.Arrays;

import android.graphics.Point;
import android.graphics.Rect;

public class PengScan {

	// 朝向
	public static final int up_direct = 0;
	public static final int down_direct = 1;
	public static final int left_direct = 2;
	public static final int right_direct = 3;
	
	// 判断两个集合是否有交集
	public static boolean isJiao(int i1, int i2, int j1, int j2) {
		int tempMax = 0;
		int tempMin = 0;
		// 求最大
		tempMax = Math.max(i1, i2);
		tempMax = Math.max(tempMax, j1);
		tempMax = Math.max(tempMax, j2);

		// 求最小
		tempMin = Math.min(i1, i2);
		tempMin = Math.min(tempMin, j1);
		tempMin = Math.min(tempMin, j2);

		if (tempMax - tempMin < i2 - i1 + j2 - j1) {
			return true;
		}

		return false;
	}
	
	// 
	public static boolean isPeng(int[] rect1, int[] rect2) {		
		if (rect1[2] < rect2[0]) {
			return false;
		}
		if (rect1[0] > rect2[2]) {
			return false;
		}
		if (rect1[3] < rect2[1]) {
			return false;
		}
		if (rect1[1] > rect2[3]) {
			return false;
		}
		return true;
	}
	
	private static int[] rectTemp = {0,0,0,0};
	private static int[] rectTemp1 = {0,0,0,0};
	public static boolean isPeng(int x, int y, int[] rect1, int[] rect2) {
		
		Arrays.fill(rectTemp, 0);
		rectTemp[0] = rect1[0] + x;
		rectTemp[1] = rect1[1] + y;
		rectTemp[2] = rect1[2] + x;
		rectTemp[3] = rect1[3] + y;
		
		return isPeng(rectTemp, rect2);
	}
	
	public static boolean isPeng(int x_1, int y_1, int[] rect1,
			int x_2, int y_2, int[] rect2) {
		return isPeng(x_1, y_1, rect1, x_2, y_2, rect2, 0);
	}
	
	public static boolean isPeng(int x_1, int y_1, int[] rect1,
			int x_2, int y_2, int[] rect2, int trim) {
		
		if (rect1 == null) {
			return false;
		}
		
		if (rect2 == null) {
			return false;
		}
		
		Arrays.fill(rectTemp, 0);
		rectTemp[0] = rect1[0] + x_1 - trim;
		rectTemp[1] = rect1[1] + y_1 - trim;
		rectTemp[2] = rect1[2] + x_1 + trim;
		rectTemp[3] = rect1[3] + y_1 + trim;
		
		Arrays.fill(rectTemp1, 0);
		rectTemp1[0] = rect2[0] + x_2;
		rectTemp1[1] = rect2[1] + y_2;
		rectTemp1[2] = rect2[2] + x_2;
		rectTemp1[3] = rect2[3] + y_2;
		
		return isPeng(rectTemp, rectTemp1);
	}
	
	// 
	public static boolean isInRect(int x, int y, int[] rect) {
		int tempX = x;
		int tempY = y;
		if (tempX > rect[0] && tempX < rect[2]) {
			if (tempY > rect[1] && tempY < rect[3]) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isInRect(int x, int y, int rectX, int rectY, int[] rect) {		
		rectTemp[0] = rect[0] + rectX;
		rectTemp[1] = rect[1] + rectY;
		rectTemp[2] = rect[2] + rectX;
		rectTemp[3] = rect[3] + rectY;
		
		if (x > rectTemp[0] && x < rectTemp[2]) {
			if (y > rectTemp[1] && y < rectTemp[3]) {
				return true;
			}
		}
		return false;
	}
		
	// 判断两点距离
	public static float pointToPointLength(float x1, float y1, float x2, float y2) {
		float xTemp = x1 - x2;
		float yTemp = y1 - y2;
		
		double result = Math.pow(xTemp, 2) + Math.pow(yTemp, 2);
		result = Math.sqrt(result);
		
		return (float) result;
	}
}
