package com.dsy.ActionBoneParse;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.armature.oo.oop.R;
import com.dsy.ActionBoneParse.manager.AnimationManager;
import com.dsy.ActionBoneParse.manager.PlistManager;
import com.dsy.ActionBoneParse.plist.PlistHandler;
import com.dsy.ActionBoneParse.util.AndroidUtil;
import com.dsy.ActionBoneParse.view.MySurfaceView;

import dalvik.system.VMRuntime;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class ArmatureActivity extends Activity {

	private static Context context;
	private static Activity activity;
	private MySurfaceView mySurfaceView;
	
	private final float TARGET_HEAP_UTILIZATION = 0.75f;// 堆内存的处理效率
	private final long MIN_HEAP_SIZE = 20 * 1024 * 1024;// 设置最小heap内存8M
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// context ≥ı ºªØ
		context = this;
		activity = this;
		
		killBackgroundProcess();
		initActivity();
		setFullScreen();

		mySurfaceView = new MySurfaceView(this, this.getWindowManager(), this);
		mySurfaceView.gameCreated();

//		KeyListener kl = new KeyListener();
//		mySurfaceView.setOnKeyListener(kl);

		setContentView(mySurfaceView);
	}
	
	private void initActivity() {
		VMRuntime.getRuntime().setMinimumHeapSize(MIN_HEAP_SIZE);
		VMRuntime.getRuntime()
				.setTargetHeapUtilization(TARGET_HEAP_UTILIZATION);
		// 禁止屏幕休眠
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	public void onLowMemory() {
		killBackgroundProcess();
		super.onLowMemory();
	}

	private void setFullScreen() {
		// 去掉通知栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// 去掉标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	/**
	 * 杀死进程
	 */
	private void killBackgroundProcess() {
		ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> apps = activityManager
				.getRunningAppProcesses();
		int mypid = android.os.Process.myPid();
		for (RunningAppProcessInfo runningAppProcessInfo : apps) {
			if (runningAppProcessInfo.pid != mypid
					&& runningAppProcessInfo.importance > RunningAppProcessInfo.IMPORTANCE_SERVICE) {
				android.os.Process.killProcess(runningAppProcessInfo.pid);
			}
		}
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	/**
	 * 强制退出程序
	 */
	@Override
	protected void onDestroy() {
		// if (relativeLayout != null) {
		// relativeLayout.removeAllViews();
		// }
		super.onDestroy();
		System.exit(0);
		// 强制结束进程
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	public static Context getContextInstance() {
		return context;
	}

	public static Activity getInstance() {
		return activity;
	}
}