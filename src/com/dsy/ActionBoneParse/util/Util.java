/*
 * 创建日期 2006-11-30
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 �?首�?�?�?Java �?代码样式 �?代码模板
 */
package com.dsy.ActionBoneParse.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;

import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

import android.graphics.Bitmap;

/**
 * @author yangsong
 * 
 *         TODO 要更改此生成的类型注释的模板，请转至 窗口 �?首�?�?�?Java �?代码样式 �?代码模板
 */
public class Util {
	// 清除图片数组
	public static void cleanImageArray(Bitmap[][] bitmap) {
		if (bitmap != null) {
			for (int i = 0; i < bitmap.length; i++) {
				for (int j = 0; j < bitmap[i].length; j++) {
					if (bitmap[i][j] != null) {
						bitmap[i][j].recycle();
						bitmap[i][j] = null;
					}
				}
			}
			bitmap = null;
		}
	}

	// 清除图片数组
	public static void cleanImageArray(Bitmap[] bitmap) {
		if (bitmap != null) {
			for (int i = 0; i < bitmap.length; i++) {
				if (bitmap[i] != null) {
					bitmap[i].recycle();
					bitmap[i] = null;
				}
			}
			bitmap = null;
		}
	}

	// 清除图片数组
	public static void cleanImageArray(Bitmap bitmap) {
		if (bitmap != null) {
			bitmap.recycle();
			bitmap = null;
		}
	}

	// 判断飞行方向
	final public static double flyDirection(float[] temp, int[] pointArray) {
		int increase_x = pointArray[2] - pointArray[0];
		int increase_y = pointArray[3] - pointArray[1];

		double degree = Math.toDegrees(Math.atan2(increase_x, increase_y));
		degree -= 90;

		float scaleX = (float) Math.cos(Math.toRadians(-degree));
		float scaleY = (float) Math.sin(Math.toRadians(-degree));

		temp[0] = scaleX;
		temp[1] = scaleY;

		if (degree < 0) {
			degree += 360;
		}
		return degree;
	}

	// 角度换算
	final public static void flyDirection(double degree, float[] temp) {
		float scaleX = (float) Math.cos(Math.toRadians(-degree));
		float scaleY = (float) Math.sin(Math.toRadians(-degree));

		temp[0] = scaleX;
		temp[1] = scaleY;
	}

	final public static boolean crashAble(int ax1, int ay1, int ax2, int ay2,
			int bx1, int by1, int bx2, int by2) {
		if (ax1 < bx2 && ax2 > bx1 && ay1 < by2 && ay2 > by1)
			return true;
		else
			return false;
	}

	// z轴判断的碰撞用于人物碰撞
	final public static boolean crashAble(int az1, int az2, int bz1, int bz2) {
		// 人物Y轴上�?
		if (az1 < bz2 && az2 > bz1)
			return true;
		else
			return false;
	}

	// 随机数部�?
	public static InputStream getInputStream(String path) {
		// return new Object().getClass().getResourceAsStream(path);
		return AndroidUtil.getResourceAsStream(path);// Android创建InputStream
	}

	// ////////////////////////////////////数据记录部分///////////////////////
	// 定义�?��RecordStore类实例，记得数据索引�?�?��而不�?
	static public RecordStore store;
	static ByteArrayInputStream bis;
	static DataInputStream dis;
	static byte[] data;

	/*
	 * 打开�?��store, 如不存在，建立一个并初始化store的内�?
	 */
	static public byte openStore(String dbname, int length) {
		try {
			// 尝试打开�?��已有store，如不存在，抛出异常
			store = RecordStore.openRecordStore(dbname, false);
			// System.out.println("Read a old store");
			return 0;
		} catch (RecordStoreException ex) {
			// 接收到store不存在的异常，建立一个store
			// try {
			// store = RecordStore.openRecordStore(dbname, true);
			// data = new byte[1];
			// data[0] = 0;
			// for (int i = 0; i < length; i++) {
			// store.addRecord(data, 0, data.length);
			// }
			// System.out.println("Creat a new store");
			// return 1;
			// } catch (Exception e) {
			// //创建新store失败
			// System.out.println("Creat new store error");
			// return 2;
			// }
			return 1;
		}
	}

	/*
	 * 建立�?��并初始化store的内�?
	 */
	static public byte creatStore(String dbname, int length) {

		// 接收到store不存在的异常，建立一个store
		try {
			store = RecordStore.openRecordStore(dbname, true);
			data = new byte[1];
			data[0] = 0;
			for (int i = 0; i < length; i++) {
				store.addRecord(data, 0, data.length);
			}
			System.out.println("Creat a new store");
			return 0;
		} catch (Exception e) {
			// 创建新store失败
			System.out.println("Creat new store error");
			return 1;
		}
	}

	/*
	 * 关闭store
	 */
	static public void closeStore() {
		try {
			if (store != null) {
				store.closeRecordStore();
			}
		} catch (RecordStoreException ex) {

		}
	}

	static public void readData(int index) {
		try {
			data = null;
			data = store.getRecord(index);
			bis = new ByteArrayInputStream(data);
			dis = new DataInputStream(bis);
		} catch (Exception e) {
		}
	}

	/**
	 * 读取整型数据
	 * 
	 * @param index
	 *            整型数据位置
	 * @return 读取的整型数�?
	 */
	static public int getIntData(int index) {
		readData(index);
		try {
			return dis.readInt();
		} catch (Exception e) {
			// System.out.println("get int data error");
			return 0;
		}
	}

	/**
	 * 读取整型数组
	 * 
	 * @param index
	 *            整型数据位置
	 * @return 读取的整型数�?
	 */
	static public int[] getIntsData(int index, int length) {
		readData(index);
		int[] intData = new int[length];
		try {
			for (int i = 0; i < length; i++) {
				intData[i] = (((0xff & (data[i * 4])) << 24)
						| ((0xff & (data[i * 4 + 1])) << 16)
						| ((0xff & (data[i * 4 + 2])) << 8) | (0xff & data[i * 4 + 3]));
				// System.out.println("read ints "+ intData[i]);
			}
			return intData;
		} catch (Exception e) {
			System.out.println("get ints data error");
			return null;
		}
	}

	/**
	 * 读取字节数据
	 * 
	 * @param index
	 *            字节数据位置
	 * @return 读取的字节数�?
	 */
	static public byte getByteData(int index) {
		readData(index);
		return data[0];
	}

	/**
	 * 读取字节数组数据
	 * 
	 * @param index
	 *            字节数组位置
	 * @return 读取的字节数组数�?
	 */
	static public byte[] getBytesData(int index) {
		readData(index);
		return data;
	}

	/**
	 * 存入整型数据
	 * 
	 * @param data
	 *            整型数据
	 * @param id
	 *            存入位置
	 */
	static public void writeIntData(int intData, int id) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			dos.writeInt(intData);
			dos.flush();
			store.setRecord(id, bos.toByteArray(), 0, bos.toByteArray().length);
		} catch (Exception e) {
			System.out.println("write int error");
			// System.out.println("写入错误的整型数的ID = " + id);
		}
	}

	/**
	 * 存入整型数组
	 * 
	 * @param data
	 *            整型数据
	 * @param id
	 *            存入位置
	 */
	static public void writeIntsData(int[] intData, int id, int length) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		data = new byte[length * 4];
		try {
			for (int i = 0; i < length; i++) {
				data[i * 4] = (byte) (intData[i] >>> 24);
				data[i * 4 + 1] = (byte) (intData[i] >>> 16);
				data[i * 4 + 2] = (byte) (intData[i] >>> 8);
				data[i * 4 + 3] = (byte) intData[i];
				// System.out.println("id = " + id + "   " + "write ints "+
				// intData[i]);
			}
			writeBytesData(data, id);
			// for(int i = 0; i < intData.length; i++){
			// dos.writeInt(intData[i]);
			// dos.flush();
			// }
			// store.setRecord(id,bos.toByteArray(),0,bos.toByteArray().length);
		} catch (Exception e) {
			System.out.println("write ints error");
			// System.out.println("写入出错");
		}
	}

	/**
	 * 存入字节数据
	 * 
	 * @param data
	 *            字节数据
	 * @param id
	 *            存入位置
	 */
	static public void writeByteData(byte data, int id) {
		byte newData[] = new byte[1];
		newData[0] = data;
		try {
			store.setRecord(id, newData, 0, 1);
		} catch (Exception e) {
			System.out.println("write byte error");
			// System.out.println("写入错误字节的ID = " + id);
		}
	}

	/**
	 * 存入字节数组
	 * 
	 * @param data
	 *            字节数组数据
	 * @param id
	 *            存入位置
	 */
	static public void writeBytesData(byte[] data, int id) {
		try {
			store.setRecord(id, data, 0, data.length);
		} catch (Exception e) {
			System.out.println("write bytes error");
		}
	}

	/**
	 * 存入字符串数�? *
	 * 
	 * @param Str
	 *            字符串数�? * @param id 存入位置
	 */
	static public void writeStringData(String str, int id) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			dos.writeUTF(str);
			dos.flush();
			store.setRecord(id, bos.toByteArray(), 0, bos.toByteArray().length);
		} catch (Exception e) {
			System.out.println("write String error");
		}
	}

	/**
	 * 读取String数据
	 * 
	 * @param index
	 *            String数据位置
	 * @return 读取的String
	 */
	static public String getStringData(int index) {
		readData(index);
		try {
			return dis.readUTF();
		} catch (Exception e) {
			System.out.println("get String data error");
			return null;
		}
	}

	/**
	 * 存入字符串数�? *
	 * 
	 * @param Str
	 *            字符串数�? * @param id 存入位置
	 */
	static public void writeBooleanData(boolean boo, int id) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			dos.writeBoolean(boo);
			dos.flush();
			store.setRecord(id, bos.toByteArray(), 0, bos.toByteArray().length);
		} catch (Exception e) {
			System.out.println("write boolean error");
		}
	}

	/**
	 * 读取boolean数据
	 * 
	 * @param index
	 *            boolean数据位置
	 * @return 读取的boolean
	 */
	static public boolean getBooleanData(int index) {
		readData(index);
		try {
			return dis.readBoolean();
		} catch (Exception e) {
			System.out.println("get boolean data error");
			return false;
		}
	}
}
