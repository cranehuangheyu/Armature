package com.dsy.ActionBoneParse.util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.dsy.ActionBoneParse.ArmatureActivity;

import android.content.Context;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.util.Log;
public class AndroidUtil {
    private static final String LOG = "PIC_ERROR";
    /**
	 * 控制线程同步
	 */
//	public static ConditionVariable cv = new ConditionVariable(true);
        
	public static String ReadFile(String Path) {
		BufferedReader reader = null;
		String laststr = "";
		StringBuffer stringBuffer = new StringBuffer();
		try {
			InputStream inputStream = AndroidUtil.getResourceAsStream(Path);
//			FileInputStream fileInputStream = new FileInputStream(Path);
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream, "UTF-8");
			reader = new BufferedReader(inputStreamReader);
			stringBuffer.delete(0, stringBuffer.length());
			String tempString = null; 
			while ((tempString = reader.readLine()) != null) {
//				laststr += tempString;
				stringBuffer.append(tempString);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
//		return laststr;
		return stringBuffer.toString();
	}
	
	/**
	 * 读取资源文件夹assets下的文件
	 */
	public static AssetManager am = ArmatureActivity.getInstance().getAssets();
	public static InputStream getResourceAsStream(String name){
		InputStream is = null;
		try { 
			if(name.indexOf('/') == 0){
				is  = am.open(name.substring(1, name.length()));
			}else{
				is  = am.open(name);
			}
			if(is == null){
				Log.e(LOG, name+" is not exist");
			}
		} catch (IOException e) {
			Log.e(LOG, name+" is not exist,error");
			e.printStackTrace();
		}
		return is;
	}
	
	
	private static AudioManager aManager;
	/**
	 * 获取当前系统音乐音量
	 */
	public static int getCurrentMusic(int volume){
		if(ArmatureActivity.getContextInstance() != null){
			aManager = (AudioManager) ArmatureActivity.getContextInstance().getSystemService(Context.AUDIO_SERVICE);
			return aManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		}
		return -1;
	}
	/**
	 * 设置系统媒体音量
	 */
	public static void setMusic(int volume){
		if(ArmatureActivity.getContextInstance() != null){
			aManager = (AudioManager) ArmatureActivity.getContextInstance().getSystemService(Context.AUDIO_SERVICE);
			aManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_VIBRATE);
		}
	}
	
	/**
	 * 设备屏幕分辨�?
	 */
	public static float DM_X , DM_Y;
	/**
	 * 文字大小�?FONT_SIZE[0]= small, FONT_SIZE[1]= medium, FONT_SIZE[2]=large
	 */
	public static int[] FONT_SIZE = new int[3];
	/**
	 * j2me 游戏的屏幕宽�?
	 */
	public static float J2ME_SCREEN_X, J2ME_SCREEN_Y;
	
	/**
	 * 缩放x, y 
	 */
	public static float Scale_x, Scale_y;
	public static boolean showExit = true;
}
