package com.dsy.ActionBoneParse.armature;

import android.graphics.Matrix;

public class DrawableData {
	public float x;
	public float y;
	public float z;
	public float cX;
	public float cY;
	public float kX;
	public float kY;
		
	public float xBone;
	public float yBone;
	public float zBone;
	public float cXBone;
	public float cYBone;
	public float kXBone;
	public float kYBone;
		
	public BoneData boneData;
	
	public boolean isComputed;
	
	public int parentIndex = -1;

	public Matrix boneMatrix;
	
	public Matrix skinMatrix;
	
	public boolean isVisable;
	
	public int frameDataIndex;
	
	public float contourVertexArray[][][];
	
	public int alpha;
	
	public int drawIndex;
		
	public DrawableData() {
		boneMatrix = new Matrix();
		
		skinMatrix = new Matrix();
	}
}
