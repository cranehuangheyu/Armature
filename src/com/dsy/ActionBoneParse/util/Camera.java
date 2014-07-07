/*
 * 创建日期 2006-11-30
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 �?首�?�?�?Java �?代码样式 �?代码com.fruitfactory.oo.oop.model */
package com.dsy.ActionBoneParse.util;

import com.dsy.ActionBoneParse.UserData;

/**
 * @author yangsong
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 �?首�?�?�?Java �?代码样式 �?代码模板
 */
public class Camera implements UserData{
    public int CameraXInScreen;
    public int CameraYInScreen;
    public int CameraX;
    public int CameraY;
    public int CameraW;
    public int CameraH;
    public int WorldX;
    public int WorldY;
    public int WorldW;
    public int WorldH;
//    public TGraphics tg;

    public int getX(int x) {
        return x - CameraX + CameraXInScreen;
    }

    public int getY(int y) {
        return y - CameraY + CameraYInScreen;
    }

    public void setCameraSize(int width, int height) {
        CameraW = width;
        CameraH = height;
    }
    public void setCameraPosition(int x, int y) {
//        System.out.println("设置摄像机的位置");
        CameraX = x;
        CameraY = y;
        moveCamera(0 , 0);
    }
    
    public void setCameraPositionInScreen(int x, int y) {
        CameraXInScreen = x;
        CameraYInScreen = y;
    }

//    public void setGraphcis(TGraphics g1) {
//        tg = g1;
//    }
   public void setWorld(int x, int y, int w, int h) {
        WorldX = x;
        WorldY = y;
        WorldW = w;
        WorldH = h;
    }
    int ax1, ax2, ay1, ay2;
    void setArea(int x, int y, int w, int h) {
        ax1 = x;
        ax2 = x + w;
        ay1 = y;
        ay2 = y + h;
    }

    public boolean drawAble(
            int X,
            int Y,
            int w,
            int h,
            int trans) {
        switch (trans) {
        case TRANS_NONE://不翻�?
            setArea(X, Y, w, h);
            break;
        case TRANS_ROT90://旋转90

            setArea(X - h, Y, h, w);
            break;
        case TRANS_ROT180://旋转180

            setArea(X - w, Y - h, w, h);
            break;
        case TRANS_ROT270://旋转270

            setArea(X, Y - w, h, w);
            break;
        case TRANS_MIRROR://翻转

            setArea(X - w, Y, w, h);
            break;
        case TRANS_MIRROR_ROT90://翻转后旋�?0

            setArea(X - h, Y - w, h, w);
            break;
        case TRANS_MIRROR_ROT180://翻转后旋�?80

            setArea(X, Y - h, w, h);
            break;
        case TRANS_MIRROR_ROT270://翻转后旋�?70

            setArea(X, Y, h, w);
            break;
        }
        return Util.crashAble(
                ax1, ay1, ax2, ay2,
                CameraX, CameraY, CameraX + CameraW, CameraY + CameraH);
    }
    private int Limit(int value, int min, int max) {
        if (value > max) {
            return max;
        } else if (value < min) {
            return min;
        } else {
            return value;
        }
    }
    private int filmValidateCameraX(int X) {
        return Limit(X, 0, WorldW);
    }

    private int filmValidateCameraY(int Y) {
        return Limit(Y, 0, WorldH);
    }
    public void moveCamera(int x, int y) {
//	    横坐标确�?        
    	if (CameraX + x <= WorldX) {
            CameraX = WorldX;
        } else if (CameraX + x + CameraW >= WorldX + WorldW) {
            CameraX = WorldX + WorldW - CameraW;
        } else {
            CameraX += x;
        }
//	    纵坐标确�?        
        if (CameraY + y <= WorldY) {
            CameraY = WorldY;
        } else if (CameraY + y + CameraH >= WorldY + WorldH) {
            CameraY = WorldY + WorldH - CameraH;
        } else {
            CameraY += y;
        }
    }
    
    public boolean moveCameraCustom(int x, int y) {
    	boolean flag = false;
//	    横坐标确�?        
    	if (CameraX + x <= WorldX) {
            CameraX = WorldX;
        } else if (CameraX + x + CameraW >= WorldX + WorldW) {
//            CameraX = WorldX + WorldW - CameraW;
        	// 到头回来
            CameraX = 0;
            
            flag = true;
        } else {
            CameraX += x;
        }
//	    纵坐标确�?        
        if (CameraY + y <= WorldY) {
            CameraY = WorldY;
        } else if (CameraY + y + CameraH >= WorldY + WorldH) {
            CameraY = WorldY + WorldH - CameraH;
        } else {
            CameraY += y;
        }
        
        return flag;
    }
}
