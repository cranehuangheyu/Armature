package com.dsy.ActionBoneParse;

import com.dsy.ActionBoneParse.armature.Armature;
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

	public Armature armature;

	// 坐标
	public float xPos;

	public float yPos;

	public String playWithAnimation;

	public int currentAnimationFrameMax;

	public float currentAnimationFrameRate;

	public int frame;

	public float timeDelta;

	public MoveData moveData;

	public DrawableData drawableDataArray[];

	// 时间缩放
	public float timeScale;

	// 朝向
	public float faceTo;

	public Matrix matrixAssistant;

	// 身体缩放
	public float bodyScale;
	
	// In boneData.drawableDataVector index, because search drawableData from boneData is waste time.
	public int indexInBoneData;

	public ArmatureObj(String jsonPath) {
		xPos = 400;

		yPos = 480;
		
		armature = AnimationManager.armatureMap.get(jsonPath);
		
		if (armature == null) {
			AnimationManager.addArmature(jsonPath);
			armature = AnimationManager.armatureMap.get(jsonPath);
		}
		
		if (armature == null) {
			Log.e("error!!", "the armature can not found!");
		}

		drawableDataArray = new DrawableData[armature.armatureData.boneDatas
				.size()];
		for (int i = 0; i < drawableDataArray.length; i++) {
			drawableDataArray[i] = new DrawableData();
			BoneData boneData = armature.armatureData.boneDatas
					.get(i);
			boneData.drawableDataVector.add(drawableDataArray[i]);
			indexInBoneData = boneData.drawableDataVector.size() - 1;
			
			drawableDataArray[i].boneData = boneData;
			
			drawableDataArray[i].contourVertexArray = new float[boneData.displayDataVector.size()][][];
			for (int j = 0; j < boneData.displayDataVector.size(); j++) {
				if (boneData.displayDataVector.get(j).displayType == 0 && boneData.displayDataVector.get(j).textureData.contourData.x != null) {
					drawableDataArray[i].contourVertexArray[j] = new float[boneData.displayDataVector.get(j).textureData.contourData.x.length][2];
				}
			}
		}

		timeDelta = 0;

		timeScale = 1;

		faceTo = 0;

		bodyScale = 0.5f;

		matrixAssistant = new Matrix();
	}

	private DrawableData drawableDataForDraw;

	public void draw(Graphics g) {

//		time2 = System.currentTimeMillis();

		for (int i = moveData.moveDataVector.size() - 1; i > -1; i--) {
			MoveBoneData moveBoneData = moveData.moveDataVector.get(i);
			drawableDataForDraw = moveBoneData.boneData.drawableDataVector.get(indexInBoneData);

			if (drawableDataForDraw.isComputed
					&& drawableDataForDraw.boneData.displayDataVector.get(drawableDataForDraw.drawIndex).bitmap != null
					&& drawableDataForDraw.isVisable) {
				g.drawBitmapBone(
						drawableDataForDraw.boneData.displayDataVector.get(drawableDataForDraw.drawIndex).bitmap,
						drawableDataForDraw.skinMatrix, drawableDataForDraw.alpha);
			}
			drawableDataForDraw.isComputed = false;
		}
		
		// draw contour vertex
//		drawContourVertex(g);

//		time2 = System.currentTimeMillis() - time2;
//		Log.e("timeDelta logic", "" + time2);
	}
	
	public void drawContourVertex(Graphics g) {
		for (int i = moveData.moveDataVector.size() - 1; i > -1; i--) {
			MoveBoneData moveBoneData = moveData.moveDataVector.get(i);
			drawableDataForDraw = moveBoneData.boneData.drawableDataVector.get(indexInBoneData);

			if (drawableDataForDraw.boneData.displayDataVector.get(drawableDataForDraw.drawIndex).bitmap != null
					&& drawableDataForDraw.isVisable) {
				if (drawableDataForDraw.contourVertexArray != null) {
					for (int j = 0; j < drawableDataForDraw.contourVertexArray.length; j++) {
						float point1[];
						float point2[];
						if (j == drawableDataForDraw.contourVertexArray.length - 1) {
							point1 = drawableDataForDraw.contourVertexArray[drawableDataForDraw.drawIndex][j];
							point2 = drawableDataForDraw.contourVertexArray[drawableDataForDraw.drawIndex][0];
						} else {
							point1 = drawableDataForDraw.contourVertexArray[drawableDataForDraw.drawIndex][j];
							point2 = drawableDataForDraw.contourVertexArray[drawableDataForDraw.drawIndex][j + 1];
						}
						
						g.save();
						g.setColor(255, 0, 0);
						g.drawLine(point1[0], point1[1], point2[0], point2[1]);
						g.restore();
					}
				}
			}
			drawableDataForDraw.isComputed = false;
		}
	}

	// 切换到指定的Animation
	public void playWithAnimationName(String name) {
		playWithAnimation = name;

		moveData = armature.animationData.moveDataMap
				.get(playWithAnimation);

		currentAnimationFrameMax = moveData.dr - 1;

		currentAnimationFrameRate = 1000 / (moveData.sc * 60);

		frame = 0;
	}
	
	// 切换到指定的Animation
	public void playWithAnimationIndex(int index) {
		if (index < 0 || index >= armature.animationData.moveDataVector.size()) {
			return ;
		}
		
		moveData = armature.animationData.moveDataVector
				.get(index);

		currentAnimationFrameMax = moveData.dr - 1;

		currentAnimationFrameRate = 1000 / (moveData.sc * 60);

		frame = 0;
	}

	public void makeRootBoneData(MoveBoneData moveBoneData) {

		DrawableData drawableDataRootBone = moveBoneData.boneData.drawableDataVector.get(indexInBoneData);

		if (drawableDataRootBone.isComputed) {
			return;
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
		int alpha1 = 0;
		int alpha2 = 0;
		int alphaOffset = 0;

		// int frameDataIndex = getFrameDataIndex(frame, moveBoneData);

		if (frame == 0) {
			drawableDataRootBone.frameDataIndex = 0;
		}

		FrameData frameData2 = null;
		if (drawableDataRootBone.frameDataIndex < moveBoneData.frameDataVector
				.size() - 1) {
			frameData2 = moveBoneData.frameDataVector
					.get(drawableDataRootBone.frameDataIndex + 1);
		}

		if (frameData2 != null) {
			if (frame >= frameData2.fi) {
				drawableDataRootBone.frameDataIndex++;
				if (drawableDataRootBone.frameDataIndex >= moveBoneData.frameDataVector
						.size()) {
					drawableDataRootBone.frameDataIndex--;
				}
			}
		}

		// current frame
		FrameData frameData = moveBoneData.frameDataVector
				.get(drawableDataRootBone.frameDataIndex);
		
		if (drawableDataRootBone.frameDataIndex < moveBoneData.frameDataVector
				.size() - 1) {
			frameData2 = moveBoneData.frameDataVector
					.get(drawableDataRootBone.frameDataIndex + 1);
		} else {
			frameData2 = null;
		}
		
		if (frameData.dI == -1000 || frameData.dI == -1) {
			drawableDataRootBone.isVisable = false;
			drawableDataRootBone.drawIndex = 0;
		} else {
			drawableDataRootBone.isVisable = true;
			drawableDataRootBone.drawIndex = frameData.dI;
		}

		BoneData boneData = moveBoneData.boneData;
		
		DisplayData displayData = boneData.displayDataVector.get(drawableDataRootBone.drawIndex);
					
		alpha1 = frameData.isColor ? frameData.alpha : 255;

		if (displayData.displayType == 0) {
			SkinData skinData = displayData.skinData;

			x = boneData.x + frameData.x;
			y = boneData.y + frameData.y;
			cX = boneData.cX + frameData.cX - 1;
			cY = boneData.cY + frameData.cY - 1;
			kX = boneData.kX + frameData.kX;
			kY = boneData.kY + frameData.kY;

			// add interpolation
			if (frameData2 != null) {
				
				alpha2 = frameData2.isColor ? frameData2.alpha : 255;
				
				xOffset = frameData2.x - frameData.x;
				yOffset = frameData2.y - frameData.y;
				cXOffset = frameData2.cX - frameData.cX;
				cYOffset = frameData2.cY - frameData.cY;
				kXOffset = frameData2.kX - frameData.kX;
				kYOffset = frameData2.kY - frameData.kY;
				alphaOffset = alpha2 - alpha1;

				float rate = computeTweenRate(frame, frameData, frameData2);
				xOffset = xOffset * rate;
				yOffset = yOffset * rate;
				cXOffset = cXOffset * rate;
				cYOffset = cYOffset * rate;
				kXOffset = kXOffset * rate;
				kYOffset = kYOffset * rate;
				alphaOffset *= rate;

				x += xOffset;
				y += yOffset;
				cX += cXOffset;
				cY += cYOffset;
				kX += kXOffset;
				kY += kYOffset;
			}

			drawableDataRootBone.xBone = x;
			drawableDataRootBone.yBone = y;
			drawableDataRootBone.cXBone = cX;
			drawableDataRootBone.cYBone = cY;
			drawableDataRootBone.kXBone = kX;
			drawableDataRootBone.kYBone = kY;
			
			drawableDataRootBone.alpha = alpha1 + alphaOffset;

			// compute bone matrix
			drawableDataRootBone.boneMatrix.reset();
			drawableDataRootBone.boneMatrix.postTranslate(x, -y);
			drawableDataRootBone.boneMatrix.postScale(cX, cY, x, -y);
			Graphics.mySkewTranslation(drawableDataRootBone.boneMatrix, -kY,
					kX, x, -y);

			x += skinData.x;
			y += skinData.y;
			kX += skinData.kX;
			kY += skinData.kY;
			cX += skinData.cX;
			cY += skinData.cY;

			drawableDataRootBone.x = x;
			drawableDataRootBone.y = y;
			drawableDataRootBone.cX = cX;
			drawableDataRootBone.cY = cY;
			drawableDataRootBone.kX = kX;
			drawableDataRootBone.kY = kY;

			// compute skin matrix
			drawableDataRootBone.skinMatrix.reset();
			drawableDataRootBone.skinMatrix.postTranslate(skinData.x,
					-skinData.y);
			drawableDataRootBone.skinMatrix
					.postTranslate(
							-drawableDataRootBone.boneData.displayDataVector.get(drawableDataRootBone.drawIndex).textureData.pX
									* displayData.bitmap.getWidth(),
							-(1 - drawableDataRootBone.boneData.displayDataVector.get(drawableDataRootBone.drawIndex).textureData.pY)
									* displayData.bitmap.getHeight());
			drawableDataRootBone.skinMatrix.postScale(skinData.cX, skinData.cY,
					skinData.x, -skinData.y);
			Graphics.mySkewTranslation(drawableDataRootBone.skinMatrix,
					-skinData.kY, skinData.kX, skinData.x, -skinData.y);

			drawableDataRootBone.skinMatrix
					.postConcat(drawableDataRootBone.boneMatrix);

			matrixAssistant.setScale(bodyScale, bodyScale);
			if (faceTo == 1) {
				matrixAssistant.postScale(-1, 1);
			}
			drawableDataRootBone.skinMatrix.postConcat(matrixAssistant);

			drawableDataRootBone.skinMatrix.postTranslate(xPos, yPos);
						
			computeVertex(drawableDataRootBone);

			drawableDataRootBone.isComputed = true;
		}
		// }
	}

	public void makeParentBoneData(MoveBoneData moveBoneData) {
		DrawableData drawableParentBoneData = moveBoneData.boneData.drawableDataVector.get(indexInBoneData);

		if (drawableParentBoneData.isComputed) {
			return;
		}

		BoneData parentBoneData = drawableParentBoneData.boneData.parentBoneData;

		if (parentBoneData.parentBoneData == null) {
			makeRootBoneData(moveBoneData.parentMoveBoneData);
		} else {
			makeParentBoneData(moveBoneData.parentMoveBoneData);
		}

		// compute parent bone data
		DrawableData drawableData = drawableParentBoneData;
		DrawableData parentDrawableData = drawableDataArray[parentBoneData.index];

		DisplayData displayData = drawableData.boneData.displayDataVector.get(drawableParentBoneData.drawIndex);

		// children bone
		BoneData boneData = drawableData.boneData;
		SkinData skinData = boneData.displayDataVector.get(drawableParentBoneData.drawIndex).skinData;

		if (frame == 0) {
			drawableParentBoneData.frameDataIndex = 0;
		}

		FrameData frameData2 = null;
		if (drawableParentBoneData.frameDataIndex < moveBoneData.frameDataVector
				.size() - 1) {
			frameData2 = moveBoneData.frameDataVector
					.get(drawableParentBoneData.frameDataIndex + 1);
		}

		if (frameData2 != null) {
			if (frame >= frameData2.fi) {
				drawableParentBoneData.frameDataIndex++;
				if (drawableParentBoneData.frameDataIndex >= moveBoneData.frameDataVector
						.size()) {
					drawableParentBoneData.frameDataIndex--;
				}
			}
		}

		// int frameDataIndex = getFrameDataIndex(frame, moveBoneData);

		// current frame
		FrameData frameData = moveBoneData.frameDataVector
				.get(drawableParentBoneData.frameDataIndex);
		
		if (frameData.isColor) {
			drawableParentBoneData.alpha = frameData.alpha;
		} else {
			drawableParentBoneData.alpha = 255;
		}

		if (drawableParentBoneData.frameDataIndex < moveBoneData.frameDataVector
				.size() - 1) {
			frameData2 = moveBoneData.frameDataVector
					.get(drawableParentBoneData.frameDataIndex + 1);
		} else {
			frameData2 = null;
		}
		
		if (frameData.dI == -1000 || frameData.dI == -1) {
			drawableParentBoneData.isVisable = false;
			drawableParentBoneData.drawIndex = 0;
		} else {
			drawableParentBoneData.isVisable = true;
			drawableParentBoneData.drawIndex = frameData.dI;
		}
		
		int alpha1 = 0;
		int alpha2 = 0;
		int alphaOffset = 0;
		
		alpha1 = frameData.isColor ? frameData.alpha : 255;

		float xOffset = 0;
		float yOffset = 0;
		float cXOffset = 0;
		float cYOffset = 0;
		float kXOffset = 0;
		float kYOffset = 0;

		if (frameData2 != null) {
			
			alpha2 = frameData2.isColor ? frameData2.alpha : 255;
			
			xOffset = frameData2.x - frameData.x;
			yOffset = frameData2.y - frameData.y;
			cXOffset = frameData2.cX - frameData.cX;
			cYOffset = frameData2.cY - frameData.cY;
			kXOffset = frameData2.kX - frameData.kX;
			kYOffset = frameData2.kY - frameData.kY;
			alphaOffset = alpha2 - alpha1;
			
			float rate = computeTweenRate(frame, frameData, frameData2);
			xOffset = xOffset * rate;
			yOffset = yOffset * rate;
			cXOffset = cXOffset * rate;
			cYOffset = cYOffset * rate;
			kXOffset = kXOffset * rate;
			kYOffset = kYOffset * rate;
			alphaOffset *= rate;
		}

		// parent bone kx ky
		// PointF pointF = pointRotateByPoint(parentDrawableData.xBone +
		// boneData.x + frameData.x + xOffset,
		// parentDrawableData.xBone,
		// parentDrawableData.yBone + boneData.y + frameData.y + yOffset,
		// parentDrawableData.yBone,
		// parentDrawableData.kYBone);
		// drawableData.x = pointF.x;
		// drawableData.y = pointF.y;
		
		drawableParentBoneData.alpha = alpha1 + alphaOffset;

		// children bone data
		drawableData.x = boneData.x + frameData.x + xOffset;
		drawableData.y = boneData.y + frameData.y + yOffset;
		drawableData.cX = boneData.cX + frameData.cX - 1;
		drawableData.cY = boneData.cY + frameData.cY - 1;
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
		drawableParentBoneData.boneMatrix.reset();
		drawableParentBoneData.boneMatrix.postTranslate(drawableData.x,
				-drawableData.y);
		drawableParentBoneData.boneMatrix.postScale(drawableData.cX,
				drawableData.cY, drawableData.x, -drawableData.y);
		Graphics.mySkewTranslation(drawableParentBoneData.boneMatrix,
				-drawableData.kY, drawableData.kX, drawableData.x,
				-drawableData.y);

		// concat parent bone matrix
		drawableParentBoneData.boneMatrix
				.postConcat(parentDrawableData.boneMatrix);

		// pointF = pointRotateByPoint(parentDrawableData.xBone + boneData.x +
		// frameData.x + xOffset + skinData.x,
		// parentDrawableData.xBone,
		// parentDrawableData.yBone + boneData.y + frameData.y + yOffset +
		// skinData.y,
		// parentDrawableData.yBone,
		// parentDrawableData.kYBone);

		drawableData.x += skinData.x;
		drawableData.y += skinData.y;
		drawableData.kX += skinData.kX;
		drawableData.kY += skinData.kY;
		drawableData.cX += skinData.cX;
		drawableData.cY += skinData.cY;

		// compute skin matrix
		drawableParentBoneData.skinMatrix.reset();
		drawableParentBoneData.skinMatrix
				.postTranslate(skinData.x, -skinData.y);
		drawableParentBoneData.skinMatrix.postTranslate(
				-drawableData.boneData.displayDataVector.get(drawableParentBoneData.drawIndex).textureData.pX
						* displayData.bitmap.getWidth(),
				-(1 - drawableData.boneData.displayDataVector.get(drawableParentBoneData.drawIndex).textureData.pY)
						* displayData.bitmap.getHeight());
		drawableParentBoneData.skinMatrix.postScale(skinData.cX, skinData.cY,
				skinData.x, -skinData.y);
		Graphics.mySkewTranslation(drawableParentBoneData.skinMatrix,
				-skinData.kY, skinData.kX, skinData.x, -skinData.y);

		drawableParentBoneData.skinMatrix
				.postConcat(drawableParentBoneData.boneMatrix);

		matrixAssistant.setScale(bodyScale, bodyScale);
		if (faceTo == 1) {
			matrixAssistant.postScale(-1, 1);
		}
		drawableParentBoneData.skinMatrix.postConcat(matrixAssistant);

		drawableParentBoneData.skinMatrix.postTranslate(xPos, yPos);
		
		computeVertex(drawableParentBoneData);

		drawableData.isComputed = true;
	}

	public long time2;

	public void logic(int dt) {

		timeDelta += dt;
		if (timeDelta >= currentAnimationFrameRate * timeScale) {

			int temp = (int) (timeDelta / (currentAnimationFrameRate * timeScale));

			temp = 1;

			// If you need frame skip, set 1 to temp.
			frame += temp;
			if (frame >= currentAnimationFrameMax) {
				frame = 0;
			}

			timeDelta = timeDelta - temp
					* (currentAnimationFrameRate * timeScale);
		}

		// Log.e("deltaTime", "" + dt);

		update();
	}

	public void update() {

		for (int i = moveData.moveDataVector.size() - 1; i > -1; i--) {
			MoveBoneData moveBoneData = moveData.moveDataVector.get(i);
			// drawableData = moveBoneData.boneData.drawableData;

			if (moveBoneData.boneData.parentBoneData == null) {
				makeRootBoneData(moveBoneData);
			} else {
				makeParentBoneData(moveBoneData);
			}
		}

		// for (int i = 0; i < drawableDataArray.length; i++) {
		// drawableData = drawableDataArray[i];
		// if (drawableData.boneData.parentBoneData == null) {
		// makeRootBoneData(i);
		// } else {
		// makeParentBoneData(i);
		// }
		// }
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

	public PointF pointRotateByPoint(float x, float rx0, float y, float ry0,
			float a) {
		float x0 = (float) ((x - rx0) * Math.cos(a) - (y - ry0) * Math.sin(a) + rx0);
		float y0 = (float) ((x - rx0) * Math.sin(a) + (y - ry0) * Math.cos(a) + ry0);
		pointF.set(x0, y0);
		return pointF;
	}
	
	public PointF computePointInMatrix(float x, float y, Matrix matrix) {
		float values[] = new float[9];
		matrix.getValues(values);
		x = values[0] * x + values[1] * y + values[2];
		y = values[3] * x + values[4] * y + values[5];
		pointF.set(x, y);
		return pointF;
	}
	
	public void computeVertex(DrawableData drawableData)
	{
		if (drawableData.contourVertexArray == null || drawableData.contourVertexArray[drawableData.drawIndex] == null) {
			return ;
		}
		
		for (int i = 0; i < drawableData.contourVertexArray[drawableData.drawIndex].length; i++) {
			PointF tempPointF = computePointInMatrix( 
					drawableData.boneData.displayDataVector.get(drawableData.drawIndex).textureData.contourData.x[i], 
					drawableData.boneData.displayDataVector.get(drawableData.drawIndex).textureData.contourData.y[i], 
					drawableData.skinMatrix);
			
			drawableData.contourVertexArray[drawableData.drawIndex][i][0] = tempPointF.x;
			drawableData.contourVertexArray[drawableData.drawIndex][i][1] = tempPointF.y;
		}
	}
}
