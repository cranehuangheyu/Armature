package com.dsy.man;

import android.R.drawable;
import android.util.Log;

import com.dsy.ActionBoneParse.ArmatureObj;
import com.dsy.ActionBoneParse.armature.BoneData;
import com.dsy.ActionBoneParse.armature.DisplayData;
import com.dsy.ActionBoneParse.armature.DrawableData;
import com.dsy.ActionBoneParse.armature.FrameData;
import com.dsy.ActionBoneParse.armature.MoveBoneData;
import com.dsy.ActionBoneParse.armature.SkinData;
import com.dsy.ActionBoneParse.util.Graphics;
import com.dsy.ActionBoneParse.util.PengScan;

public class Hunter extends ArmatureObj {

	public Hunter() {
		super("Hero/DemoPlayer.ExportJson");
		// TODO Auto-generated constructor stub
		
//		playWithAnimationName("run");
		playWithAnimationIndex(1);
		
		bodyScale = 1;	
		
		isMoveTo = false;
	}
	
	public void draw(Graphics g) {
		super.draw(g);
	}
	
	public void logic(int dt) {
		super.logic(dt);
		
		if (isMoveTo) {
			if (PengScan.pointToPointLength(xPos, yPos, moveToX, moveToY) < 10) {
				xPos = moveToX;
				yPos = moveToY;
				isMoveTo = false;
			} else {
				float arcDegree = (float) Math.atan2((moveToY - yPos), (moveToX - xPos));
				float sinDegree = (float) Math.sin(arcDegree);
				float cosDegree = (float) Math.cos(arcDegree);
				
//				if (moveToX - xPos >= 0) {
//					xPos += sinDegree * 10;
//				} else {
//					xPos -= sinDegree * 10;
//				}
//				
//				if (moveToY - yPos >= 0) {
//					yPos += cosDegree * 10;
//				} else {
//					yPos -= cosDegree * 10;
//				}
				
				xPos += cosDegree * 10;
				yPos += sinDegree * 10;
				
				Log.e("move", "" + sinDegree + "," + cosDegree);
			}
		}
	}
	
	boolean isMoveTo;
	public float moveToX;
	public float moveToY;
	
	public void setMoveTo(float x, float y) {
		isMoveTo = true;
		moveToX = x;
		moveToY = y;
	}
}
