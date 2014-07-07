package com.dsy.ActionBoneParse;

import com.dsy.ActionBoneParse.armature.BoneData;
import com.dsy.ActionBoneParse.armature.DisplayData;
import com.dsy.ActionBoneParse.armature.DrawableData;
import com.dsy.ActionBoneParse.armature.FrameData;
import com.dsy.ActionBoneParse.armature.MoveBoneData;
import com.dsy.ActionBoneParse.armature.MoveData;
import com.dsy.ActionBoneParse.armature.SkinData;
import com.dsy.ActionBoneParse.armature.TextureData;
import com.dsy.ActionBoneParse.manager.AnimationManager;
import com.dsy.ActionBoneParse.manager.PlistManager;
import com.dsy.ActionBoneParse.util.Graphics;

import android.R.drawable;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.Log;

public class ArmatureObj {

	public AnimationManager animationManager;

	public float xPos;

	public float yPos;

	public String playWithAnimation;

	public int currentAnimationFrameMax;

	public float currentAnimationFrameRate;

	public int frame;

	public float timeDelta;
	
	public MoveData moveData;
	
	public DrawableData drawableDataArray[];
	
	public float timeScale;
	
	public float faceTo;
	
	public Matrix matrixAssistant;
	
	public float bodyScale;

	public ArmatureObj() {
		xPos = 400;

		yPos = 480;
		
		animationManager = new AnimationManager("DemoPlayer/Export/DemoPlayer.ExportJson", "DemoPlayer/Export/DemoPlayer0.plist", "DemoPlayer/Export/Resources/");
		
		drawableDataArray = new DrawableData[animationManager.armature.armatureData.boneDataArray.size()];
		for (int i = 0; i < drawableDataArray.length; i++) {
			drawableDataArray[i] = new DrawableData();
		}
				
		for (int j = 0; j < animationManager.armature.armatureData.boneDatas.size(); j++) {
			BoneData boneData = animationManager.armature.armatureData.boneDatas.get(j);
			drawableDataArray[j].boneData = boneData;
		}
		
		playWithAnimation("walk");

		timeDelta = 0;
		
		timeScale = 2;
		
		faceTo = 0;
		
		bodyScale = 0.4f;
		
		matrixAssistant = new Matrix();
	}

	public void draw(Graphics g) {
		
		for (int i = moveData.moveDataVector.size() - 1; i > -1; i--) {
			MoveBoneData moveBoneData = moveData.moveDataVector.get(i);
			
			for (int i1 = 0; i1 < drawableDataArray.length; i1++) {
				drawableData = drawableDataArray[i1];
				
				if (moveBoneData.name.equals(drawableData.boneData.name)) {
					if (drawableData.isComputed && drawableData.boneData.displayData.bitmap != null) {
//						g.drawBitmapBone(drawableData.boneData.displayData.bitmap,
//								xPos + drawableData.x, yPos - drawableData.y, drawableData.cX, drawableData.cY, drawableData.kX, -drawableData.kY, drawableData.boneData.displayData.textureData.pX,
//								1 - drawableData.boneData.displayData.textureData.pY);	
						g.drawBitmapBone(drawableData.boneData.displayData.bitmap, drawableData.skinMatrix);
					}
					drawableData.isComputed = false;
				}
			}
		}
	}
	
	private DrawableData drawableData;

	public void playWithAnimation(String name) {
		playWithAnimation = name;
		
		moveData = animationManager.armature.animationData.moveDataVector
				.get(playWithAnimation);

		currentAnimationFrameMax = moveData.dr;

		currentAnimationFrameRate = 1000 / (moveData.sc * 60);

		frame = 0;
	}
	
	public void makeRootBoneData(int index) {
		
		if (drawableDataArray[index].isComputed) {
			return ;
		}
		
		float x;
		float y;
		float cX;
		float cY;
		float kX;
		float kY;
 
		// offset
		float xOffset = 0;
		float yOffset = 0;
		float cXOffset = 0;
		float cYOffset = 0;
		float kXOffset = 0;
		float kYOffset = 0;

		//
//		MoveBoneData moveBoneData = null;
		int iIndex = 0;
		for (; iIndex < moveData.moveDataVector.size(); iIndex++) {
			MoveBoneData moveBoneData = moveData.moveDataVector.get(iIndex);
			
			Log.e("find move bone data", moveBoneData.name + ",," + drawableDataArray[index].boneData.name);
			if (moveBoneData.name.equals(drawableDataArray[index].boneData.name)) {
				break;
			}
		}
		
		Log.e("iIndex", "" + iIndex);
		Log.e("moveData.moveDataVector", "" + moveData.moveDataVector.size());
		
		if (iIndex >= moveData.moveDataVector.size()) {
			return ;
		}

//		for (int i = 0;i < moveData.moveDataVector.size();i++) {
			MoveBoneData moveBoneData = moveData.moveDataVector.get(iIndex);

			int frameDataIndex = getFrameDataIndex(frame, moveBoneData);

			// current frame
			FrameData frameData = moveBoneData.frameDataVector
					.get(frameDataIndex);

			FrameData frameData2 = null;
			if (frameDataIndex < moveBoneData.frameDataVector.size() - 1) {
				frameData2 = moveBoneData.frameDataVector
						.get(frameDataIndex + 1);
			}
			
			BoneData boneData = moveBoneData.boneData;

			DisplayData displayData = boneData.displayData;

			if (displayData.displayType == 0) {
				SkinData skinData = displayData.skinData;

				x = boneData.x + frameData.x;
				y = boneData.y + frameData.y;
				cX = frameData.cX;
				cY = frameData.cY;
				kX = boneData.kX + frameData.kX;
				kY = boneData.kY + frameData.kY;

				// add interpolation
				if (frameData2 != null) {
					xOffset = frameData2.x - frameData.x;
					yOffset = frameData2.y - frameData.y;
					cXOffset = frameData2.cX - frameData.cX;
					cYOffset = frameData2.cY - frameData.cY;
					kXOffset = frameData2.kX - frameData.kX;
					kYOffset = frameData2.kY - frameData.kY;

					float rate = computeTweenRate(frame, frameData, frameData2);
					xOffset = xOffset * rate;
					yOffset = yOffset * rate;
					cXOffset = cXOffset * rate;
					cYOffset = cYOffset * rate;
					kXOffset = kXOffset * rate;
					kYOffset = kYOffset * rate;

					x += xOffset;
					y += yOffset;
					cX += cXOffset;
					cY += cYOffset;
					kX += kXOffset;
					kY += kYOffset;
				}
				
				drawableDataArray[index].xBone = x;
				drawableDataArray[index].yBone = y;
				drawableDataArray[index].cXBone = cX;
				drawableDataArray[index].cYBone = cY;
				drawableDataArray[index].kXBone = kX;
				drawableDataArray[index].kYBone = kY;
				
				// compute bone matrix
				drawableDataArray[index].boneMatrix.reset();
				drawableDataArray[index].boneMatrix.postTranslate(x, -y);
				drawableDataArray[index].boneMatrix.postScale(cX, cY, x, -y);
				Graphics.mySkewTranslation(drawableDataArray[index].boneMatrix, -kY, kX, x, -y);
								
				x += skinData.x;
				y += skinData.y;
				kX += skinData.kX;
				kY += skinData.kY;

				drawableDataArray[index].x = x;
				drawableDataArray[index].y = y;
				drawableDataArray[index].cX = cX;
				drawableDataArray[index].cY = cY;
				drawableDataArray[index].kX = kX;
				drawableDataArray[index].kY = kY;
				
				Log.e("root index", "" + index);
				
				// compute skin matrix
				drawableDataArray[index].skinMatrix.reset();
				drawableDataArray[index].skinMatrix.postTranslate(skinData.x, -skinData.y);
				drawableDataArray[index].skinMatrix.postTranslate(- drawableData.boneData.displayData.textureData.pX * displayData.bitmap.getWidth(), 
						- (1 - drawableData.boneData.displayData.textureData.pY) * displayData.bitmap.getHeight());
				drawableDataArray[index].skinMatrix.postScale(skinData.cX, skinData.cY, skinData.x, -skinData.y);
				Graphics.mySkewTranslation(drawableDataArray[index].skinMatrix, -skinData.kY,skinData.kX, skinData.x, -skinData.y);
				
				drawableDataArray[index].skinMatrix.postConcat(drawableDataArray[index].boneMatrix);
				
				matrixAssistant.setScale(bodyScale, bodyScale);
				if (faceTo == 1) {
					matrixAssistant.postScale(-1, 1);
				}
				drawableDataArray[index].skinMatrix.postConcat(matrixAssistant);
				
				drawableDataArray[index].skinMatrix.postTranslate(xPos, yPos);
																
				drawableDataArray[index].isComputed = true;
			}
//		}
	}
	
	public void makeParentBoneData(int index) 
	{
		if (drawableDataArray[index].isComputed) {
			return ;
		}
		
		BoneData parentBoneData = drawableDataArray[index].boneData.parentBoneData;
		
		if (parentBoneData.parentBoneData == null) {
			makeRootBoneData(parentBoneData.index);
		} else {
			makeParentBoneData(parentBoneData.index);
		}
		
		// compute parent bone data
		DrawableData drawableData = drawableDataArray[index];
		DrawableData parentDrawableData = drawableDataArray[parentBoneData.index];
		
		DisplayData displayData = drawableData.boneData.displayData;
		
		// children bone
		BoneData boneData = drawableData.boneData;
		SkinData skinData = boneData.displayData.skinData;
		
		int iIndex = 0;
		for (; iIndex < moveData.moveDataVector.size(); iIndex++) {
			MoveBoneData moveBoneData = moveData.moveDataVector.get(iIndex);
			if (moveBoneData.name.equals(boneData.name)) {
				break;
			}
		}
		
		if (iIndex >= moveData.moveDataVector.size()) {
			return ;
		}
		
		// children frame data
		MoveBoneData moveBoneData = moveData.moveDataVector.get(iIndex);

		int frameDataIndex = getFrameDataIndex(frame, moveBoneData);

		// current frame
		FrameData frameData = moveBoneData.frameDataVector
				.get(frameDataIndex);

		FrameData frameData2 = null;
		if (frameDataIndex < moveBoneData.frameDataVector.size() - 1) {
			frameData2 = moveBoneData.frameDataVector
					.get(frameDataIndex + 1);
		}
		
		float xOffset = 0;
		float yOffset = 0;
		float cXOffset = 0;
		float cYOffset = 0;
		float kXOffset = 0;
		float kYOffset = 0;
		
		if (frameData2 != null) {
			xOffset = frameData2.x - frameData.x;
			yOffset = frameData2.y - frameData.y;
			cXOffset = frameData2.cX - frameData.cX;
			cYOffset = frameData2.cY - frameData.cY;
			kXOffset = frameData2.kX - frameData.kX;
			kYOffset = frameData2.kY - frameData.kY;
			float rate = computeTweenRate(frame, frameData, frameData2);
			xOffset = xOffset * rate;
			yOffset = yOffset * rate;
			cXOffset = cXOffset * rate;
			cYOffset = cYOffset * rate;
			kXOffset = kXOffset * rate;
			kYOffset = kYOffset * rate;
		}
		
		// parent bone kx ky
//		PointF pointF = pointRotateByPoint(parentDrawableData.xBone + boneData.x + frameData.x + xOffset, 
//				parentDrawableData.xBone, 
//				parentDrawableData.yBone + boneData.y + frameData.y + yOffset, 
//				parentDrawableData.yBone, 
//				parentDrawableData.kYBone);
//		drawableData.x = pointF.x;
//		drawableData.y = pointF.y;
		
		// children bone data
		drawableData.x = boneData.x + frameData.x + xOffset;
		drawableData.y = boneData.y + frameData.y + yOffset;
		drawableData.cX = frameData.cX + cXOffset;
		drawableData.cY = frameData.cY + cYOffset;
		drawableData.kX = frameData.kX + kXOffset + boneData.kX;
		drawableData.kY = frameData.kY + kYOffset + boneData.kY;
		
		// bone data
		drawableData.xBone = drawableData.x;
		drawableData.yBone = drawableData.y;
		drawableData.cXBone = drawableData.cX;
		drawableData.cYBone = drawableData.cY;
		drawableData.kXBone = drawableData.kX;
		drawableData.kYBone = drawableData.kY;
		
		// compute bone matrix
		drawableDataArray[index].boneMatrix.reset();
		drawableDataArray[index].boneMatrix.postTranslate(drawableData.x, -drawableData.y);
		drawableDataArray[index].boneMatrix.postScale(drawableData.cX, drawableData.cY, drawableData.x, -drawableData.y);
		Graphics.mySkewTranslation(drawableDataArray[index].boneMatrix, -drawableData.kY, drawableData.kX, drawableData.x, -drawableData.y);
		
		// concat parent bone matrix
		drawableDataArray[index].boneMatrix.postConcat(parentDrawableData.boneMatrix);

//		pointF = pointRotateByPoint(parentDrawableData.xBone + boneData.x + frameData.x + xOffset + skinData.x, 
//				parentDrawableData.xBone, 
//				parentDrawableData.yBone + boneData.y + frameData.y + yOffset + skinData.y, 
//				parentDrawableData.yBone, 
//				parentDrawableData.kYBone);
		
		drawableData.x += skinData.x;
		drawableData.y += skinData.y;
		drawableData.kX += skinData.kX;
		drawableData.kY += skinData.kY;
		
		// compute skin matrix
		drawableDataArray[index].skinMatrix.reset();
		drawableDataArray[index].skinMatrix.postTranslate(skinData.x, -skinData.y);
		drawableDataArray[index].skinMatrix.postTranslate(- drawableData.boneData.displayData.textureData.pX * displayData.bitmap.getWidth(), 
				- (1 - drawableData.boneData.displayData.textureData.pY) * displayData.bitmap.getHeight());
		drawableDataArray[index].skinMatrix.postScale(skinData.cX, skinData.cY, skinData.x, -skinData.y);
		Graphics.mySkewTranslation(drawableDataArray[index].skinMatrix, -skinData.kY,skinData.kX, skinData.x, -skinData.y);
		
		drawableDataArray[index].skinMatrix.postConcat(drawableDataArray[index].boneMatrix);
		
		matrixAssistant.setScale(bodyScale, bodyScale);
		if (faceTo == 1) {
			matrixAssistant.postScale(-1, 1);
		}
		drawableDataArray[index].skinMatrix.postConcat(matrixAssistant);
				
		drawableDataArray[index].skinMatrix.postTranslate(xPos, yPos);
		
		drawableData.isComputed = true;
	}

	public void logic(int dt) {

		timeDelta += dt;
		if (timeDelta >= currentAnimationFrameRate * timeScale) {

			int temp = (int) (timeDelta / (currentAnimationFrameRate * timeScale));

			frame += temp;
			if (frame >= currentAnimationFrameMax) {
				frame = 0;
			}

			timeDelta = timeDelta - temp * (currentAnimationFrameRate * timeScale);
		}
		
		update();
	}
	
	public void update() {
		for (int i = 0; i < drawableDataArray.length; i++) {
			drawableData = drawableDataArray[i];
				if (drawableData.boneData.parentBoneData == null) {
					makeRootBoneData(i);
				} else {
					makeParentBoneData(i);
				}
		}
	}

	public float computeTweenRate(int frame, FrameData frameData,
			FrameData frameData2) {
		int a = frameData2.fi - frameData.fi;
		int b = frame - frameData.fi;
		return (float) b / a;
	}

	public int getFrameDataIndex(int frame, MoveBoneData moveBoneData) {
		for (int i = 0; i < moveBoneData.frameDataVector.size(); i++) {
			if (moveBoneData.frameDataVector.get(i).fi > frame) {
				if (i > 0) {
					return i - 1;
				} else {
					return 0;
				}
			}
		}
		return moveBoneData.frameDataVector.size() - 1;
	}
	
	public PointF pointF = new PointF();
	
	public PointF pointRotateByPoint(float x, float rx0, float y, float ry0, float a)
	{
		float x0 = (float) ((x - rx0) * Math.cos(a) - (y - ry0) * Math.sin(a) + rx0);
		float y0 = (float) ((x - rx0) * Math.sin(a) + (y - ry0) * Math.cos(a) + ry0);
		pointF.set(x0, y0);
		return pointF;
	}
}
