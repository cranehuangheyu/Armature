package com.dsy.ActionBoneParse.util;

import java.io.IOException;
import java.io.InputStream;

import com.dsy.ActionBoneParse.ArmatureActivity;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapManager {
	// 引用
	public static BitmapManager bitmapManager;

	// 水果图片数组
	// 工厂 水果 物品 箱子 任务

	// 箱子图片数组

	private static AssetManager am;

	// 全局
	private static Bitmap image = null;
	private static InputStream is = null;
	
	public static Bitmap getImageFromAssetsFile(String fileName) {
		try {
			if (am == null) {
				am = ArmatureActivity.getContextInstance().getResources().getAssets();
			}
			is = am.open(fileName);
			image = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}
}
